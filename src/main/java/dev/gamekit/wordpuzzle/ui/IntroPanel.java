package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.utils.EngineImage;
import dev.gamekit.utils.VoidCallback;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class IntroPanel extends Compose {
  private static final EngineImage BG = IO.getImage("intro-bg.jpg");
  private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("E, MMMM dd");
  private static final Color WORD_PANEL_BG = new Color(0xDDB3A48E, true);

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
        props.background = BG;
        props.cornerRadius = 16;
      },
      Padding.create(
        48, 48, 48, 48,
        Column.create(
          props -> props.crossAxisAlignment = CrossAxisAlignment.STRETCH,
          Text.create(
            props -> {
              props.text = DATE_FMT.format(LocalDateTime.now());
              props.alignment = Alignment.CENTER;
            }
          ),
          Text.create(
            props -> {
              props.text = theme;
              props.alignment = Alignment.CENTER;
              props.fontStyle = Text.BOLD;
              props.fontSize = 48;
            }
          ),
          Gap.create(8, 8),
          Text.create(
            props -> {
              props.text = "Find 5 words hidden in the grid";
              props.fontStyle = Text.BOLD;
              props.fontSize = 20;
              props.alignment = Alignment.CENTER;
            }
          ),
          Gap.create(8, 48),
          Text.create(
            props -> {
              props.text = "WORDS TO FIND";
              props.fontStyle = Text.BOLD;
            }
          ),
          Sized.create(
            props -> {
              props.fixedWidth = 512.0;
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
                      Text.create(word)
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
            AudibleButton.create(
              props -> props.mouseListener = (ev) -> {
                if (ev.type == MouseEvent.Type.CLICK)
                  onStart.invoke();
              },
              Text.create(
                props -> {
                  props.text = "START";
                  props.fontSize = 36;
                  props.fontHeightRatio = 0.7;
                }
              )
            )
          )
        )
      )
    );
  }
}
