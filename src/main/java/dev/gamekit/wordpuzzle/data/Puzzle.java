package dev.gamekit.wordpuzzle.data;

import java.util.*;

public class Puzzle {
  public final String[] chars;
  public final int rows;
  public final int cols;

  private final char[][] cells;

  public Puzzle(int rows, int cols, String[] words) {
    this.rows = rows;
    this.cols = cols;

    // Get unique word length
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

    // Randomize the grid cells
    cells = new char[rows][cols];

    for (int r = 1; r <= rows; r++) {
      for (int c = 1; c <= cols; c++) {
        cells[r - 1][c - 1] = (char) (65 + rnd.nextInt(26));
      }
    }

    // For each unique word length, get the possible grid slots
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

    // Place the words on the grid by computing valid slots for each one
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

    // Create a 1-D array of the grid
    chars = new String[rows * cols];

    for (int r = 1; r <= rows; r++) {
      for (int c = 1; c <= cols; c++) {
        int row = r - 1, col = c - 1;
        int index = row * cols + col;

        chars[index] = String.valueOf(cells[row][col]);
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

  public Slot getSlot(int startRow, int startCol, int endRow, int endCol) {
    Direction[] directions = Direction.values();

    for (Direction dir : directions) {
      List<int[]> positions = new ArrayList<>();
      int row = startRow, col = startCol;
      boolean endCellReached = false;

      while (true) {
        boolean posIsValid = 1 <= row && row <= rows && 1 <= col && col <= cols;
        if (!posIsValid) break;

        positions.add(new int[]{ row, col });

        if (row == endRow && col == endCol) {
          endCellReached = true;
          break;
        }

        row += dir.row;
        col += dir.col;
      }

      if (endCellReached) {
        int[][] positionArray = new int[positions.size()][];
        positionArray = positions.toArray(positionArray);

        return new Slot(positionArray, dir, positionArray.length);
      }
    }


    return null;
  }

  /** Prints the cells of the grid */
  private void printGrid() {
    for (int r = 1; r <= rows; r++) {
      for (int c = 1; c <= cols; c++) {
        System.out.printf("%-3c", cells[r - 1][c - 1]);
      }

      System.out.println();
    }
  }

  /** Prints the cells of the grid contained in one or more of the provided word slots */
  private void printGrid(List<Slot> wordSlots) {
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
