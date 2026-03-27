package dev.gamekit.wordpuzzle.ui;

import dev.gamekit.audio.AudioClip;
import dev.gamekit.audio.AudioClip2D;
import dev.gamekit.audio.AudioGroup;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Button;
import dev.gamekit.ui.widgets.ButtonConfig;
import dev.gamekit.ui.widgets.Widget;

public class AudibleButton extends Button {
  private static final AudioClip CLICKED_SFX;

  static {
    CLICKED_SFX = new AudioClip2D("button-clicked.wav", AudioGroup.EFFECTS, 1);
  }

  public AudibleButton(ButtonConfig config, Widget child) {
    super(config, child);
  }

  public static AudibleButton create(ButtonConfig.Updater updater, Widget child) {
    ButtonConfig config = new ButtonConfig();
    updater.update(config);
    return new AudibleButton(config, child);
  }

  @Override
  public void handleEvent(MouseEvent event) {
    if (event.type == MouseEvent.Type.CLICK)
      CLICKED_SFX.play();

    super.handleEvent(event);
  }
}
