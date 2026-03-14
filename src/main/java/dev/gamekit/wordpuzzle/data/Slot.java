package dev.gamekit.wordpuzzle.data;

/** Represents a list of {@link Cell cells} in a specified {@link Direction} */
public record Slot(Cell[] cells, Direction dir, int length) {
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("Slot(");
    builder.append("Cells(");

    for (int i = 0; i < cells.length; i++) {
      Cell cell = cells[i];
      builder.append(cell);

      if (i != cells.length - 1)
        builder.append(",");
    }

    builder.append("),");
    builder.append(String.format("Dir(%s),", dir.name()));
    builder.append(String.format("Length(%d)", length));
    builder.append(")");

    return builder.toString();
  }
}
