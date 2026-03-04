package dev.gamekit.wordpuzzle;

import dev.gamekit.core.Application;
import dev.gamekit.settings.*;

public class Launcher extends Application {
  public Launcher() {
    super(
      new Settings(
        "Word Puzzle",
        Resolution.HD,
        Antialiasing.ON,
        TextAntialiasing.ON,
        RenderingStrategy.QUALITY
      )
    );
  }

  public static void main(String[] args) {
    Launcher launcher = new Launcher();
    launcher.run();
  }
}
