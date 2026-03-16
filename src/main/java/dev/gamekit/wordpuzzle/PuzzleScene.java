package dev.gamekit.wordpuzzle;

import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.wordpuzzle.data.Puzzle;
import dev.gamekit.wordpuzzle.data.Slot;
import dev.gamekit.wordpuzzle.ui.GestureDetector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PuzzleScene extends Scene {
  private static final String[] WORDS = new String[]{ "OSCARS", "AYRA", "OMAH", "OMOTOLA", "AFROBEATS" };

  private final Puzzle puzzle;
  private final java.util.List<String> foundWords;
  private final java.util.List<Slot> validSlots;

  public PuzzleScene() {
    super("Puzzle Scene");

    puzzle = new Puzzle(12, 12, WORDS);
    foundWords = new ArrayList<>();
    validSlots = new ArrayList<>();
  }

  @Override
  protected void render() {
    Renderer.clear(Color.BLACK);
  }

  @Override
  protected Widget createUI() {
    return Stack.create(
      Align.create(
        props -> {
          props.horizontalAlignment = Alignment.START;
          props.verticalAlignment = Alignment.CENTER;
        },
        Padding.create(
          96,
          Column.create(
            props -> props.crossAxisAlignment = CrossAxisAlignment.CENTER, Text.create(
              props -> {
                props.text = "Find These Words";
                props.fontSize = 32;
              }
            ),
            Gap.create(16, 16),
            Column.create(
              props -> props.crossAxisAlignment = CrossAxisAlignment.CENTER,
              Arrays.stream(WORDS).map(
                word -> Panel.create(
                  props -> {
                    props.cornerRadius = 32;
                    props.color = foundWords.contains(word) ? Color.GREEN : Color.ORANGE;
                  },
                  Padding.create(
                    20, 28,
                    Text.create(
                      props -> {
                        props.text = word;
                        props.color = Color.BLACK;
                        props.fontStyle = Text.BOLD;
                        props.fontSize = 24;
                      }
                    )
                  )
                )
              ).toArray(Widget[]::new)
            )
          )
        )
      ),
      Center.create(
        Sized.create(
          props -> {
            props.fixedWidth = 768.0;
            props.fixedHeight = 768.0;
          },
          GestureDetector.create(
            (GestureDetector.GestureDetectorConfig.Updater) props -> {
              props.puzzle = puzzle;
              props.validSlots = validSlots;
              props.onSlotMarked = this::onSlotMarked;
            },
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
      )
    );
  }

  void onSlotMarked(Slot slot) {
    String slotWord = puzzle.getSlotWord(slot);

    if (Arrays.asList(WORDS).contains(slotWord) && !foundWords.contains(slotWord)) {
      System.out.println(slotWord);
      foundWords.add(slotWord);
      validSlots.add(slot);
      updateUI();
    }
  }
}
