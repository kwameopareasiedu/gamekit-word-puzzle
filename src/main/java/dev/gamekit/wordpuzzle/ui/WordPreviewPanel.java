package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.ui.widgets.Panel;

import java.awt.*;
import java.util.Arrays;

public class WordPreviewPanel extends Compose {
  private static final Color WORD_PANEL_BG = new Color(0xDDB3A48E, true);

  private final String[] words;

  public WordPreviewPanel(String[] words) {
    this.words = words;
  }

  public static WordPreviewPanel create(String[] words) {
    return new WordPreviewPanel(words);
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
          Arrays.stream(words).map(
            word -> Panel.create(
              props -> {
                props.color = WORD_PANEL_BG;
                props.cornerRadius = 16;
              },
              Padding.create(
                12, 24,
                Center.create(
                  Text.create(
                    props -> {
                      props.text = word;
                    }
                  )
                )
              )
            )
          ).toArray(Widget[]::new)
        )
      )
    );
  }
}
