package dev.gamekit.wordpuzzle.data;

/** Represent a cardinal direction */
public enum Direction {
  NORTH(-1, 0),
  NORTH_EAST(-1, 1),
  EAST(0, 1),
  SOUTH_EAST(1, 1),
  SOUTH(1, 0),
  SOUTH_WEST(1, -1),
  WEST(0, -1),
  NORTH_WEST(-1, -1);

  public static final Direction[] READABLE = new Direction[]{
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
  };

  public final int row;
  public final int col;

  Direction(int row, int col) {
    this.row = row;
    this.col = col;
  }

}
