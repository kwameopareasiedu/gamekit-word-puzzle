package dev.gamekit.wordpuzzle.data;

import java.util.*;

public class Puzzle {
  private final int rows;
  private final int cols;
  private final char[][] cells;

  public Puzzle(int rows, int cols, String[] words) {
    this.rows = rows;
    this.cols = cols;

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

    Random rnd = new Random();

    cells = new char[rows][cols];

    for (int r = 1; r <= rows; r++) {
      for (int c = 1; c <= cols; c++) {
        cells[r - 1][c - 1] = (char) (65 + rnd.nextInt(26));
      }
    }

    Map<Integer, List<Slot>> lengthSlotMap = new HashMap<>();

    for (int len : wordLengths) {
      List<Slot> slots = new ArrayList<>();

      for (int r = 1; r <= rows; r++) {
        for (int c = 1; c <= cols; c++) {
          for (var dir : Direction.READABLE) {
            Slot slot = getSlot(r, c, dir, len);

            if (slot != null) slots.add(slot);
          }
        }
      }

      lengthSlotMap.put(len, slots);
    }

    Map<String, Slot> wordSlotMap = new HashMap<>();

    for (String word : words) {
      List<Slot> possibleSlots = lengthSlotMap.get(word.length());

      while (true) {
        int slotIndex = rnd.nextInt(possibleSlots.size());
        Slot selectedSlot = possibleSlots.get(slotIndex);
        if (selectedSlot == null) continue;

        boolean slotIsValid = true;

        for (Map.Entry<String, Slot> pair : wordSlotMap.entrySet()) {
          int[] intersection = selectedSlot.intersectsWith(pair.getValue());

          if (intersection != null) {
            // TODO: Check if word letters at intersection indices are the same
            char wordCharAtIntersection = word.charAt(intersection[0]);
            char otherWordCharAtIntersection = pair.getKey().charAt(intersection[1]);

            if (wordCharAtIntersection != otherWordCharAtIntersection) {
              slotIsValid = false;
              break;
            }
          }
        }

        if (slotIsValid) {
          possibleSlots.remove(selectedSlot);
          wordSlotMap.put(word, selectedSlot);
          System.out.printf("%s: %s\n", word, selectedSlot);

          int[][] slotPositions = selectedSlot.positions();

          for (int i = 0; i < slotPositions.length; i++) {
            int[] pos = slotPositions[i];
            int row = pos[0] - 1;
            int col = pos[1] - 1;

            cells[row][col] = word.toUpperCase().charAt(i);
          }

          break;
        }
      }
    }

    printGrid(wordSlotMap.values().stream().toList());
  }

  private Slot getSlot(int startRow, int startCol, Direction dir, int length) {
    int[][] positions = new int[length][2];
    int row = startRow, col = startCol, len = 0;

    while (len < positions.length) {
      try {
        boolean cellIsValid = 1 <= row && row <= rows && 1 <= col && col <= cols;

        if (!cellIsValid) return null;

        positions[len++] = new int[]{ row, col };

        row += dir.row;
        col += dir.col;
      } catch (Exception ignored) {
        return null;
      }
    }

    return new Slot(positions, dir, length);
  }

  private void printGrid() {
    for (int r = 1; r <= rows; r++) {
      for (int c = 1; c <= cols; c++) {
        System.out.printf("%-3c", cells[r - 1][c - 1]);
      }

      System.out.println();
    }
  }

  private void printGrid(List<Slot> wordSlots) {
    System.out.println(wordSlots.get(0).positions().length);

    for (int r = 1; r <= rows; r++) {
      for (int c = 1; c <= cols; c++) {
        char character = '-';

        for (Slot slot : wordSlots) {
          for (int[] pos : slot.positions()) {
            if (pos[0] == r && pos[1] == c) {
              character = cells[r - 1][c - 1];
              break;
            }
          }
        }

        System.out.printf("%-3c", character);
      }

      System.out.println();
    }
  }
}
