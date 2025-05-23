package universite_paris8.iut.ylecoguic.saeterrarialike.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Joueur;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Map;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Objet;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueJoueur;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueMap;

public class Controller implements Initializable{

    @FXML
    private TilePane panneauDeJeu;
    @FXML
    private Pane panneauJoueur;
    private Map map;
    private VueMap vueMap;
    private Joueur joueur;
    private VueJoueur vueJoueur;
    private boolean droite = false;
    private boolean gauche = false;
    private boolean enSaut = false;
    @FXML
    private TableView<Objet> inventaire;//nom table
    @FXML
    private TableColumn<Objet, String> col1;//nom colonne
    @FXML
    private TableColumn<Objet, String> col2;
    @FXML
    private TableColumn<Objet, String> col3;

    public void gestionClavier() {
        panneauDeJeu.sceneProperty().addListener((obs, ancienneScene, nouvelleScene) -> {
            nouvelleScene.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case Z, UP, SPACE:
                        enSaut = true ;
                        break;
                    case Q, LEFT:
                        gauche = true;
                        break;
                    case D, RIGHT:
                        droite = true;
                        break;
                }
            });

            nouvelleScene.setOnKeyReleased(event -> {
                switch (event.getCode()) {
                    case Z, UP, SPACE:
                        enSaut = false;
                        break;
                    case Q, LEFT:
                        droite = false;
                        break;
                    case D, RIGHT:
                        gauche = false;
                        break;
                }
            });
        });
    }

    public void AnimationTimer(){
        AnimationTimer timer = new AnimationTimer() { // classe qui sert pour faire des animations fluides car dans sa méthode handle ,ce qui est écrit dedans est effectué toutes les frames
            private long lastUpdate = 0;
            private final long frameInterval = 16_666_666; // Conversion nano secondes en secondes = 60 FPS
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= frameInterval) {
                    if (gauche == true){
                        joueur.deplacement(-1, 0);
                        gauche = false;
                    }
                    else if (droite== true) {
                        joueur.deplacement(1, 0);
                        droite = false;
                    }
                    if (!joueur.collision(joueur.getX(), joueur.getY() + joueur.getVGravite())){
                        joueur.gravite(0, -1);
                        enSaut = true;
                    }
                    else {
                        if (!enSaut){
                            joueur.saut(0, 1);
                            enSaut = false;
                        }
                    }
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        map = new Map();
        vueMap = new VueMap(panneauDeJeu, map);
        joueur = new Joueur(500, 805, map);
        vueJoueur = new VueJoueur(panneauJoueur);
        vueJoueur.getImageView().translateXProperty().bind(joueur.getxProperty());
        vueJoueur.getImageView().translateYProperty().bind(joueur.getyProperty());
        gestionClavier();
        AnimationTimer();
    }
}