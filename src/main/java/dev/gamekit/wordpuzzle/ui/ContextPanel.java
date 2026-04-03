package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.ImageFit;
import dev.gamekit.ui.enums.MainAxisAlignment;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.utils.EngineImage;

public class ContextPanel extends Compose {
  private static final EngineImage LEAF = IO.getImage("leaf.png");
  private static final EngineImage BG = IO.getImage("context-bg.jpg");

  private final String word;
  private final String context;

  public ContextPanel(String word, String context) {
    this.word = word;
    this.context = context;
  }

  public static ContextPanel create(String word, String context) {
    return new ContextPanel(word, context);
  }

  @Override
  protected Widget build() {
    return Stack.create(
      Image.create(
        props -> {
          props.image = BG;
          props.fit = ImageFit.CROP;
        }
      ),
      Padding.create(
        24, 24, 24, 24,
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
                props.text = word;
                props.alignment = Alignment.CENTER;
                props.fontHeightRatio = 0.7;
                props.fontSize = 32;
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
              props.text = context;
              props.alignment = Alignment.CENTER;
              props.fontSize = 28;
            }
          )
        )
      )
    );
  }
}
