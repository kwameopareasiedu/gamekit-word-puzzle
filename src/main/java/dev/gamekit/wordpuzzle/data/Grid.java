package dev.gamekit.wordpuzzle.data;

import java.util.ArrayList;
import java.util.List;

import static dev.gamekit.wordpuzzle.Utils.rowColToIndex;

/** Represents the puzzle grid containing a grid of {@link Cell cells} */
public class Grid {
  public final int rows;
  public final int cols;

  private final List<Cell> cells;

  public Grid(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;

    cells = new ArrayList<>();

    for (int r = 1; r <= rows; r++) {
      for (int c = 1; c <= cols; c++) {
        int index = rowColToIndex(rows, cols, r, c);
        cells.add(new Cell(r, c, index));
      }
    }
  }

  /** Returns a {@link Slot} starting from the specified row and col, direction and length */
  public Slot getSlot(int row, int col, Direction dir, int length) {
    Cell[] slotCells = new Cell[length];
    int currentRow = row, currentCol = col, currentLength = 0;

    while (currentLength < slotCells.length) {
      try {
        int index = rowColToIndex(rows, cols, currentRow, currentCol);
        slotCells[currentLength] = cells.get(index);

        currentRow += dir.row;
        currentCol += dir.col;
        currentLength++;
      } catch (Exception ignored) {
        return null;
      }
    }

    return new Slot(slotCells, dir, length);
  }
}
