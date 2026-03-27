package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.ImageFit;
import dev.gamekit.ui.enums.MainAxisAlignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.utils.EngineImage;
import dev.gamekit.utils.VoidCallback;

public class ErrorPanel extends Compose {
  private static final EngineImage LEAF = IO.getImage("leaf.png");
  private static final EngineImage BG = IO.getImage("context-bg.jpg");

  private final VoidCallback onRetry;

  public ErrorPanel(VoidCallback onRetry) {
    this.onRetry = onRetry;
  }

  public static ErrorPanel create(VoidCallback onRetry) {
    return new ErrorPanel(onRetry);
  }

  @Override
  protected Widget build() {
    return Stack.create(
      Sized.create(
        props -> props.fractionalWidth = 1.0,
        Image.create(
          props -> {
            props.image = BG;
            props.fit = ImageFit.CROP;
          }
        )
      ),
      Padding.create(
        24, 48, 24, 48,
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
                props.text = "Initialization Failed";
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
          Text.create(
            props -> {
              props.text = "We could not reach the internet to retrieve today's words. Please make sure you have a " +
                "working connection and try again";
              props.alignment = Alignment.CENTER;
              props.fontSize = 28;
            }
          ),
          Gap.create(0, 0),
          Sized.create(
            props -> {
              props.fractionalWidth = 1.0;
              props.fixedHeight = 64.0;
            },
            AudibleButton.create(
              props -> {
                props.mouseListener = (ev) -> {
                  if (ev.type == MouseEvent.Type.CLICK)
                    onRetry.run();
                };
              },
              Text.create(
                props -> {
                  props.text = "RETRY";
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
