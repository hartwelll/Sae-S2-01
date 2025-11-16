package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesTerrain.*;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesEntite.*;

/**
 * Cette class represente le joueur contrôlé par l'utilisateur.
 * Ne contient que la logique de jeu. Ne connaît pas la Vue.
 */
public class Joueur extends Entite {

    private Terrain map;
    private int hauteurJoueur;
    private int largeurJoueur;
    private Inventaire inventaire;
    private CraftingSystem craftingSystem;



    public Joueur(int x, int y, Terrain map, int vie, int v, Inventaire inv) {
        super(x, y, map, vie, v);
        this.map = map;
        this.hauteurJoueur = 60;
        this.largeurJoueur = 30;
        this.inventaire = inv;
        this.craftingSystem = new CraftingSystem();
    }

    public void deplacement(int dx, int dy){
        super.deplacement(dx, dy);
    }

    @Override
    public void decrementerVie(int vieAenlever) {
        super.decrementerVie(vieAenlever);
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

    public void poserBlock(int colTileClick, int ligneTileClick, boolean adjacent, Objet objetSelectionne){
        if (adjacent) {
            int idBlocCible = map.codeTuile(ligneTileClick, colTileClick);
            if (idBlocCible == TUILE_VIDE) {
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
                if(estDansPortee(getTileX(), getTileY(), colTileClick, ligneTileClick, PORTEE_TABLE_CRAFT)) {
                }
            }
        }
    }

    public void clicGauche(int colTile, int ligneTile, Environnement env){
        boolean peutCasser = estDansPortee(colTile, ligneTile, getTileX(), getTileY(), PORTEE_CASSER_BLOC);
        boolean aToucheEnnemi = false;

        for (Ennemis ennemi : env.getEnnemis()) {
            if (colTile == ennemi.getTileX() && ligneTile == ennemi.getTileY()) {
                attaque(ennemi, DEGATS_ATTAQUE_JOUEUR);
                aToucheEnnemi = true;
                break;
            }
        }

        if (!aToucheEnnemi) {
            casserBlock(colTile, ligneTile, peutCasser);
        }
    }

    public void clicDroit(int colTile, int ligneTile, Objet objetSelectionne){
        boolean peutPoser = estDansPortee(colTile, ligneTile, getTileX(), getTileY(), PORTEE_POSER_BLOC);
        poserBlock(colTile, ligneTile, peutPoser, objetSelectionne);
    }

    public boolean estDansPortee(int x1, int y1, int x2, int y2, int portee) {
        return Math.abs(x1 - x2) <= portee && Math.abs(y1 - y2) <= portee;
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

    public void craft(String nomObjet, boolean aProcheTableCraft){
        Objet objetCrafte = tenterCraft(nomObjet, aProcheTableCraft);

        if (objetCrafte != null) {
            inventaire.ajouterObjet(objetCrafte, 1);
            System.out.println("Crafté : " + objetCrafte.getNom());
        } else {
            System.out.println("Craft impossible : ingrédients manquants");
        }
    }
}