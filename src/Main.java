import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Main extends Application {

    private static final int GRID_SIZE = 3;
    private Button[][] buttons = new Button[GRID_SIZE][GRID_SIZE];
    private Random random = new Random();
    private int score = 0;
    private Button currentMole = null;

    private Label scoreLabel = new Label("Score: 0");
    private Label feedbackLabel = new Label("");

    @Override
    public void start(Stage primaryStage) {
        // Top bar with score and feedback
        scoreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        feedbackLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: red;");

        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER);
        topBar.getChildren().addAll(scoreLabel, feedbackLabel);

        // Game grid
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

        // Layout root
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(grid);

        Scene scene = new Scene(root, 400, 450);
        primaryStage.setTitle("Whack-a-Mole");
        primaryStage.setScene(scene);
        primaryStage.show();

        showRandomMole(); // Start game
    }

    private void handleClick(Button clickedButton) {
        if (clickedButton == currentMole) {
            System.out.println("WHACK! +1");
            score++;
            feedbackLabel.setText("+1");
            feedbackLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: green;");
            clickedButton.setText(" ");
        } else {
            System.out.println("Miss! -1");
            score--;
            feedbackLabel.setText("-1");
            feedbackLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: red;");
        }

        scoreLabel.setText("Score: " + score);

        // Fade the feedback after 1 second
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> feedbackLabel.setText(""));
        pause.play();

        showRandomMole();
    }

    private void showRandomMole() {
        if (currentMole != null) {
            currentMole.setText(" ");
        }

        int row = random.nextInt(GRID_SIZE);
        int col = random.nextInt(GRID_SIZE);
        currentMole = buttons[row][col];
        currentMole.setText("🐹");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
