package dev.gamekit.wordpuzzle;

import dev.gamekit.wordpuzzle.data.Puzzle;
import dev.gamekit.wordpuzzle.data.Slot;

public class TestPad {
  public static void main(String[] args) {
    Puzzle p = new Puzzle(12, 12, new String[]{ "birdman", "mugagbe", "shattashatta", "eoco" });

    Slot s = p.getSlot(1, 1, 5, 1);

    System.out.println(s != null ? s : "n/A");
  }
}
