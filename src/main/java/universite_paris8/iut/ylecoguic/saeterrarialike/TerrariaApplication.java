package universite_paris8.iut.ylecoguic.saeterrarialike;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import universite_paris8.iut.ylecoguic.saeterrarialike.controller.Controller;

import java.io.IOException;

public class TerrariaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TerrariaApplication.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        Controller.setScene(scene);
        stage.setTitle("The Custest Story Of The Unicorn Slayer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}