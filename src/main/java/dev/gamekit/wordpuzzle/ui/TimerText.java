package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.widgets.*;

import java.awt.*;

public class TimerText extends Compose {
  private static final Font CLOCK_FONT = IO.getFont("howdybun.ttf");

  private final String formattedTimer;

  public TimerText(long timerMs) {
    long timerSecs = timerMs / 1000;
    long secs = timerSecs % 60;
    long mins = timerSecs / 60;

    formattedTimer = String.format("%02d:%02d", mins, secs);
  }

  public static TimerText create(long timerMs) {
    return new TimerText(timerMs);
  }

  @Override
  protected Widget build() {
    return Stack.create(
//      Opacity.create(
//        0.25,
//        Text.create(
//          props -> {
//            props.text = "00:00";
//            props.font = CLOCK_FONT;
//            props.fontSize = 96;
//          }
//        )
//      ),
      Text.create(
        props -> {
          props.text = formattedTimer;
          props.font = CLOCK_FONT;
          props.fontSize = 96;
        }
      )
    );
  }
}
