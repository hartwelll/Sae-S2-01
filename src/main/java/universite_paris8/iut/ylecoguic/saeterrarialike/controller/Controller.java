package universite_paris8.iut.ylecoguic.saeterrarialike.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Joueur;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Objects;

public class Controller implements Initializable {

    @FXML
    private Pane panneauDeJeu;
    private static Circle joueur;
    private static Joueur j;

    public void sprite(){
        j = new Joueur(250, 100);
        //ImageView joueur = new ImageView(String.valueOf(getClass().getResource("perso.png")));
        joueur = new Circle(10, Color.PINK);
        joueur.translateXProperty().bind(j.getxProperty());
        joueur.translateYProperty().bind(j.getyProperty());
        panneauDeJeu.getChildren().add(joueur);
    }

    public static void seDeplace(){
        joueur.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                char dir = ' ';
                switch(dir){
                    case 'z':
                        j.deplacement(0, 1);
                        break;
                    case 'q':
                        j.deplacement(-1, 0);
                        break;
                    case 'd':
                        j.deplacement(1, 0);
                        break;
                }
            }
        });
    }

    public void initialize(URL url, ResourceBundle resourceBundle){

    }
}