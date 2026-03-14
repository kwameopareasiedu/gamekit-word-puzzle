package dev.gamekit.wordpuzzle.data;

/** Represents a row and column pair in a grid */
public record Cell(int row, int col, int index) {
  @Override
  public String toString() {
    return String.format("Cell(R:%d,C:%d)", row, col);
  }
}
