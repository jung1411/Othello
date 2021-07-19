package main.example;


import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    static public Scene scene;
    @Override

    // Standard boiler plate code
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Othello");

        // Select main fxml file
        Parent root = FXMLLoader.load(
            Objects.requireNonNull(getClass().getClassLoader().getResource("main.fxml")));
        scene = new Scene(root);

        // set primary scene
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        // display on ui
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


}

