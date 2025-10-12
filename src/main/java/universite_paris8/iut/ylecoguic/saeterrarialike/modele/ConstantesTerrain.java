package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

/**
 * Constantes liées au terrain et aux tuiles.
 */
public class ConstantesTerrain {

    // Dimensions des tuiles
    public static final int TAILLE_TUILE = 32;

    // Types de tuiles
    public static final int TUILE_VIDE = 0;
    public static final int TUILE_PIERRE = 1;
    public static final int TUILE_CAISSE_BOIS = 2;
    public static final int TUILE_BARBELE = 3;
    public static final int TUILE_TABLE_CRAFT = 4;
    public static final int TUILE_BOIS = 5;

    // Limites de la carte
    public static final int MIN_X_MAP = 0;
    public static final int MAX_X_MAP = 1854;
    public static final int MIN_Y_MAP = 0;
    public static final int MAX_Y_MAP = 1024;

    // Dimensions du terrain en tuiles
    public static final int NB_COLONNES = 60;
    public static final int NB_LIGNES = 33;


    private ConstantesTerrain() {

    }
}
