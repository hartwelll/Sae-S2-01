package universite_paris8.iut.ylecoguic.saeterrarialike.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.input.MouseButton;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.animation.AnimationTimer;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.*;

public class Controller implements Initializable {

    @FXML private Pane menu;
    @FXML private TilePane panneauDeJeu;
    @FXML private Pane panneauJoueur;
    @FXML private Pane craft;
    @FXML private Pane tuto;
    @FXML private Pane consignes;
    @FXML private Pane TableCraft;
    @FXML private HBox coeurs;
    @FXML private TableView<Objet> inventaireTable;
    @FXML private TableColumn<Objet, String> nomCol;
    @FXML private TableColumn<Objet, String> descCol;
    @FXML private TableColumn<Objet, String> quantCol;
    @FXML private Button pioche;
    @FXML private Button pelle;
    @FXML private Button epee;
    @FXML private Button tableDeCraft;
    @FXML private Button caisse;
    @FXML private Pane objetAffiche;
    private Map map;
    private VueMap vueMap;
    private Joueur joueur;
    private VueJoueur vueJoueur;
    private Coeur coeur;
    private VueCoeur vueCoeur;
    private Ennemis ennemis;
    private VueEnnemis vueEnnemis;
    private Set<KeyCode> touchesActives;
    private final Inventaire inventaire = new Inventaire();
    private ArrayList<Button> buttonList = new ArrayList<>();

    private AnimationTimer gameTimer;


    public void retourJeu() {
        menu.setVisible(false);
        if (gameTimer != null) {
            gameTimer.start();
        }
    }
    public void showTuto() {
        tuto.setVisible(true);
        menu.setVisible(false);
        if (gameTimer != null) {
            gameTimer.start();
        }
    }
    public void quitGame() {
        System.exit(0);
    }

    public void setupInput() {
        panneauDeJeu.sceneProperty().addListener((obs, oldScene, sceneActuel) -> {
            if (sceneActuel != null) {
                sceneActuel.setOnKeyPressed(event -> {
                    touchesActives.add(event.getCode());
                    switch (event.getCode()){
                        case C:
                            craft.setVisible(!craft.isVisible() && !TableCraft.isVisible());
                            break;
                        case ESCAPE:
                            if (tuto.isVisible()) {
                                tuto.setVisible(false);
                                if (gameTimer != null && !menu.isVisible()) {
                                    gameTimer.start();
                                }
                            } else if (!menu.isVisible()) {
                                menu.setVisible(true);
                                consignes.setVisible(false);
                                if (gameTimer != null) {
                                    gameTimer.stop();
                                }
                            } else {
                                menu.setVisible(false);
                                if (gameTimer != null) {
                                    gameTimer.start();
                                }
                            }
                            break;
                    }
                });
                sceneActuel.setOnKeyReleased(event -> {
                    touchesActives.remove(event.getCode());
                });
                sceneActuel.setOnMouseClicked(this::clickBlock);
            }
        });
    }

    private void clickBlock(MouseEvent event) {
        int colTileCliquer = (int) (event.getX() / 32);
        int ligneTileCliquer = (int) (event.getY() / 32);

        int joueurPoseTileX = joueur.getTileX();
        int joueurPoseTileY = joueur.getTileY();

        boolean estAdjacentCasseBlock = Math.abs(colTileCliquer - joueurPoseTileX) <= 1 && Math.abs(ligneTileCliquer - joueurPoseTileY) <= 1;
        boolean estAdjacentPoseBlock = Math.abs(colTileCliquer - joueurPoseTileX) <= 2 && Math.abs(ligneTileCliquer - joueurPoseTileY) <= 2;

        if (event.getButton() == MouseButton.PRIMARY) {
            joueur.casserBlock(colTileCliquer, ligneTileCliquer, estAdjacentCasseBlock);
        }
        else if (event.getButton() == MouseButton.SECONDARY) {
            joueur.poserBlock(colTileCliquer, ligneTileCliquer, estAdjacentPoseBlock);
        }
        vueMap.miseAJourAffichage(ligneTileCliquer, colTileCliquer);
    }


    public void craft() {
        craftItemButton(tableDeCraft, "Table De Craft", "une simple table de craft", 4, 0);
        craftItemButton(caisse, "Caisse En Bois", "une caisse qui caisse", 2, 0);
    }

    public void craftDansTableCraft(){
        craftItemButton(pioche, "Pioche", "Une pioche brillante",2,3 );
        craftItemButton(pelle, "Pelle", "Une pelle brillante",3,1);
        craftItemButton(epee, "Épée", "Une épée brillante", 1, 2);
    }

    private void craftItemButton(Button bouttonItem, String itemName, String itemDescription, int nbBois, int nbPierre) {
        bouttonItem.setOnMouseClicked(e -> {
            if(inventaire.getQuantiteObjet("Bois") >= nbBois && inventaire.getQuantiteObjet("Pierre") >= nbPierre) {
                if (e.getButton() == MouseButton.PRIMARY) {
                    Objet objet = new Objet(itemName, itemDescription);
                    VueObjet nouvelItem = new VueObjet(objet, 100, 730, 60, 60);
                    Objet boisARemove = joueur.creerObjetDepuisBloc(2);
                    Objet pierreARemove = joueur.creerObjetDepuisBloc(1);

                    inventaire.removeObjet(boisARemove, nbBois);
                    inventaire.removeObjet(pierreARemove, nbPierre);
                    System.out.println("Ajout à l'inventaire : " + nouvelItem.getObjet().getNom());
                    inventaire.addObjet(nouvelItem.getObjet(), 1);
                    System.out.println("Nombre d'objets dans l'inventaire : " + inventaire.getObjets().size());
                }
            }
        });
    }

    private void spawnObjects() {
        Objet objet = new Objet("Sabre Laser", "Un laser qui koupe !!! ");
        VueObjet sabre = new VueObjet(objet, 100, 762, 32, 32, "/Objet/lightSaberDrop.png");

        sabre.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                System.out.println("Ajout à l'inventaire : " + sabre.getObjet().getNom());
                inventaire.addObjet(sabre.getObjet(), 1);
                objetAffiche.getChildren().remove(sabre);
                System.out.println("Nombre d'objets dans l'inventaire : " + inventaire.getObjets().size());
            }
        });
        objetAffiche.getChildren().add(sabre);
    }

    public void animationTimer() {
        gameTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            private final long frameInterval = 16_666_666; // Environ 60 FPS

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
                    if (Math.abs(joueur.getX() / 32 - map.getColId(4)) >= 4 || Math.abs(joueur.getY() / 32 - map.getLigneId(4)) >= 4) {
                        TableCraft.setVisible(false);
                    }
                    coeur.enleverCoeur();
                    joueur.appliquerMouvementVertival();
                    ennemis.appliquerMouvementVertival();
                    ennemis.deplacement();
                    lastUpdate = now;
                }
            }
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        map = new Map();
        vueMap = new VueMap(panneauDeJeu, map);
        joueur = new Joueur(500, 725, map, 100, 8, inventaire, inventaireTable, craft, TableCraft);
        vueJoueur = new VueJoueur(panneauJoueur);
        vueJoueur.getImageView().translateXProperty().bind(joueur.getxProperty());
        vueJoueur.getImageView().translateYProperty().bind(joueur.getyProperty());
        vueCoeur = new VueCoeur(coeurs);
        coeur = new Coeur(joueur, vueCoeur);
        ennemis = new Ennemis(600, 625, map, 50, 4);
        vueEnnemis = new VueEnnemis(panneauJoueur);
        vueEnnemis.getImageView().translateXProperty().bind(ennemis.getxProperty());
        vueEnnemis.getImageView().translateYProperty().bind(ennemis.getyProperty());
        craft.setVisible(false);
        TableCraft.setVisible(false);
        tuto.setVisible(false);
        menu.setVisible(false);

        touchesActives = new HashSet<>();
        buttonList = new ArrayList<>();
        nomCol.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        descCol.setCellValueFactory(cellData -> cellData.getValue().descProperty());
        quantCol.setCellValueFactory(cellData -> cellData.getValue().quantiteProperty().asObject().asString());
        inventaireTable.setItems(inventaire.getObjets());
        spawnObjects();
        setupInput();
        animationTimer();

        menu.visibleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                gameTimer.stop();
                System.out.println("Timer arrêté (menu visible)");
            } else {
                gameTimer.start();
                System.out.println("Timer démarré (menu invisible)");
            }
        });
        if (!menu.isVisible()) {
            gameTimer.start();
        }
    }
}