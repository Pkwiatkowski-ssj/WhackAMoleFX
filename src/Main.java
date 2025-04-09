import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Main extends Application {

    private static final int GRID_SIZE = 3;
    private Button[][] buttons = new Button[GRID_SIZE][GRID_SIZE];
    private Random random = new Random();
    private int score = 0;
    private Button moleButton = null;
    private Button bunnyButton = null;

    private Label scoreLabel = new Label("Score: 0");
    private Label feedbackLabel = new Label("");
    private Label usernameLabel = new Label("");
    private Label speedLabel = new Label("");

    private String username = "";
    private Timeline moleMover;
    private double moleDelay = 3.0;
    private final double MIN_DELAY = 0.35;

    @Override
    public void start(Stage primaryStage) {
        showUsernameScreen(primaryStage);
    }

    private void showUsernameScreen(Stage stage) {
        Label prompt = new Label("Enter your username:");
        TextField usernameField = new TextField();
        Button enterButton = new Button("Enter Game");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(prompt, usernameField, enterButton);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Whack-a-Mole Login");
        stage.show();

        enterButton.setOnAction(e -> {
            String input = usernameField.getText().trim();
            if (!input.isEmpty()) {
                username = input;
                usernameLabel.setText(username);
                showGameScreen(stage);
            }
        });

        usernameField.setOnAction(e -> enterButton.fire());
    }

    private void showGameScreen(Stage stage) {
        scoreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        feedbackLabel.setStyle("-fx-font-size: 20px;");
        usernameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        speedLabel.setStyle("-fx-font-size: 14px;");

        Region spacerLeft = new Region();
        Region spacerRight = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER);
        topBar.getChildren().addAll(scoreLabel, spacerLeft, feedbackLabel, spacerRight, usernameLabel);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Button btn = new Button(" ");
                btn.setPrefSize(100, 100);
                final int r = row;
                final int c = col;
                btn.setOnAction(e -> handleClick(buttons[r][c]));
                buttons[row][col] = btn;
                grid.add(btn, col, row);
            }
        }

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(grid);
        root.setBottom(speedLabel);
        BorderPane.setAlignment(speedLabel, Pos.CENTER);
        BorderPane.setMargin(speedLabel, new Insets(10));

        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Whack-a-Mole");
        stage.setScene(scene);
        stage.show();

        startMoleTimer();
    }

    private void handleClick(Button clickedButton) {
        if (clickedButton == moleButton) {
            System.out.println("WHACK! +1");
            score++;
            feedbackLabel.setText("+1");
            feedbackLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: green;");

            speedUp();           // Speed up the timer
            moveCharacters();    // Move mole + bunny immediately
        } else if (clickedButton == bunnyButton) {
            System.out.println("NOOO! That was the bunny. -2");
            score -= 2;
            feedbackLabel.setText("-2");
            feedbackLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: red;");
        } else {
            System.out.println("Missed! Empty tile. -1");
            score -= 1;
            feedbackLabel.setText("-1");
            feedbackLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: orange;");
        }

        scoreLabel.setText("Score: " + score);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> feedbackLabel.setText(""));
        pause.play();
    }

    private void speedUp() {
        if (moleDelay > MIN_DELAY) {
            moleDelay -= 0.2;
            if (moleDelay < MIN_DELAY) moleDelay = MIN_DELAY;

            speedLabel.setText("Speeding up! Delay: " + String.format("%.2f", moleDelay) + "s");

            moleMover.stop();
            moleMover = new Timeline(new KeyFrame(Duration.seconds(moleDelay), e -> moveCharacters()));
            moleMover.setCycleCount(Timeline.INDEFINITE);
            moleMover.play();
        }
    }

    private void startMoleTimer() {
        moveCharacters(); // Start with first mole/bunny

        speedLabel.setText("Delay: " + String.format("%.2f", moleDelay) + "s");

        moleMover = new Timeline(new KeyFrame(Duration.seconds(moleDelay), e -> moveCharacters()));
        moleMover.setCycleCount(Timeline.INDEFINITE);
        moleMover.play();
    }

    private void moveCharacters() {
        if (moleButton != null) {
            moleButton.setText(" ");
            moleButton.setStyle("");
        }
        if (bunnyButton != null) {
            bunnyButton.setText(" ");
            bunnyButton.setStyle("");
        }

        int moleRow, moleCol, bunnyRow, bunnyCol;
        do {
            moleRow = random.nextInt(GRID_SIZE);
            moleCol = random.nextInt(GRID_SIZE);
            bunnyRow = random.nextInt(GRID_SIZE);
            bunnyCol = random.nextInt(GRID_SIZE);
        } while (moleRow == bunnyRow && moleCol == bunnyCol);

        moleButton = buttons[moleRow][moleCol];
        bunnyButton = buttons[bunnyRow][bunnyCol];

        moleButton.setText("🐹");
        moleButton.setStyle("-fx-text-fill: brown; -fx-font-size: 20px;");

        bunnyButton.setText("🐰");
        bunnyButton.setStyle("-fx-text-fill: lightblue; -fx-font-size: 20px;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
