package dev.gamekit.wordpuzzle;

import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.wordpuzzle.data.Puzzle;

import java.awt.*;
import java.util.Arrays;

public class PuzzleScene extends Scene {
  private final Puzzle puzzle;

  public PuzzleScene() {
    super("Puzzle Scene");

    puzzle = new Puzzle(12, 12, new String[]{ "OSCARS", "AYRA", "OMAH", "OMOTOLA", "AFROBEATS" });
  }

  @Override
  protected void render() {
    Renderer.clear(Color.BLACK);
  }

  @Override
  protected Widget createUI() {
    return Center.create(
      Sized.create(
        props -> {
          props.fixedWidth = 768.0;
          props.fixedHeight = 768.0;
        },
        Stack.create(
          Sized.create(
            props -> {
              props.fractionalWidth = 1.0;
              props.fractionalHeight = 1.0;
            },
            PuzzlePanel.create(puzzle)
          ),
          Grid.create(
            props -> {
              props.columnCount = puzzle.cols;
              props.columnGapSize = props.rowGapSize = 0;
            },
            Arrays.stream(puzzle.chars).map(
              (ch) -> Center.create(
                Text.create(
                  props -> {
                    props.text = ch;
                    props.fontStyle = Text.BOLD;
                    props.fontSize = 36;
                    props.fontHeightRatio = 0.8;
                  }
                )
              )
            ).toArray(Widget[]::new)
          )
        )
      )
    );
  }
}
