package universite_paris8.iut.ylecoguic.saeterrarialike.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.*;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.*;

import java.net.URL;
import java.util.*;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesEntite.*;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesTerrain.*;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesJeu.*;

/**
 * Contrôleur principal du jeu Terraria-like.
 * Gère :
 * - le modèle et les vues
 * - les entrées clavier/souris
 * - la boucle de jeu (AnimationTimer)
 * - les menus, craft et interactions
 */
public class Controller implements Initializable {

    // --- FXML ---
    @FXML private Pane menu, panneauJoueur, craft, tuto, consignes, tableCraftPane, objetAffiche;
    @FXML private TilePane panneauDeJeu;
    @FXML private HBox coeurs;
    @FXML private TableView<Objet> inventaireTable;
    @FXML private TableColumn<Objet, String> nomCol, descCol, quantCol;
    @FXML private Button pioche, pelle, epee, tableDeCraft, caisse;

    // --- Modèle et vues ---
    private Environnement env;
    private Terrain terrain;
    private VueTerrain vueTerrain;
    private Joueur joueur;
    private VueJoueur vueJoueur;
    private VueCoeur vueCoeur;
    private VueEnnemis vueEnnemis;
    private Ennemis ennemis;
    private final Inventaire inventaire = new Inventaire();
    private Set<KeyCode> touchesActives;
    private AnimationTimer gameTimer;

    // =============================
    // ========== MÉTHODES =========
    // =============================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiserModeleEtVues();
        configurerInterface();
        configurerInventaireTable();
        apparaitreObjets();
        setupInput();
        demarrerBoucleJeu();
    }

    // --- Initialisation du modèle et des vues ---
    private void initialiserModeleEtVues() {
        env = new Environnement();
        terrain = new Terrain();
        vueTerrain = new VueTerrain(panneauDeJeu, terrain);
        vueCoeur = new VueCoeur(coeurs);

        Coeur coeur = new Coeur(vueCoeur);
        vueJoueur = new VueJoueur(panneauJoueur);
        joueur = new Joueur(
                JOUEUR_POSITION_X_DEPART, JOUEUR_POSITION_Y_DEPART,
                terrain, JOUEUR_VIE_INITIALE, JOUEUR_VITESSE_BASE,
                inventaire, inventaireTable, craft, tableCraftPane,
                vueCoeur, coeur, vueJoueur
        );

        vueJoueur.getImageView().translateXProperty().bind(joueur.getxProperty());
        vueJoueur.getImageView().translateYProperty().bind(joueur.getyProperty());

        vueEnnemis = new VueEnnemis(panneauJoueur);
        ennemis = new Ennemis(
                ENNEMI_POSITION_X_DEPART, ENNEMI_POSITION_Y_DEPART,
                terrain, ENNEMI_VIE_INITIALE, ENNEMI_VITESSE_BASE, vueEnnemis
        );

        vueEnnemis.getImageView().translateXProperty().bind(ennemis.getxProperty());
        vueEnnemis.getImageView().translateYProperty().bind(ennemis.getyProperty());
    }

    private void configurerInterface() {
        craft.setVisible(false);
        tableCraftPane.setVisible(false);
        tuto.setVisible(false);
        menu.setVisible(false);
        touchesActives = new HashSet<>();
    }

    private void configurerInventaireTable() {
        nomCol.setCellValueFactory(cell -> cell.getValue().nomProperty());
        descCol.setCellValueFactory(cell -> cell.getValue().descProperty());
        quantCol.setCellValueFactory(cell -> cell.getValue().quantiteProperty().asObject().asString());
        inventaireTable.setItems(inventaire.getObjets());
    }

    // --- Gestion du menu ---
    public void retourJeu() {
        menu.setVisible(false);
        if (gameTimer != null) gameTimer.start();
    }

    public void afficherTuto() {
        tuto.setVisible(true);
        menu.setVisible(false);
        if (gameTimer != null) gameTimer.start();
    }

    public void quitterPartie() {
        System.exit(0);
    }

    // --- Gestion des entrées clavier / souris ---
    private void setupInput() {
        panneauDeJeu.sceneProperty().addListener((obs, oldScene, sceneActuel) -> {
            if (sceneActuel == null) return;

            sceneActuel.setOnKeyPressed(event -> {
                touchesActives.add(event.getCode());
                if (event.getCode() == KeyCode.C)
                    craft.setVisible(!craft.isVisible() && !tableCraftPane.isVisible());
                else if (event.getCode() == KeyCode.ESCAPE)
                    gererEscape();
            });

            sceneActuel.setOnKeyReleased(event -> touchesActives.remove(event.getCode()));
            sceneActuel.setOnMouseClicked(this::gererClicSouris);
        });
    }

    private void gererEscape() {
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

    private void gererClicSouris(MouseEvent event) {
        int col = (int) (event.getX() / TAILLE_TUILE);
        int ligne = (int) (event.getY() / TAILLE_TUILE);
        int joueurX = joueur.getTileX();
        int joueurY = joueur.getTileY();

        boolean cassePossible = estDansPortee(col, ligne, joueurX, joueurY, PORTEE_CASSER_BLOC);
        boolean posePossible = estDansPortee(col, ligne, joueurX, joueurY, PORTEE_POSER_BLOC);

        if (event.getButton() == MouseButton.PRIMARY)
            gererClicGauche(col, ligne, cassePossible);
        else if (event.getButton() == MouseButton.SECONDARY)
            joueur.poserBlock(col, ligne, posePossible);

        vueTerrain.miseAJourAffichage(ligne, col);
    }

    private void gererClicGauche(int col, int ligne, boolean cassePossible) {
        if (col == ennemis.getTileX() && ligne == ennemis.getTileY())
            joueur.attaque(ennemis, DEGATS_ATTAQUE_JOUEUR);
        else
            joueur.casserBlock(col, ligne, cassePossible);
    }

    private boolean estDansPortee(int col, int ligne, int x, int y, int portee) {
        return Math.abs(col - x) <= portee && Math.abs(ligne - y) <= portee;
    }

    // --- Gestion du craft ---
    public void fabrication() {
        configurerBoutonCraft(tableDeCraft, "Table De Craft");
        configurerBoutonCraft(caisse, "Caisse En Bois");
    }

    public void fabricationDansTable() {
        configurerBoutonCraft(pioche, "Pioche");
        configurerBoutonCraft(pelle, "Pelle");
        configurerBoutonCraft(epee, "Épée");
    }

    private void configurerBoutonCraft(Button bouton, String nomObjet) {
        bouton.setOnMouseClicked(e -> {
            if (e.getButton() != MouseButton.PRIMARY) return;
            boolean procheTable = tableCraftPane.isVisible();
            Objet craft = joueur.tenterCraft(nomObjet, procheTable);

            if (craft != null) {
                inventaire.ajouterObjet(craft, 1);
                System.out.println("Crafté : " + craft.getNom());
            } else {
                System.out.println("Craft impossible : ingrédients manquants");
            }
        });
    }

    // --- Apparition d’un objet spécial ---
    private void apparaitreObjets() {
        Objet sabre = new Objet("Sabre Laser", "Un laser qui koupe !!! ");
        VueObjet vueSabre = new VueObjet(sabre, OBJET_SABRE_X, OBJET_SABRE_Y,
                TAILLE_OBJET_DROP, TAILLE_OBJET_DROP, "/Objet/lightSaberDrop.png");

        vueSabre.setOnMouseClicked(e -> {
            if (e.getButton() != MouseButton.PRIMARY) return;
            System.out.println("Ajout à l'inventaire : " + sabre.getNom());
            inventaire.ajouterObjet(sabre, 1);
            objetAffiche.getChildren().remove(vueSabre);
        });

        objetAffiche.getChildren().add(vueSabre);
    }

    // --- Boucle de jeu principale ---
    private void demarrerBoucleJeu() {
        gameTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate < FRAME_INTERVAL_NANOSEC) return;

                env.unTour();
                gererDeplacements();
                gererSaut();
                verifierProximiteTableCraft();
                lastUpdate = now;
            }
        };

        menu.visibleProperty().addListener((obs, oldVal, visible) -> {
            if (visible) gameTimer.stop();
            else gameTimer.start();
        });

        gameTimer.start();
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

    private void verifierProximiteTableCraft() {
        boolean tropLoin = Math.abs(joueur.getX() / TAILLE_TUILE - terrain.getColId(TUILE_TABLE_CRAFT)) >= PORTEE_TABLE_CRAFT + 2
                || Math.abs(joueur.getY() / TAILLE_TUILE - terrain.getLigneId(TUILE_TABLE_CRAFT)) >= PORTEE_TABLE_CRAFT + 2;
        if (tropLoin) tableCraftPane.setVisible(false);
    }
}
