package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.ImageFit;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.utils.EngineImage;
import dev.gamekit.utils.VoidCallback;

import java.awt.*;

public class ExitPanel extends Compose {
  private static final EngineImage LEAF = IO.getImage("leaf.png");
  private static final EngineImage BG = IO.getImage("context-bg.jpg");
  private static final Color TRANSLUCENT = new Color(0x99000000, true);

  private final VoidCallback onExit;
  private final VoidCallback onBack;

  public ExitPanel(VoidCallback onExit, VoidCallback onBack) {
    this.onExit = onExit;
    this.onBack = onBack;
  }

  public static ExitPanel create(VoidCallback onExit, VoidCallback onBack) {
    return new ExitPanel(onExit, onBack);
  }

  @Override
  protected Widget build() {
    return Panel.create(
      props -> props.color = TRANSLUCENT,
      Center.create(
        Sized.create(
          props -> props.fractionalWidth = 0.25,
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
                Text.create(
                  props -> {
                    props.text = "Are you sure you want to quit?";
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
                        onExit.invoke();
                    },
                    Text.create(
                      props -> {
                        props.text = "Yes";
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
                        onBack.invoke();
                    },
                    Text.create(
                      props -> {
                        props.text = "No";
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
