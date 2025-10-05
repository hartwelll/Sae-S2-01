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

/**
 * Cette class represente le contrôleur principal de l'application JavaFX.
 * Responsabilités :
 * - Initialiser le modèle et les vues
 * - Gérer les entrées utilisateur (clavier, souris)
 * - Orchestrer la boucle de jeu (AnimationTimer)
 * - Gérer les menus et interfaces
 * - Faire le lien entre le modèle et les vues
 */
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
    private Terrain terrain;
    private VueTerrain vueTerrain;
    private Joueur joueur;
    private VueJoueur vueJoueur;
    private Coeur coeur;
    private VueCoeur vueCoeur;
    private Ennemis ennemis;
    private VueEnnemis vueEnnemis;
    private ArrayList<Entite> entites;
    private Set<KeyCode> touchesActives;
    private final Inventaire inventaire = new Inventaire();
    private AnimationTimer gameTimer;


    public void retourJeu() {
        menu.setVisible(false);
        if (gameTimer != null) {
            gameTimer.start();
        }
    }
    public void afficherTuto() {
        tuto.setVisible(true);
        menu.setVisible(false);
        if (gameTimer != null) {
            gameTimer.start();
        }
    }
    public void quitterPartie() {
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
            if (colTileCliquer == ennemis.getTileX() && ligneTileCliquer == ennemis.getTileY()){
                joueur.attaque(ennemis, 10);
            }else joueur.casserBlock(colTileCliquer, ligneTileCliquer, estAdjacentCasseBlock);

        }
        else if (event.getButton() == MouseButton.SECONDARY) {
            joueur.poserBlock(colTileCliquer, ligneTileCliquer, estAdjacentPoseBlock);
        }
        vueTerrain.miseAJourAffichage(ligneTileCliquer, colTileCliquer);
    }


    //à déplacer dans class fab
    public void fabrication() {
        configurerBoutonCraft(tableDeCraft, "Table De Craft");
        configurerBoutonCraft(caisse, "Caisse En Bois");
    }

    //à déplacer dans class fab
    public void fabricationDansTableDeFabrication(){
        configurerBoutonCraft(pioche, "Pioche");
        configurerBoutonCraft(pelle, "Pelle");
        configurerBoutonCraft(epee, "Épée");
    }

    private void configurerBoutonCraft(Button bouton, String nomObjet) {
        bouton.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                boolean aProcheTableCraft = TableCraft.isVisible();
                Objet objetCrafte = joueur.tenterCraft(nomObjet, aProcheTableCraft);

                if (objetCrafte != null) {
                    inventaire.ajouterObjet(objetCrafte, 1);
                    System.out.println("Crafté : " + objetCrafte.getNom());
                } else {
                    System.out.println("Craft impossible : ingrédients manquants");
                }
            }
        });
    }

        private void apparitionObjets() {
        Objet objet = new Objet("Sabre Laser", "Un laser qui koupe !!! ");
        VueObjet sabre = new VueObjet(objet, 100, 762, 32, 32, "/Objet/lightSaberDrop.png");

        sabre.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                System.out.println("Ajout à l'inventaire : " + sabre.getObjet().getNom());
                inventaire.ajouterObjet(sabre.getObjet(), 1);
                objetAffiche.getChildren().remove(sabre);
                System.out.println("Nombre d'objets dans l'inventaire : " + inventaire.getObjets().size());
            }
        });
        objetAffiche.getChildren().add(sabre);
    }

    public void animationTimer() {
        gameTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            private final long frameInterval = 16_666_666;
            long delay = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= frameInterval) {
                    joueur.appliquerMouvementVertical();
                    if (touchesActives.contains(KeyCode.Q) || touchesActives.contains(KeyCode.LEFT)) {
                        joueur.deplacement(-1, 0, 1);
                    } else if (touchesActives.contains(KeyCode.D) || touchesActives.contains(KeyCode.RIGHT)) {
                        joueur.deplacement(1, 0, 2);
                    }
                    else vueJoueur.affichage(0);
                    if (touchesActives.contains(KeyCode.Z) || touchesActives.contains(KeyCode.UP) || touchesActives.contains(KeyCode.SPACE)) {
                        joueur.demarrerSaut();
                    }
                    if (Math.abs(joueur.getX() / 32 - terrain.getColId(4)) >= 4 || Math.abs(joueur.getY() / 32 - terrain.getLigneId(4)) >= 4) {
                        TableCraft.setVisible(false);
                    }
                    for(int i = 0 ; i < entites.size(); i++){
                        if (entites.get(i).estMort()){
                            panneauJoueur.getChildren().remove(i);
                            entites.remove(i);
                        }
                    }
                    if(ennemis.getVie() > 0) {
                        ennemis.appliquerMouvementVertical();
                        ennemis.mettreAJourComportement(joueur.getX(), joueur.getY(), 15);
                        if (delay >= 600_000_000) {
                            ennemis.attaque(joueur, 2);
                            delay = 0;
                        } else delay += frameInterval;
                    }
                    lastUpdate = now;
                }
            }
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        terrain = new Terrain();
        vueTerrain = new VueTerrain(panneauDeJeu, terrain);
        vueCoeur = new VueCoeur(coeurs);
        coeur = new Coeur(vueCoeur);
        vueJoueur = new VueJoueur(panneauJoueur);
        joueur = new Joueur(500, 725, terrain, 100, 8, inventaire, inventaireTable, craft, TableCraft, vueCoeur, coeur, vueJoueur);
        vueJoueur.getImageView().translateXProperty().bind(joueur.getxProperty());
        vueJoueur.getImageView().translateYProperty().bind(joueur.getyProperty());
        vueEnnemis = new VueEnnemis(panneauJoueur);
        ennemis = new Ennemis(500, 625, terrain, 50, 4, vueEnnemis);
        vueEnnemis.getImageView().translateXProperty().bind(ennemis.getxProperty());
        vueEnnemis.getImageView().translateYProperty().bind(ennemis.getyProperty());
        entites = new ArrayList();
        entites.add(joueur);
        entites.add(ennemis);
        craft.setVisible(false);
        TableCraft.setVisible(false);
        tuto.setVisible(false);
        menu.setVisible(false);

        touchesActives = new HashSet<>();
        nomCol.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        descCol.setCellValueFactory(cellData -> cellData.getValue().descProperty());
        quantCol.setCellValueFactory(cellData -> cellData.getValue().quantiteProperty().asObject().asString());
        inventaireTable.setItems(inventaire.getObjets());
        apparitionObjets();
        setupInput();
        animationTimer();

        menu.visibleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                gameTimer.stop();
            } else {
                gameTimer.start();
            }
        });

        if (!menu.isVisible()) {
            gameTimer.start();
        }
    }
}