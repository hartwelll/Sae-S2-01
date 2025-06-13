package universite_paris8.iut.ylecoguic.saeterrarialike.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
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
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueEnnemis;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueJoueur;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueMap;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueObjet;

public class Controller implements Initializable {

    @FXML private TilePane panneauDeJeu;
    @FXML private Pane panneauJoueur;
    @FXML private Pane craft;
    @FXML private Pane tuto;
    @FXML private Pane consignes;
    @FXML private Pane TableCraft;
    @FXML private ImageView coeur1, coeur2, coeur3, coeur4, coeur5, coeur6, coeur7, coeur8, coeur9, coeur10;
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
    private Ennemis ennemis;
    private VueEnnemis vueEnnemis;
    private ArrayList<ImageView> coeurList;
    private Set<KeyCode> touchesActives;
    private final Inventaire inventaire = new Inventaire();

    public void setupInput() {
        panneauDeJeu.sceneProperty().addListener((obs, oldScene, sceneActuel) -> {
            if (sceneActuel != null) {
                sceneActuel.setOnKeyPressed(event -> {
                    touchesActives.add(event.getCode());
                    switch (event.getCode()){
                        case C:
                            if (!craft.isVisible() && !TableCraft.isVisible()){
                                craft.setVisible(true);
                            } else{
                                craft.setVisible(false);
                            }
                            break;
                        case ESCAPE:
                            if (!tuto.isVisible()) {
                                tuto.setVisible(true);
                                consignes.setVisible(false);
                            } else {
                                tuto.setVisible(false);
                            }
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
            casserBlock(colTileCliquer, ligneTileCliquer, estAdjacentCasseBlock);
        }
        else if (event.getButton() == MouseButton.SECONDARY) {
            poserBlock(colTileCliquer, ligneTileCliquer, estAdjacentPoseBlock);
        }
        vueMap.miseAJourAffichage(ligneTileCliquer, colTileCliquer);
    }

    public void casserBlock(int colTileClick, int ligneTileClick, boolean adjacent){
        int nbAajouter;
        if (adjacent) {
            int idBloc = map.getCase(ligneTileClick, colTileClick);
            if (idBloc != 0 && idBloc != 3) {
                Objet objetCasse = creerObjetDepuisBloc(idBloc);
                if (objetCasse != null) {
                    nbAajouter = 1;
                    if(idBloc == 2){
                        nbAajouter = 2;
                    }
                    inventaire.addObjet(objetCasse, nbAajouter);
                }
                map.setCase(ligneTileClick, colTileClick, 0);
            }
        }
    }

    public void poserBlock(int colTileClick, int ligneTileClick, boolean adjacent){
        if (adjacent) {
            int idBlocCible = map.getCase(ligneTileClick, colTileClick);
            if (idBlocCible == 0) {
                Objet objetSelectionne = inventaireTable.getSelectionModel().getSelectedItem();
                if (objetSelectionne != null) {
                    if (objetSelectionne.getQuantite() > 0) {
                        int idBlocAPoser = getIdBlocDepuisObjet(objetSelectionne);
                        if (idBlocAPoser != 0) {
                            inventaire.removeObjet(objetSelectionne, 1);
                            map.creeCase(ligneTileClick, colTileClick, idBlocAPoser);
                        }
                    }
                }
            } else if (idBlocCible == 4) {
                if(Math.abs(joueur.getX() / 32 - map.getColId(4)) <= 2 && Math.abs(joueur.getY() / 32 - map.getLigneId(4)) <= 2) {
                    if (!TableCraft.isVisible() && !craft.isVisible()) {
                        TableCraft.setVisible(true);
                    } else {
                        TableCraft.setVisible(false);
                    }
                }
            }
        }
    }

    private int getIdBlocDepuisObjet(Objet objet) {
        switch (objet.getNom()) {
            case "Pierre":
                return 1;
            case "Caisse En Bois":
                return 2;
            case "Table De Craft":
                return 4;
            default:
                return 0;
        }
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
                    Objet boisARemove = creerObjetDepuisBloc(2);
                    Objet pierreARemove = creerObjetDepuisBloc(1);

                    inventaire.removeObjet(boisARemove, nbBois);
                    inventaire.removeObjet(pierreARemove, nbPierre);
                    System.out.println("Ajout à l'inventaire : " + nouvelItem.getObjet().getNom());
                    inventaire.addObjet(nouvelItem.getObjet(), 1);
                    System.out.println("Nombre d'objets dans l'inventaire : " + inventaire.getObjets().size());
                }
            }
        });
    }

    private Objet creerObjetDepuisBloc(int idBloc) {
        switch (idBloc) {
            case 1:
                return new Objet("Pierre", "De la pierre");
            case 2, 5:
                return new Objet("Bois", "Du bois");
            case 4:
                return new Objet("Table De Craft", "une simple table de craft");
            default:
                return null;
        }
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
                    if (!coeurList.isEmpty()) {
                        if (joueur.getVie() % 10 == 0 && joueur.decrementerVie() && joueur.getVie() <= 90) {
                            coeurList.get(0).setVisible(false);
                            coeurList.remove(0);
                        }
                    }
                    if (Math.abs(joueur.getX() / 32 - map.getColId(4)) >= 4 || Math.abs(joueur.getY() / 32 - map.getLigneId(4)) >= 4) {
                        TableCraft.setVisible(false);
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
        vueMap = new VueMap(panneauDeJeu, map); // La VueMap gère maintenant toutes les ImageView
        joueur = new Joueur(500, 725, map);
        vueJoueur = new VueJoueur(panneauJoueur);
        vueJoueur.getImageView().translateXProperty().bind(joueur.getxProperty());
        vueJoueur.getImageView().translateYProperty().bind(joueur.getyProperty());
        ennemis = new Ennemis(600, 625, map);
        vueEnnemis = new VueEnnemis(panneauJoueur);
        vueEnnemis.getImageView().translateXProperty().bind(ennemis.getxProperty());
        vueEnnemis.getImageView().translateYProperty().bind(ennemis.getyProperty());
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
        TableCraft.setVisible(false);
        tuto.setVisible(false);
        touchesActives = new HashSet<>();
        nomCol.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        descCol.setCellValueFactory(cellData -> cellData.getValue().descProperty());
        quantCol.setCellValueFactory(cellData -> cellData.getValue().quantiteProperty().asObject().asString());
        inventaireTable.setItems(inventaire.getObjets());
        spawnObjects();
        setupInput();
        startAnimationTimer();
    }
}