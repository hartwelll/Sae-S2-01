package universite_paris8.iut.ylecoguic.saeterrarialike.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.*;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.*;

import java.net.URL;
import java.util.*;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesEntite.*;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesTerrain.*;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesJeu.*;

public class Controller implements Initializable {

    @FXML private Pane menu, panneauJoueur, craft, tuto, consignes, TableCraft, objetAffiche;
    @FXML private TilePane panneauDeJeu;
    @FXML private HBox coeurs;
    @FXML private TableView<Objet> inventaireTable;
    @FXML private TableColumn<Objet, String> nomCol, descCol, quantCol;
    @FXML private Button pioche, pelle, epee, tableDeCraft, caisse;

    private Environnement env;
    private Terrain terrain;
    private Joueur joueur;
    private Ennemis ennemis;
    private ArrayList<Entite> entites;
    private final Inventaire inventaire = new Inventaire();

    private VueTerrain vueTerrain;
    private VueJoueur vueJoueur;
    private VueCoeur vueCoeur;
    private VueEnnemis vueEnnemis;

    private Set<KeyCode> touchesActives;
    private AnimationTimer gameTimer;
    public void retourJeu() { menu.setVisible(false); if (gameTimer != null) gameTimer.start(); }
    public void afficherTuto() { tuto.setVisible(true); menu.setVisible(false); if (gameTimer != null) gameTimer.start(); }
    public void quitterPartie() { System.exit(0); }
    public void setupInput() {
        panneauDeJeu.sceneProperty().addListener((obs, oldScene, sceneActuel) -> {
            if (sceneActuel == null) return;
            sceneActuel.setOnKeyPressed(event -> {
                touchesActives.add(event.getCode());
                switch (event.getCode()) {
                    case C -> craft.setVisible(!craft.isVisible() && !TableCraft.isVisible());
                    case ESCAPE -> gererMenuOuTuto();
                }
            });
            sceneActuel.setOnKeyReleased(event -> touchesActives.remove(event.getCode()));
            sceneActuel.setOnMouseClicked(this::clickBlock);
        });
    }
    private void gererMenuOuTuto() {
        if (tuto.isVisible()) {
            tuto.setVisible(false);
            if (gameTimer != null && !menu.isVisible()) gameTimer.start();
        } else if (!menu.isVisible()) {
            menu.setVisible(true);
            consignes.setVisible(false);
            if (gameTimer != null) gameTimer.stop();
        } else {
            menu.setVisible(false);
            if (gameTimer != null) gameTimer.start();
        }
    }

    private void clickBlock(MouseEvent event) {
        int colTile = (int) (event.getX() / TAILLE_TUILE);
        int ligneTile = (int) (event.getY() / TAILLE_TUILE);


        if (event.getButton() == MouseButton.PRIMARY) {
            joueur.clicGauche(colTile, ligneTile, env);

        } else if (event.getButton() == MouseButton.SECONDARY) {
            Objet objetSelectionne = inventaireTable.getSelectionModel().getSelectedItem();

            joueur.clicDroit(colTile, ligneTile, objetSelectionne);

            int idBlocCible = terrain.codeTuile(ligneTile, colTile);
            if (idBlocCible == TUILE_TABLE_CRAFT &&
                    joueur.estDansPortee(colTile, ligneTile, joueur.getTileX(), joueur.getTileY(), PORTEE_TABLE_CRAFT)) {

                TableCraft.setVisible(!TableCraft.isVisible() && !craft.isVisible());
            }
        }
        vueTerrain.miseAJourAffichage(ligneTile, colTile);
    }

    public void fabrication() {
        configurerBoutonCraft(tableDeCraft, "Table De Craft");
        configurerBoutonCraft(caisse, "Caisse En Bois");
    }
    public void fabricationDansTableDeFabrication() {
        configurerBoutonCraft(pioche, "Pioche");
        configurerBoutonCraft(pelle, "Pelle");
        configurerBoutonCraft(epee, "Épée");
    }
    private void configurerBoutonCraft(Button bouton, String nomObjet) {
        bouton.setOnMouseClicked(e -> {
            if (e.getButton() != MouseButton.PRIMARY) return;
            boolean aProcheTableCraft = TableCraft.isVisible();
            joueur.craft(nomObjet, aProcheTableCraft);
        });
    }
    private void apparitionObjets() {
        Objet objet = new Objet("Sabre Laser", "Un laser qui koupe !!!");
        VueObjet sabre = new VueObjet(objet, OBJET_SABRE_X, OBJET_SABRE_Y, TAILLE_OBJET_DROP, TAILLE_OBJET_DROP, "/Objet/lightSaberDrop.png");
        sabre.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                inventaire.ajouterObjet(sabre.getObjet(), 1);
                objetAffiche.getChildren().remove(sabre);
            }
        });
        objetAffiche.getChildren().add(sabre);
    }
    public void animationTimer() {
        gameTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate < FRAME_INTERVAL_NANOSEC) return;
                lastUpdate = now;
                env.unTour();
                gererDeplacements();
                gererSaut();
                gererMort();
                verifierDistanceTableCraft();
            }
        };
    }
    private void gererDeplacements() {
        if (touchesActives.contains(KeyCode.Q) || touchesActives.contains(KeyCode.LEFT)) {
            vueJoueur.affichage(1);
            joueur.deplacement(-1, 0);
        } else if (touchesActives.contains(KeyCode.D) || touchesActives.contains(KeyCode.RIGHT)) {
            vueJoueur.affichage(2);
            joueur.deplacement(1, 0);
        } else {
            vueJoueur.affichage(0);
        }
    }
    private void gererSaut() {
        if (touchesActives.contains(KeyCode.Z) || touchesActives.contains(KeyCode.UP) || touchesActives.contains(KeyCode.SPACE)) {
            joueur.demarrerSaut();
        }
    }
    private void gererMort(){
        ArrayList<Entite> morts = new ArrayList<>();
        for(Entite e : entites){
            if(e.estMort()){
                if(e.getClass() == Ennemis.class){
                    vueEnnemis.supprimerAffichage();
                }else vueJoueur.supprimerAffichage();
                morts.add(e);
            }
        }
        entites.removeAll(morts);
    }
    private void verifierDistanceTableCraft() {
        double distX = Math.abs(joueur.getX() / TAILLE_TUILE - terrain.getColId(TUILE_TABLE_CRAFT));
        double distY = Math.abs(joueur.getY() / TAILLE_TUILE - terrain.getLigneId(TUILE_TABLE_CRAFT));
        if (distX >= PORTEE_TABLE_CRAFT + 2 || distY >= PORTEE_TABLE_CRAFT + 2) {
            TableCraft.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        terrain = Terrain.getInstance();

        joueur = new Joueur(JOUEUR_POSITION_X_DEPART, JOUEUR_POSITION_Y_DEPART,
                JOUEUR_VIE_INITIALE, JOUEUR_VITESSE_BASE, inventaire);

        ennemis = new Ennemis(ENNEMI_POSITION_X_DEPART, ENNEMI_POSITION_Y_DEPART,
                ENNEMI_VIE_INITIALE, ENNEMI_VITESSE_BASE);

        entites = new ArrayList<>();
        entites.add(joueur);
        entites.add(ennemis);

        env = new Environnement(joueur);
        env.ajouterEnnemis(ennemis);

        vueTerrain = new VueTerrain(panneauDeJeu);
        vueCoeur = new VueCoeur(coeurs);
        vueJoueur = new VueJoueur(panneauJoueur);
        vueEnnemis = new VueEnnemis(panneauJoueur);

        vueJoueur.getImageView().translateXProperty().bind(joueur.getxProperty());
        vueJoueur.getImageView().translateYProperty().bind(joueur.getyProperty());
        vueEnnemis.getImageView().translateXProperty().bind(ennemis.getxProperty());
        vueEnnemis.getImageView().translateYProperty().bind(ennemis.getyProperty());

        joueur.vieProperty().addListener((obs, oldVal, newVal) -> {
            vueCoeur.mettreAJourAffichage(newVal.intValue());
        });
        vueCoeur.mettreAJourAffichage(joueur.getVie());

        ennemis.directionAnimationProperty().addListener((obs, oldVal, newVal) -> {
            vueEnnemis.affichage(newVal.intValue());
        });

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
            if (gameTimer == null) return;
            if (newVal) gameTimer.stop();
            else gameTimer.start();
        });

        if (!menu.isVisible() && gameTimer != null) gameTimer.start();
    }
}