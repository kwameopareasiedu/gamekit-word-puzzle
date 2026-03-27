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

public class SolvedPanel extends Compose {
  private static final EngineImage LEAF = IO.getImage("leaf.png");
  private static final EngineImage BG = IO.getImage("context-bg.jpg");
  private static final Color TRANSLUCENT = new Color(0x99000000, true);

  private final VoidCallback onReplay;
  private final VoidCallback onExit;

  public SolvedPanel(VoidCallback onReplay, VoidCallback onExit) {
    this.onReplay = onReplay;
    this.onExit = onExit;
  }

  public static SolvedPanel create(VoidCallback onRetry, VoidCallback onExit) {
    return new SolvedPanel(onRetry, onExit);
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
                  props.fit = ImageFit.CROP;
                }
              )
            ),
            Padding.create(
              48, 48, 48, 48,
              Column.create(
                props -> {
                  props.crossAxisAlignment = CrossAxisAlignment.STRETCH;
                  props.gapSize = 24;
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
                      props.text = "Congratulations";
                      props.alignment = Alignment.CENTER;
                      props.fontHeightRatio = 0.7;
                      props.fontSize = 64;
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
                Text.create(
                  props -> {
                    props.text = "Awesome! You have completed today's word puzzle successfully!";
                    props.alignment = Alignment.CENTER;
                    props.fontSize = 32;
                  }
                ),
                Text.create(
                  props -> {
                    props.text = "Make sure to check back in tomorrow for new words.";
                    props.alignment = Alignment.CENTER;
                    props.fontSize = 32;
                  }
                ),
                Gap.create(0, 0),
                Sized.create(
                  props -> {
                    props.fractionalWidth = 1.0;
                    props.fixedHeight = 64.0;
                  },
                  AudibleButton.create(
                    props -> props.mouseListener = (ev) -> {
                      if (ev.type == MouseEvent.Type.CLICK)
                        onReplay.run();
                    },
                    Text.create(
                      props -> {
                        props.text = "PLAY AGAIN";
                        props.fontSize = 36;
                        props.fontHeightRatio = 0.7;
                      }
                    )
                  )
                ),
                Sized.create(
                  props -> {
                    props.fractionalWidth = 1.0;
                    props.fixedHeight = 64.0;
                  },
                  AudibleButton.create(
                    props -> props.mouseListener = (ev) -> {
                      if (ev.type == MouseEvent.Type.CLICK)
                        onExit.run();
                    },
                    Text.create(
                      props -> {
                        props.text = "EXIT";
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
