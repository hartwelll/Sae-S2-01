package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueCoeur;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueJoueur;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesTerrain.*;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesEntite.*;


import java.util.ArrayList;

/**
 * Cette class represente le joueur contrôlé par l'utilisateur.
 * Responsabilités :
 * - Casser et poser des blocs dans le terrain
 * - Gérer l'inventaire et le crafting
 * - Interagir avec les objets du monde (table de craft, etc.)
 * - Convertir entre objets et IDs de blocs
 */
public class Joueur extends Entite {

    private Terrain map;
    private int hauteurJoueur;
    private int largeurJoueur;
    private Inventaire inventaire;
    private VueCoeur vueCoeur;
    private VueJoueur vueJoueur;
    private Coeur coeur;
    private TableView<Objet> inventaireTable;
    Pane craft;
    Pane TableCraft;
    private CraftingSystem craftingSystem;

    public Joueur(int x, int y, Terrain map, int vie, int v, Inventaire inv, TableView<Objet> inventaireTable, Pane craft, Pane TableCraft, VueCoeur vueCoeur, Coeur coeur, VueJoueur vuejoueur) {
        super(x, y, map, vie, v);
        this.map = map;
        this.hauteurJoueur = 60;
        this.largeurJoueur = 30;
        this.inventaire = inv;
        this.vueCoeur = vueCoeur;
        this.vueJoueur = vuejoueur;
        this.coeur = coeur;
        this.inventaireTable = inventaireTable;
        this.craft = craft;
        this.TableCraft = TableCraft;
        this.craftingSystem = new CraftingSystem();
    }

    public void deplacement(int dx, int dy, int id){
        vueJoueur.affichage(id);
        super.deplacement(dx, dy);
    }

    public void decrementerVie(int vieAenlever) {
        super.decrementerVie(vieAenlever);
        vueCoeur.enleverCoeurVue(this, coeur);
    }

    public void casserBlock(int colTileClick, int ligneTileClick, boolean adjacent){
        int nbAajouter;
        if (adjacent) {
            int idBloc = map.codeTuile(ligneTileClick, colTileClick);
            if (idBloc != TUILE_VIDE && idBloc != TUILE_BARBELE) {
                Objet objetCasse = creerObjetDepuisBloc(idBloc);
                if (objetCasse != null) {
                    nbAajouter = 1;
                    if(idBloc == TUILE_CAISSE_BOIS){
                        nbAajouter = 2;
                    }
                    inventaire.ajouterObjet(objetCasse, nbAajouter);
                }
                map.setCase(ligneTileClick, colTileClick, 0);
            }
        }
    }

    public void poserBlock(int colTileClick, int ligneTileClick, boolean adjacent){
        if (adjacent) {
            int idBlocCible = map.codeTuile(ligneTileClick, colTileClick);
            if (idBlocCible == TUILE_VIDE) {
                Objet objetSelectionne = inventaireTable.getSelectionModel().getSelectedItem();
                if (objetSelectionne != null) {
                    if (objetSelectionne.getQuantite() > 0) {
                        int idBlocAPoser = getIdBlocDepuisObjet(objetSelectionne);
                        if (idBlocAPoser != 0) {
                            inventaire.supprimerObjet(objetSelectionne, 1);
                            map.creeCase(ligneTileClick, colTileClick, idBlocAPoser);
                        }
                    }
                }
            } else if (idBlocCible == TUILE_TABLE_CRAFT) {
                if(Math.abs(getX() / TAILLE_TUILE - map.getColId(TUILE_TABLE_CRAFT)) <= PORTEE_TABLE_CRAFT && Math.abs(getY() / TAILLE_TUILE - map.getLigneId(TUILE_TABLE_CRAFT)) <= PORTEE_TABLE_CRAFT) {
                    TableCraft.setVisible(!TableCraft.isVisible() && !craft.isVisible());
                }
            }
        }
    }


    private int getIdBlocDepuisObjet(Objet objet) {
        switch (objet.getNom()) {
            case "Pierre":
                return TUILE_PIERRE;
            case "Caisse En Bois":
                return TUILE_CAISSE_BOIS;
            case "Table De Craft":
                return TUILE_TABLE_CRAFT;
            default:
                return TUILE_VIDE;
        }
    }

    public Objet creerObjetDepuisBloc(int idBloc) {
        switch (idBloc) {
            case TUILE_PIERRE:
                return new Objet("Pierre", "De la pierre");
            case TUILE_CAISSE_BOIS, TUILE_BOIS:
                return new Objet("Bois", "Du bois");
            case TUILE_TABLE_CRAFT:
                return new Objet("Table De Craft", "une simple table de craft");
            default:
                return null;
        }
    }

    public Objet tenterCraft(String nomObjet, boolean aProcheTableCraft) {
        return craftingSystem.crafter(nomObjet, inventaire, aProcheTableCraft);
    }
}