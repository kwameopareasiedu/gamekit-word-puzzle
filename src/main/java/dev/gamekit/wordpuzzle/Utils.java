package dev.gamekit.wordpuzzle;

public class Utils {
  private Utils() { }

  public static int rowColToIndex(int rows, int cols, int row, int col) {
    if (1 <= row && row <= rows && 1 <= col && col <= cols)
      return (row - 1) * cols + (col - 1);

    throw new IllegalArgumentException("Invalid row or col");
  }
}
