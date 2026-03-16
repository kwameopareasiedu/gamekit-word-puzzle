package dev.gamekit.wordpuzzle;

import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Empty;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.ui.widgets.PanelConfig;
import dev.gamekit.wordpuzzle.data.Puzzle;
import dev.gamekit.wordpuzzle.data.Slot;

import java.awt.*;

public class PuzzlePanel extends Panel {
  private static final Color BG_COLOR = Color.DARK_GRAY;
  private static final Stroke LINE_STROKE = new BasicStroke(56, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  private static final Color LINE_COLOR = Color.GREEN;

  protected Puzzle puzzle;

  private boolean mouseDown = false;
  private double colSize, rowSize;
  private int startRow = 0, startCol = 0, endRow = 0, endCol = 0;

  private PuzzlePanel(PanelConfig config, Puzzle puzzle) {
    super(config, new Empty());
    this.puzzle = puzzle;
  }

  public static PuzzlePanel create(Puzzle puzzle) {
    PanelConfig config = new PanelConfig();
    config.color = BG_COLOR;
    config.cornerRadius = 32;

    return new PuzzlePanel(config, puzzle);
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

    if (mouseDown) {
      Stroke originalStroke = g.getStroke();
      Color originalColor = g.getColor();

      g.setStroke(LINE_STROKE);
      g.setColor(LINE_COLOR);

      g.drawLine(
        (int) (absoluteBounds.x + startCol * colSize + 0.5 * colSize),
        (int) (absoluteBounds.y + startRow * rowSize + 0.5 * rowSize),
        (int) (absoluteBounds.x + endCol * colSize + 0.5 * colSize),
        (int) (absoluteBounds.y + endRow * rowSize + 0.5 * rowSize)
      );

      g.setColor(originalColor);
      g.setStroke(originalStroke);
    }
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
      case EXIT, RELEASE -> {
        mouseDown = false;
        startCol = startRow = endCol = endRow = 0;
        host.triggerRender();
      }
    }

    super.handleEvent(event);
  }
}
