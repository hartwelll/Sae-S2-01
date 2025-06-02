package universite_paris8.iut.ylecoguic.saeterrarialike.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Joueur;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.animation.AnimationTimer;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Map;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueJoueur;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueMap;

public class Controller implements Initializable {

    @FXML
    private TilePane panneauDeJeu;
    @FXML
    private Pane panneauJoueur;
    @FXML
    private Pane craft;
    @FXML
    private ImageView coeur1, coeur2, coeur3, coeur4, coeur5, coeur6, coeur7, coeur8, coeur9, coeur10;
    private Map map;
    private VueMap vueMap;
    private Joueur joueur;
    private VueJoueur vueJoueur;
    private ArrayList<ImageView> coeurList;

    private Set<KeyCode> touchesActives;

    public void setupClavierInput() {
        panneauDeJeu.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    touchesActives.add(event.getCode());
                    switch (event.getCode()){
                        case C:
                            if (!craft.isVisible()){
                                craft.setVisible(true);
                            }
                            else{
                                craft.setVisible(false);
                            }
                    }
                });
                newScene.setOnKeyReleased(event -> {
                    touchesActives.remove(event.getCode());
                });
            }
        });
    }

    public void startAnimationTimer() {
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            private final long frameInterval = 16_666_666;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= frameInterval) {
                    if (touchesActives.contains(KeyCode.Q) || touchesActives.contains(KeyCode.LEFT)) {
                        joueur.deplacement(-1, 0);
                    } else if (touchesActives.contains(KeyCode.D) || touchesActives.contains(KeyCode.RIGHT)) {
                        joueur.deplacement(1, 0);
                    }

                    if (touchesActives.contains(KeyCode.Z) || touchesActives.contains(KeyCode.UP) || touchesActives.contains(KeyCode.SPACE)) {
                        joueur.demarrerSaut();
                    }

                    if (!coeurList.isEmpty()){
                        if (joueur.getVie()%5 == 0 && joueur.decrementerVie() && joueur.getVie() <= 45){
                            coeurList.get(0).setVisible(false);
                            coeurList.remove(0);
                        }
                    }

                    joueur.appliquerMouvementVertival();

                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        map = new Map();
        vueMap = new VueMap(panneauDeJeu, map);
        joueur = new Joueur(500, 725, map);
        vueJoueur = new VueJoueur(panneauJoueur);
        vueJoueur.getImageView().translateXProperty().bind(joueur.getxProperty());
        vueJoueur.getImageView().translateYProperty().bind(joueur.getyProperty());
        coeurList = new ArrayList<>();
        coeurList.add(coeur1);
        coeurList.add(coeur2);
        coeurList.add(coeur3);
        coeurList.add(coeur4);
        coeurList.add(coeur5);
        coeurList.add(coeur6);
        coeurList.add(coeur7);
        coeurList.add(coeur8);
        coeurList.add(coeur9);
        coeurList.add(coeur10);
        craft.setVisible(false);
        touchesActives = new HashSet<>();
        setupClavierInput();
        startAnimationTimer();
    }
}