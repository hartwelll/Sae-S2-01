package universite_paris8.iut.ylecoguic.saeterrarialike.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Joueur;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Objects;
import javafx.animation.AnimationTimer;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Map;

public class Controller implements Initializable{

    @FXML
    private Pane panneauDeJeu;
    private static Circle joueur;
    private static Joueur j;
    private Map map;
    private static Scene scene;

    public void sprite(Joueur j){

        //ImageView joueur = new ImageView(String.valueOf(getClass().getResource("perso.png")));
        joueur = new Circle(10, Color.PINK);
        joueur.translateXProperty().bind(j.getxProperty());
        joueur.translateYProperty().bind(j.getyProperty());
        panneauDeJeu.getChildren().add(joueur);
    }

    public static void seDeplace(){

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                    switch(event.getCode()){
                        case Z:
                            System.out.println("zebi");
                            j.deplacement(0, 1);
                            break;
                        case Q:
                            System.out.println("zebii");
                            j.deplacement(-1, 0);
                            break;
                        case D:
                            System.out.println("zebiii");
                            j.deplacement(1, 0);
                            break;
                    }
                }

        });
    }

    public void AnimationTimer(){
        AnimationTimer timer = new AnimationTimer() { // classe qui sert pour faire des animations fluides car dans sa méthode handle ,ce qui est écrit dedans est effectué toutes les frames
            private long lastUpdate = 0;
            private final long frameInterval = 16_666_666; // Conversion nano secondes en secondes = 60 FPS
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= frameInterval) {

                    j.MAJ(map);
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    public static void setScene(Scene scene) {
        Controller.scene = scene;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        j = new Joueur(250, 100);
        sprite(j);
        AnimationTimer();
    }
}