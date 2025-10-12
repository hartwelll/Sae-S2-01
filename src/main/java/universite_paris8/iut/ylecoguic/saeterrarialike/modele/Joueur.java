package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueCoeur;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueJoueur;

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
            if (idBloc != 0 && idBloc != 3) {
                Objet objetCasse = creerObjetDepuisBloc(idBloc);
                if (objetCasse != null) {
                    nbAajouter = 1;
                    if(idBloc == 2){
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
            if (idBlocCible == 0) {
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
            } else if (idBlocCible == 4) {
                if(Math.abs(getX() / 32 - map.getColId(4)) <= 2 && Math.abs(getY() / 32 - map.getLigneId(4)) <= 2) {
                    TableCraft.setVisible(!TableCraft.isVisible() && !craft.isVisible());
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

    public Objet creerObjetDepuisBloc(int idBloc) {
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

    public Objet tenterCraft(String nomObjet, boolean aProcheTableCraft) {
        return craftingSystem.crafter(nomObjet, inventaire, aProcheTableCraft);
    }
}