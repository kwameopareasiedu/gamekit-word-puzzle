package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.annotations.CustomWidgetBuilder;
import dev.gamekit.annotations.CustomWidgetBuilderField;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.ui.widgets.Widget;
import dev.gamekit.utils.ValueCallback;
import dev.gamekit.wordpuzzle.data.Puzzle;
import dev.gamekit.wordpuzzle.data.Slot;

import java.awt.*;
import java.util.List;

/**
 * A custom {@link Panel} widget which allows words on the puzzle to be highlighted and also marks the positions of
 * discovered words
 */
@CustomWidgetBuilder
public class GestureDetector extends Panel {
  private static final Color BG_COLOR = new Color(0xAAB3A48E, true);
  private static final Stroke LINE_STROKE = new BasicStroke(56, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  private static final Color LINE_COLOR = new Color(0xFFEDAF);

  @CustomWidgetBuilderField(comparable = false)
  public Puzzle puzzle;
  @CustomWidgetBuilderField(comparable = false)
  public List<Slot> validSlots;
  @CustomWidgetBuilderField(comparable = false)
  public ValueCallback<Slot> onSlotMarked;

  private boolean mouseDown = false;
  private double colSize, rowSize;
  private int startRow = 0, startCol = 0, endRow = 0, endCol = 0;

  private GestureDetector(GestureDetectorConfig config, Widget child) {
    super(config, child);
  }

  public static GestureDetector create(GestureDetectorConfig.Updater updater, Widget child) {
    GestureDetectorConfig config = new GestureDetectorConfig();
    updater.update(config);
    config.color = BG_COLOR;
    config.cornerRadius = 32;

    return new GestureDetector(config, child);
  }

  @Override
  protected void performPostLayout() {
    super.performPostLayout();

    colSize = absoluteBounds.width / puzzle.cols;
    rowSize = absoluteBounds.height / puzzle.rows;
  }

  @Override
  public void renderSelf(Graphics2D g) {
    super.renderSelf(g);

    Stroke originalStroke = g.getStroke();
    Color originalColor = g.getColor();

    g.setStroke(LINE_STROKE);
    g.setColor(LINE_COLOR);

    for (Slot slot : validSlots) {
      int[][] positions = slot.positions();

      int startRow = positions[0][0] - 1;
      int startCol = positions[0][1] - 1;
      int endRow = positions[slot.length() - 1][0] - 1;
      int endCol = positions[slot.length() - 1][1] - 1;

      g.drawLine(
        (int) (absoluteBounds.x + startCol * colSize + 0.5 * colSize),
        (int) (absoluteBounds.y + startRow * rowSize + 0.5 * rowSize),
        (int) (absoluteBounds.x + endCol * colSize + 0.5 * colSize),
        (int) (absoluteBounds.y + endRow * rowSize + 0.5 * rowSize)
      );
    }

    if (mouseDown) {
      g.drawLine(
        (int) (absoluteBounds.x + startCol * colSize + 0.5 * colSize),
        (int) (absoluteBounds.y + startRow * rowSize + 0.5 * rowSize),
        (int) (absoluteBounds.x + endCol * colSize + 0.5 * colSize),
        (int) (absoluteBounds.y + endRow * rowSize + 0.5 * rowSize)
      );
    }

    g.setColor(originalColor);
    g.setStroke(originalStroke);
  }

  @Override
  public void handleEvent(MouseEvent event) {
    switch (event.type) {
      case DOWN -> {
        mouseDown = true;
        startCol = endCol = (int) ((event.x - absoluteBounds.x) / colSize);
        startRow = endRow = (int) ((event.y - absoluteBounds.y) / rowSize);
        host.triggerRender();
      }
      case MOTION -> {
        if (mouseDown) {
          int endCol = (int) ((event.x - absoluteBounds.x) / colSize);
          int endRow = (int) ((event.y - absoluteBounds.y) / rowSize);
          Slot s = puzzle.getSlot(startRow + 1, startCol + 1, endRow + 1, endCol + 1);

          if (s != null) {
            this.endCol = endCol;
            this.endRow = endRow;
            host.triggerRender();
          }
        }
      }
      case RELEASE -> {
        Slot s = puzzle.getSlot(startRow + 1, startCol + 1, endRow + 1, endCol + 1);

        if (s != null && onSlotMarked != null)
          onSlotMarked.update(s);

        mouseDown = false;
        startCol = startRow = endCol = endRow = 0;
        host.triggerRender();
      }
    }

    super.handleEvent(event);
  }
}
