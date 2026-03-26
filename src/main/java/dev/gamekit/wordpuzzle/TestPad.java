package dev.gamekit.wordpuzzle;

import dev.gamekit.wordpuzzle.data.Puzzle;
import dev.gamekit.wordpuzzle.data.Slot;
import dev.kwameopareasiedu.simpson.Simpson;
import dev.kwameopareasiedu.simpson.nodes.StringNode;

public class TestPad {
  public static void main(String[] args) {
    StringNode parsedObject = (StringNode) Simpson.parse("\"Hello World\"");

    System.out.println(parsedObject);
    Puzzle p = new Puzzle(12, 12, new String[]{ "birdman", "mugagbe", "shattashatta", "eoco" });

    Slot s = p.getSlot(1, 1, 5, 1);

    System.out.println(s != null ? s : "n/A");
  }
}
