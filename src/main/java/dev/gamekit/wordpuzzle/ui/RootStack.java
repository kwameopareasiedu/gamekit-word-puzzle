package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.core.Application;
import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.ImageFit;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.utils.EngineImage;

import java.awt.*;

public class RootStack extends Stateful {
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
    super("RootStack");
    this.child = child;
  }

  public static RootStack create(Widget child) {
    return new RootStack(child);
  }

  @Override
  protected RootStackState createState() {
    return new RootStackState();
  }

  protected static class RootStackState extends State<RootStack> {
    private boolean showAbout = false;
    private boolean showExit = false;

    @Override
    protected Widget build(RootStack widget) {
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
          widget.child,
          Align.create(
            props -> {
              props.horizontalAlignment = Alignment.CENTER;
              props.verticalAlignment = Alignment.END;
            },
            Padding.create(
              24,
              Row.create(
                props -> props.gapSize = 24,
                Sized.create(
                  props -> {
                    props.useIntrinsicWidth = true;
                    props.useIntrinsicHeight = true;
                  },
                  AudibleButton.create(
                    props -> {
                      props.mouseListener = (ev) -> {
                        if (ev.type == MouseEvent.Type.CLICK) {
                          showAbout = !showAbout;
                          widget.host.triggerUpdate();
                        }
                      };
                    },
                    Padding.create(
                      12, 18,
                      Text.create(
                        props -> {
                          props.text = "About";
                          props.fontHeightRatio = 0.7;
                        }
                      )
                    )
                  )
                ),
                Sized.create(
                  props -> {
                    props.useIntrinsicWidth = true;
                    props.useIntrinsicHeight = true;
                  },
                  AudibleButton.create(
                    props -> {
                      props.mouseListener = (ev) -> {
                        if (ev.type == MouseEvent.Type.CLICK) {
                          showExit = !showExit;
                          widget.host.triggerUpdate();
                        }
                      };
                    },
                    Padding.create(
                      12, 18,
                      Text.create(
                        props -> {
                          props.text = "Quit";
                          props.fontHeightRatio = 0.7;
                        }
                      )
                    )
                  )
                )
              )
            )
          ),
          Builder.create(
            () -> {
              if (showExit) {
                return ExitPanel.create(
                  () -> Application.getInstance().quit(),
                  () -> {
                    showExit = false;
                    widget.host.triggerUpdate();
                  }
                );
              } else if (showAbout) {
                return AboutPanel.create(
                  () -> {
                    showAbout = false;
                    widget.host.triggerUpdate();
                  }
                );
              }

              return Empty.create();
            }
          )
        )
      );
    }
  }
}
