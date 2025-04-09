import javafx.animation.PauseTransition;
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
    private Button currentMole = null;

    private Label scoreLabel = new Label("Score: 0");
    private Label feedbackLabel = new Label("");
    private Label usernameLabel = new Label("");

    private String username = "";

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

        // Pressing Enter in the field also starts the game
        usernameField.setOnAction(e -> enterButton.fire());
    }

    private void showGameScreen(Stage stage) {
        // TOP BAR
        scoreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        feedbackLabel.setStyle("-fx-font-size: 20px;");
        usernameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Region spacerLeft = new Region();
        Region spacerRight = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER);
        topBar.getChildren().addAll(scoreLabel, spacerLeft, feedbackLabel, spacerRight, usernameLabel);

        // GRID
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

        // MAIN LAYOUT
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(grid);

        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Whack-a-Mole");
        stage.setScene(scene);
        stage.show();

        showRandomMole();
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
