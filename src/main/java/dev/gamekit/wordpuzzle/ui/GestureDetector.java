package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Empty;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.ui.widgets.PanelConfig;
import dev.gamekit.ui.widgets.Widget;
import dev.gamekit.utils.ValueCallback;
import dev.gamekit.wordpuzzle.data.Puzzle;
import dev.gamekit.wordpuzzle.data.Slot;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class GestureDetector extends Panel {
  private static final Color BG_COLOR = Color.DARK_GRAY;
  private static final Stroke LINE_STROKE = new BasicStroke(56, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  private static final Color LINE_COLOR = Color.GREEN;

  protected Puzzle puzzle;
  protected List<Slot> validSlots;
  protected ValueCallback<Slot> onSlotMarked;

  private boolean mouseDown = false;
  private double colSize, rowSize;
  private int startRow = 0, startCol = 0, endRow = 0, endCol = 0;

  private GestureDetector(PanelConfig config, Puzzle puzzle, ValueCallback<Slot> onSlotMarked) {
    super(config, new Empty());
    this.puzzle = puzzle;
    this.onSlotMarked = onSlotMarked;
  }

  public static GestureDetector create(GestureDetectorConfig.Updater updater) {
    GestureDetectorConfig config = new GestureDetectorConfig();
    updater.update(config);
    config.color = BG_COLOR;
    config.cornerRadius = 32;

    return new GestureDetector(config, config.puzzle, config.onSlotMarked);
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

  public static class GestureDetectorConfig extends PanelConfig {
    public Puzzle puzzle;
    public List<Slot> validSlots;
    public ValueCallback<Slot> onSlotMarked;

    @Override
    public boolean equals(Object obj) {
      return super.equals(obj) &&
        obj instanceof GestureDetectorConfig gestureDetectorConfig &&
        Objects.equals(validSlots, gestureDetectorConfig.validSlots);
    }

    @Override
    public void updateWidget(Widget widget) {
      super.updateWidget(widget);

      GestureDetector gestureDetectorWidget = (GestureDetector) widget;
      gestureDetectorWidget.puzzle = puzzle;
      gestureDetectorWidget.validSlots = validSlots;
      gestureDetectorWidget.onSlotMarked = onSlotMarked;
    }

    @FunctionalInterface
    public interface Updater extends Widget.ConfigUpdater<GestureDetectorConfig> { }
  }
}
