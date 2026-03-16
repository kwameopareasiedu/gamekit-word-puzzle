package dev.gamekit.wordpuzzle.data;

/** Represents a list of grid positions in a specified {@link Direction} */
public record Slot(int[][] positions, Direction dir, int length) {
  /**
   * Checks if this slot intersects with another slot and
   * returns an integer array containing the indices of
   * the intersecting positions
   */
  public int[] intersectsWith(Slot otherSlot) {
    for (int idx = 0; idx < positions.length; idx++) {
      int[] pa = positions[idx];

      for (int otherIdx = 0; otherIdx < otherSlot.positions.length; otherIdx++) {
        int[] pb = otherSlot.positions[otherIdx];

        if (pa[0] == pb[0] && pa[1] == pb[1])
          return new int[]{ idx, otherIdx };
      }
    }

    return null;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("Slot(");
    builder.append(String.format("Dir(%s),", dir.name()));
    builder.append(String.format("Length(%d),", length));

    builder.append("Cells(");
    for (int i = 0; i < positions.length; i++) {
      int[] rowCol = positions[i];
      builder.append("Cell(").append("C:").append(rowCol[1]).append(",");
      builder.append("R:").append(rowCol[0]).append(")");

      if (i != positions.length - 1)
        builder.append(",");
    }

    builder.append(")");
    builder.append(")");

    return builder.toString();
  }
}
