package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.ImageFit;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.utils.EngineImage;

import java.awt.*;

public class RootStack extends Compose {
  private static final EngineImage BG = IO.getImage("puzzle-bg.jpg");
  private static final Font BASE_FONT = IO.getFont("howdybun.ttf");
  private static final EngineImage DEFAULT_BUTTON_BG = IO.getImageSliceWithInsets(
    "button-variants.png", 11, 107, 26, 28, 4, 4, 7, 4
  );
  private static final EngineImage HOVER_BUTTON_BG = IO.getImageSliceWithInsets(
    "button-variants.png", 11, 59, 26, 28, 4, 4, 7, 4
  );
  private static final EngineImage PRESSED_BUTTON_BG = IO.getImageSliceWithInsets(
    "button-variants.png", 59, 107, 26, 28, 4, 4, 7, 4
  );

  private final Widget child;

  public RootStack(Widget child) {
    this.child = child;
  }

  public static RootStack create(Widget child) {
    return new RootStack(child);
  }

  @Override
  protected Widget build() {
    return Theme.create(
      props -> {
        props.textFont = BASE_FONT;
        props.textColor = Color.DARK_GRAY;
        props.textFontSize = 24;
        props.buttonDefaultBackground = DEFAULT_BUTTON_BG;
        props.buttonHoverBackground = HOVER_BUTTON_BG;
        props.buttonPressedBackground = PRESSED_BUTTON_BG;
      },
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
        child,
        Align.create(
          props -> {
            props.horizontalAlignment = Alignment.CENTER;
            props.verticalAlignment = Alignment.END;
          },
          Padding.create(
            24,
            Panel.create(
              props -> {
                props.color = Color.WHITE;
                props.cornerRadius = 24;
              },
              Padding.create(
                8, 16, 16, 16,
                Text.create(
                  props -> {
                    props.text = "DNP Clone, Created by Kwame Opare Asiedu";
                    props.fontSize = 20;
                  }
                )
              )
            )
          )
        )
      )
    );
  }
}
