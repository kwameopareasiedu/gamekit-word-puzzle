package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.ImageFit;
import dev.gamekit.ui.enums.MainAxisAlignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.utils.EngineImage;
import dev.gamekit.utils.VoidCallback;

import java.awt.*;
import java.util.Arrays;

public class AboutPanel extends Compose {
  private static final EngineImage LEAF = IO.getImage("leaf.png");
  private static final EngineImage BG = IO.getImage("context-bg.jpg");
  private static final Color TRANSLUCENT = new Color(0x99000000, true);
  private static final String [][] INFO = new String[][]{
    new String[]{ "Creator", "Kwame Opare Asiedu" },
    new String[]{ "Game Engine", "GameKit (0.7.0)" },
    new String[]{ "Inspiration", "dailynewspuzzle.com (Organized Khaos)" },
    new String[]{ "Assets", "Images (unsplash.com), Music (freetouse.com)" },
  };

  private final VoidCallback onClose;

  public AboutPanel(VoidCallback onClose) {
    this.onClose = onClose;
  }

  public static AboutPanel create(VoidCallback onExit) {
    return new AboutPanel(onExit);
  }

  @Override
  protected Widget build() {
    return Panel.create(
      props -> props.color = TRANSLUCENT,
      Center.create(
        Sized.create(
          props -> props.fractionalWidth = 0.4,
          Stack.create(
            Sized.create(
              props -> {
                props.fractionalWidth = 1.0;
                props.fractionalHeight = 1.0;
              },
              Image.create(
                props -> {
                  props.image = BG;
                  props.fit = ImageFit.STRETCH;
                }
              )
            ),
            Padding.create(
              48, 48, 48, 48,
              Column.create(
                props -> {
                  props.crossAxisAlignment = CrossAxisAlignment.STRETCH;
                  props.gapSize = 6;
                },
                Row.create(
                  props -> props.mainAxisAlignment = MainAxisAlignment.CENTER,
                  Sized.create(
                    props -> {
                      props.fixedWidth = 24.0;
                      props.fixedHeight = 24.0;
                    },
                    Image.create(LEAF)
                  ),
                  Text.create(
                    props -> {
                      props.text = "Cozy Word Puzzle";
                      props.alignment = Alignment.CENTER;
                      props.fontHeightRatio = 0.7;
                      props.fontSize = 48;
                    }
                  ),
                  Sized.create(
                    props -> {
                      props.fixedWidth = 24.0;
                      props.fixedHeight = 24.0;
                    },
                    Image.create(LEAF)
                  )
                ),
                Gap.create(32, 32),
                Column.create(
                  props -> props.gapSize = 24,
                  Arrays.stream(INFO).map(item -> {
                    String label = item[0];
                    String value = item[1];

                    return Column.create(
                      Text.create(
                        props -> {
                          props.text = label;
                          props.color = Color.GRAY;
                        }
                      ),
                      Text.create(
                        props -> {
                          props.text = value;
                          props.fontSize = 30;
                        }
                      )
                    );
                  }).toArray(Widget[]::new)
                ),
                Gap.create(32, 32),
                Sized.create(
                  props -> {
                    props.fractionalWidth = 1.0;
                    props.fixedHeight = 64.0;
                  },
                  AudibleButton.create(
                    props -> props.mouseListener = (ev) -> {
                      if (ev.type == MouseEvent.Type.CLICK)
                        onClose.invoke();
                    },
                    Text.create(
                      props -> {
                        props.text = "BACK";
                        props.fontSize = 36;
                        props.fontHeightRatio = 0.7;
                      }
                    )
                  )
                )
              )
            )
          )
        )
      )
    );
  }
}
