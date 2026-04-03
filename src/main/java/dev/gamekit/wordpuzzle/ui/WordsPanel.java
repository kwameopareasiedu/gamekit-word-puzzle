package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.ui.widgets.Panel;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class WordsPanel extends Compose {
  private static final Color WORD_BG = new Color(0xDDB3A48E, true);
  private static final Color FOUND_WORD_BG = new Color(0xFFEDAF);

  private final String[] puzzleWords;
  private final List<String> foundWords;

  public WordsPanel(String[] puzzleWords, List<String> foundWords) {
    this.puzzleWords = puzzleWords;
    this.foundWords = foundWords;
  }

  public static WordsPanel create(String[] words, List<String> foundWords) {
    return new WordsPanel(words, foundWords);
  }

  @Override
  protected Widget build() {
    return Column.create(
      props -> {
        props.crossAxisAlignment = CrossAxisAlignment.STRETCH;
        props.gapSize = 24;
      },
      Text.create(
        props -> {
          props.text = "Find These Words";
          props.fontStyle = Text.BOLD;
          props.fontSize = 48;
        }
      ),
      Sized.create(
        props -> {
          props.fixedWidth = 384.0;
          props.fixedHeight = 256.0;
        },
        Grid.create(
          props -> {
            props.columnGapSize = 12;
            props.rowGapSize = 12;
          },
          Arrays.stream(puzzleWords).map(
            word -> {
              boolean found = foundWords.contains(word);

              return Panel.create(
                props -> {
                  props.color = !found ? WORD_BG : FOUND_WORD_BG;
                  props.cornerRadius = 16;
                },
                Padding.create(
                  12, 24,
                  Center.create(
                    Text.create(word)
                  )
                )
              );
            }
          ).toArray(Widget[]::new)
        )
      )
    );
  }
}
