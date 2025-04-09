import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button("Whack Me!");
        btn.setOnAction(e -> System.out.println("WHACK!"));

        StackPane root = new StackPane(btn);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Whack-a-Mole");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
