package dev.gamekit.wordpuzzle.data;

import java.util.*;

public class Puzzle {
  private final Grid grid;
  private final String[] words;
  private final Map<Integer, List<Slot>> slots;

  public Puzzle(int cols, int rows, String[] words) {
    this.words = words;

    grid = new Grid(rows, cols);

    final Set<Integer> wordLengths = new HashSet<>();
    final int maxWordLength = Math.max(rows, cols);

    for (String word : words) {
      int len = word.length();

      if (len > maxWordLength) {
        throw new IllegalArgumentException(
          String.format("%s exceeds max length of %d", word, maxWordLength)
        );
      }

      wordLengths.add(word.length());
    }

    slots = new HashMap<>();

    Direction[] playableDirections = new Direction[]{
      Direction.NORTH_EAST,
      Direction.EAST,
      Direction.SOUTH_EAST,
      Direction.SOUTH,
    };

    for (int len : wordLengths) {
      List<Slot> lengthSlots = new ArrayList<>();

      for (int r = 1; r <= rows; r++) {
        for (int c = 1; c <= cols; c++) {
          for (var dir : playableDirections) {
            Slot slot = grid.getSlot(r, c, dir, len);

            if (slot != null) {
              lengthSlots.add(slot);
            }
          }
        }
      }

      slots.put(len, lengthSlots);
      System.out.printf("%d: %d\n", len, lengthSlots.size());
    }


  }

  public Grid getGrid() {
    return grid;
  }
}
