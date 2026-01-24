package pt.uevora.joker.app.ui.fx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JokerFxApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("GUI Mode (Work in Progress)");
        Label fxLabel = new Label("JavaFX OK");
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event -> primaryStage.close());

        VBox root = new VBox(12, titleLabel, fxLabel, exitButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 420, 200);
        primaryStage.setTitle("Jogo do Joker - GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
