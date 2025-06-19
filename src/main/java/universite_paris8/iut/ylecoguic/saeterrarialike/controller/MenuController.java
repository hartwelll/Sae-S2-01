package universite_paris8.iut.ylecoguic.saeterrarialike.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private void startGame(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/ylecoguic/saeterrarialike/view.fxml"));
        Scene gameScene = new Scene(fxmlLoader.load(), 1920, 1080);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("The Cutest Story Of The Unicorn Slayer");
        stage.setScene(gameScene);
        stage.show();
    }
}