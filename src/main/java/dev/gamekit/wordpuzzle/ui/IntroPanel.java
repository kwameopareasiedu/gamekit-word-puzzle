package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.ui.widgets.Button;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.utils.VoidCallback;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class IntroPanel extends Compose {
  private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("E, MMMM dd");

  private final String theme;
  private final String[] words;
  private final VoidCallback onStart;

  public IntroPanel(String theme, String[] words, VoidCallback onStart) {
    this.theme = theme;
    this.words = words;
    this.onStart = onStart;
  }

  public static IntroPanel create(String theme, String[] words, VoidCallback onStart) {
    return new IntroPanel(theme, words, onStart);
  }

  @Override
  protected Widget build() {
    return Panel.create(
      props -> {
        props.color = Color.DARK_GRAY;
        props.cornerRadius = 16;
      },
      Padding.create(
        48, 48, 48, 48,
        Column.create(
          props -> {
            props.crossAxisAlignment = CrossAxisAlignment.STRETCH;
          },
          Text.create(
            props -> {
              props.text = DATE_FMT.format(LocalDateTime.now());
              props.alignment = Alignment.CENTER;
            }
          ),
          Text.create(
            props -> {
              props.text = theme;
              props.fontStyle = Text.BOLD;
              props.fontSize = 48;
              props.alignment = Alignment.CENTER;
            }
          ),
          Gap.create(8, 8),
          Text.create(
            props -> {
              props.text = "Find 5 words hidden in the grid";
              props.fontStyle = Text.BOLD;
              props.fontSize = 16;
              props.alignment = Alignment.CENTER;
            }
          ),
          Gap.create(8, 64),
          Text.create(
            props -> {
              props.text = "WORDS TO FIND";
              props.fontStyle = Text.BOLD;
              props.fontSize = 24;
            }
          ),
          Sized.create(
            props -> {
              props.fixedWidth = 512.0;
              props.fixedHeight = 256.0;
            },
            Grid.create(
              Arrays.stream(words).map(
                word -> Panel.create(
                  props -> { },
                  Padding.create(
                    12, 24,
                    Text.create(
                      props -> {
                        props.text = word;
                        props.fontStyle = Text.BOLD;
                        props.fontSize = 24;
                      }
                    )
                  )
                )
              ).toArray(Widget[]::new)
            )
          ),
          Gap.create(8, 64),
          Sized.create(
            props -> {
              props.fixedWidth = 512.0;
              props.fixedHeight = 64.0;
            },
            Button.create(
              props -> {
                props.mouseListener = (ev) -> {
                  if (ev.type == MouseEvent.Type.CLICK)
                    onStart.run();
                };
              },
              Text.create("START")
            )
          )
        )
      )
    );
  }
}
