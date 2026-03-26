package dev.gamekit.wordpuzzle;

import dev.gamekit.core.Application;
import dev.gamekit.core.IO;
import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.wordpuzzle.data.Puzzle;
import dev.gamekit.wordpuzzle.data.Slot;
import dev.gamekit.wordpuzzle.ui.GestureDetector;
import dev.gamekit.wordpuzzle.ui.GestureDetectorConfig;
import dev.gamekit.wordpuzzle.ui.IntroPanel;
import dev.kwameopareasiedu.simpson.Simpson;
import dev.kwameopareasiedu.simpson.nodes.ArrayNode;
import dev.kwameopareasiedu.simpson.nodes.Node;
import dev.kwameopareasiedu.simpson.nodes.ObjectNode;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PuzzleScene extends Scene {
  private static final String WORDS_API_URL = "https://www.dailynewspuzzle.com/daily_puzzle.json";
  private static final Font CLOCK_FONT = IO.getFont("digital-7-mono.ttf");

  private final List<String> foundWords;
  private final List<Slot> validSlots;
  private GameState gameState = GameState.INIT;
  private String[] puzzleWordContexts;
  private String[] puzzleWords;
  private String puzzleTheme;
  private Puzzle puzzle;
  private long timerMs = 0;

  public PuzzleScene() {
    super("Puzzle Scene");

    foundWords = new ArrayList<>();
    validSlots = new ArrayList<>();
  }

  @Override
  protected void start() {
    gameState = GameState.FETCHING_DATA;

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest req = HttpRequest.newBuilder().uri(URI.create(WORDS_API_URL)).build();
    client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
      .thenApply(stringHttpResponse -> {
        ObjectNode resData = (ObjectNode) Simpson.parse(stringHttpResponse.body());
        ArrayNode wordsData = resData.getArrayNode("puzzle_words");

        puzzleWordContexts = new String[wordsData.length()];
        puzzleWords = new String[wordsData.length()];

        int index = 0;

        for (Node<?> node : wordsData) {
          ObjectNode nodeData = (ObjectNode) node;
          puzzleWords[index] = nodeData.getStringNode("word").get().toUpperCase();
          puzzleWordContexts[index++] = nodeData.getStringNode("context").get();
        }

        puzzleTheme = resData.getStringNode("theme").get();

        logger.debug("Game words available");

        puzzle = new Puzzle(12, 12, puzzleWords);
        gameState = GameState.FETCHED_DATA;
        updateUI();

        return "";
      }).exceptionally((ex) -> {
        logger.error("Failed to get words data", ex);
        gameState = GameState.FETCH_FAILED;
        updateUI();

        return "";
      });
  }

  @Override
  protected void update() {
    if (gameState == GameState.STARTED) {
      if (foundWords.size() < puzzleWords.length) {
        long newTimerMs = timerMs + Application.FRAME_INTERVAL_MS;

        if (newTimerMs / 1000 > timerMs / 1000)
          updateUI();

        timerMs = newTimerMs;
      }
    }
  }

  @Override
  protected void render() {
    Renderer.clear(Color.BLACK);
  }

  @Override
  protected Widget createUI() {
    if (gameState == GameState.INIT)
      return Empty.create();

    if (gameState == GameState.FETCHING_DATA)
      return Center.create(
        Column.create(
          props -> props.crossAxisAlignment = CrossAxisAlignment.CENTER,
          Sized.create(
            props -> {
              props.fixedWidth = 64.0;
              props.fixedHeight = 64.0;
            },
            Spinner.create()
          ),
          Text.create("Please wait...")
        )
      );

    if (gameState == GameState.FETCHED_DATA)
      return Center.create(
        IntroPanel.create(puzzleTheme, puzzleWords, () -> {
          gameState = GameState.STARTED;
          updateUI();
        })
      );

    if (gameState != GameState.STARTED)
      return Empty.create();

    return Stack.create(
      Align.create(
        props -> {
          props.horizontalAlignment = Alignment.START;
          props.verticalAlignment = Alignment.CENTER;
        },
        Padding.create(
          96,
          Column.create(
            props -> props.crossAxisAlignment = CrossAxisAlignment.CENTER,
            Text.create(
              props -> {
                props.text = "Find These Words";
                props.fontSize = 32;
              }
            ),
            Gap.create(16, 16),
            Column.create(
              props -> props.crossAxisAlignment = CrossAxisAlignment.CENTER,
              Arrays.stream(puzzleWords).map(
                word -> Panel.create(
                  props -> {
                    props.cornerRadius = 32;
                    props.color = foundWords.contains(word) ? Color.GREEN : Color.ORANGE;
                  },
                  Padding.create(
                    20, 28,
                    Text.create(
                      props -> {
                        props.text = word;
                        props.color = Color.BLACK;
                        props.fontStyle = Text.BOLD;
                        props.fontSize = 24;
                      }
                    )
                  )
                )
              ).toArray(Widget[]::new)
            )
          )
        )
      ),
      Align.create(
        props -> {
          props.horizontalAlignment = Alignment.CENTER;
          props.verticalAlignment = Alignment.START;
        },
        Padding.create(
          48,
          Stack.create(
            Opacity.create(
              0.25,
              Text.create(
                props -> {
                  props.text = "00:00";
                  props.font = CLOCK_FONT;
                  props.fontSize = 96;
                }
              )
            ),
            Text.create(
              props -> {
                props.text = getFormattedTimer();
                props.font = CLOCK_FONT;
                props.fontSize = 96;
              }
            )
          )
        )
      ),
      Padding.create(
        128, 0, 0, 0,
        Center.create(
          Sized.create(
            props -> {
              props.fixedWidth = 768.0;
              props.fixedHeight = 768.0;
            },
            GestureDetector.create(
              (GestureDetectorConfig.Updater) props -> {
                props.puzzle = puzzle;
                props.validSlots = validSlots;
                props.onSlotMarked = this::onSlotMarked;
              },
              Grid.create(
                props -> {
                  props.columnCount = puzzle.cols;
                  props.columnGapSize = props.rowGapSize = 0;
                },
                Arrays.stream(puzzle.chars).map(
                  (ch) -> Center.create(
                    Text.create(
                      props -> {
                        props.text = ch;
                        props.fontStyle = Text.BOLD;
                        props.fontSize = 36;
                        props.fontHeightRatio = 0.8;
                      }
                    )
                  )
                ).toArray(Widget[]::new)
              )
            )
          )
        )
      )
    );
  }

  private void onSlotMarked(Slot slot) {
    String slotWord = puzzle.getSlotWord(slot);

    if (Arrays.asList(puzzleWords).contains(slotWord) && !foundWords.contains(slotWord)) {
      System.out.println(slotWord);
      foundWords.add(slotWord);
      validSlots.add(slot);
      updateUI();
    }
  }

  private String getFormattedTimer() {
    long timerSecs = timerMs / 1000;
    long secs = timerSecs % 60;
    long mins = timerSecs / 60;
    return String.format("%02d:%02d", mins, secs);
  }

  private enum GameState {
    INIT, FETCHING_DATA, FETCHED_DATA, FETCH_FAILED, STARTED, COMPLETED
  }
}
