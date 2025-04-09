import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.Random;

public class Main extends Application {

    private static final int GRID_SIZE = 3; // 3x3 grid
    private Button[][] buttons = new Button[GRID_SIZE][GRID_SIZE];
    private Random random = new Random();
    private int score = 0;
    private Button currentMole = null;

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Create grid of buttons
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

        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setTitle("Whack-a-Mole");
        primaryStage.setScene(scene);
        primaryStage.show();

        showRandomMole(); // Start the game
    }

    private void handleClick(Button clickedButton) {
        if (clickedButton == currentMole) {
            System.out.println("WHACK! +1");
            score++;
            System.out.println("Score: " + score);
            clickedButton.setText(" ");
            showRandomMole();
        } else {
            System.out.println("Miss! -1");
            score--;
            System.out.println("Score: " + score);
        }
    }
    
    

    private void showRandomMole() {
        // Clear current mole
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
