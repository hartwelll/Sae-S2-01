package universite_paris8.iut.ylecoguic.saeterrarialike.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.input.MouseButton;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Inventaire;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Joueur;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.animation.AnimationTimer;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Map;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Objet;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueJoueur;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueMap;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueObjet;

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
    @FXML private Pane objetAffiche;
    @FXML private TableView<Objet> inventaireTable;
    @FXML private TableColumn<Objet, String> nomCol;
    @FXML private TableColumn<Objet, String> descCol;
    @FXML private TableColumn<Objet, Integer> quantCol;
    private final Inventaire inventaire = new Inventaire();

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
                            break;
                    }
                });
                newScene.setOnKeyReleased(event -> {
                    touchesActives.remove(event.getCode());
                });
                newScene.setOnMouseClicked(this::handleMouseClick);
            }
        });
    }

    private void handleMouseClick(MouseEvent event) {
        int colTileCliquer = (int) (event.getX() / 32);
        int ligneTileCliquer = (int) (event.getY() / 32);
        int joueurPoseTileX = joueur.getTileX();
        int joueurPoseTileY = joueur.getTileY();

        boolean isAdjacent = Math.abs(colTileCliquer - joueurPoseTileX) <= 1 && Math.abs(ligneTileCliquer - joueurPoseTileY) <= 1;

        if (isAdjacent) {
            int idBloc = map.getCase(ligneTileCliquer, colTileCliquer);
            if (idBloc != 0 && idBloc != 3) {
                Objet objetCasse = creerObjetDepuisBloc(idBloc);
                if (objetCasse != null) {
                    inventaire.addObjet(objetCasse);
                    System.out.println("Bloc cassé ajouté à l'inventaire : " + objetCasse.getNom());
                    System.out.println("Quantite dans inventaire : " + inventaire.getObjets().size());
                }
                map.setCase(ligneTileCliquer, colTileCliquer, 0);
                vueMap.miseAJourAffichage(ligneTileCliquer, colTileCliquer);
            }
        }
    }

    public void craft(){
        System.out.println("fabrication");
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
                        if (!coeurList.isEmpty()) {
                            if (joueur.getVie() % 10 == 0 && joueur.decrementerVie() && joueur.getVie() <= 90) {
                                coeurList.get(0).setVisible(false);
                                coeurList.remove(0);
                            }
                        }
                    }
                    joueur.appliquerMouvementVertival();
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    private Objet creerObjetDepuisBloc(int idBloc) {
        switch (idBloc) {
            case 1:
                return new Objet("Pierre", "Bloc de pierre");
            case 2:
                return new Objet("Caisse", "Caisse en bois");
            default:
                return null; // Pour les blocs non récupérables (comme l'air, id = 0)
        }
    }

    private void spawnObjects() {
        Objet objet = new Objet("Épée", "Une épée brillante");
        VueObjet epee = new VueObjet(objet, 100, 730, 60, 60);

        epee.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                System.out.println("Ajout à l'inventaire : " + epee.getObjet().getNom());
                inventaire.addObjet(epee.getObjet());
                objetAffiche.getChildren().remove(epee);
                System.out.println("Nombre d'objets dans l'inventaire : " + inventaire.getObjets().size());
            }
        });

        objetAffiche.getChildren().add(epee);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        map = new Map();
        vueMap = new VueMap(panneauDeJeu, map); // La VueMap gère maintenant toutes les ImageView
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
        nomCol.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        descCol.setCellValueFactory(cellData -> cellData.getValue().descProperty());
        quantCol.setCellValueFactory(cellData -> cellData.getValue().quantiteProperty().asObject());
        inventaireTable.setItems(inventaire.getObjets());

        spawnObjects();
        setupClavierInput();
        startAnimationTimer();
    }
}