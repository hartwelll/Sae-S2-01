package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

/**
 * Constantes liées aux entités (joueur, ennemis).
 */
public class ConstantesEntite {

    // Dimensions des entités
    public static final int HAUTEUR_ENTITE = 60;
    public static final int LARGEUR_ENTITE = 30;

    // Dimensions pour l'affichage
    public static final int HAUTEUR_SPRITE = 64;
    public static final int LARGEUR_SPRITE = 32;

    // Physique - Gravité et saut
    public static final int VITESSE_GRAVITE = 4;
    public static final int VITESSE_SAUT_INITIAL = 21;
    public static final int VITESSE_VERTICALE_MAX = 20;

    // Joueur
    public static final int JOUEUR_VIE_INITIALE = 100;
    public static final int JOUEUR_VITESSE_BASE = 8;
    public static final int JOUEUR_POSITION_X_DEPART = 500;
    public static final int JOUEUR_POSITION_Y_DEPART = 725;

    // Ennemis
    public static final int ENNEMI_VIE_INITIALE = 50;
    public static final int ENNEMI_VITESSE_BASE = 4;
    public static final int ENNEMI_POSITION_X_DEPART = 500;
    public static final int ENNEMI_POSITION_Y_DEPART = 625;
    public static final int ENNEMI_DISTANCE_VISION = 15;
    public static final int ENNEMI_DEGATS_ATTAQUE = 2;

    // Combat
    public static final int DEGATS_ATTAQUE_JOUEUR = 10;
    public static final int DEGATS_BARBELE = 2;

    // Portées d'interaction
    public static final int PORTEE_CASSER_BLOC = 1;
    public static final int PORTEE_POSER_BLOC = 2;
    public static final int PORTEE_TABLE_CRAFT = 2;

    // Vie et coeurs
    public static final int VIE_PAR_COEUR = 10;
    public static final int NOMBRE_COEURS_MAX = 10;

    private ConstantesEntite() {

    }
}