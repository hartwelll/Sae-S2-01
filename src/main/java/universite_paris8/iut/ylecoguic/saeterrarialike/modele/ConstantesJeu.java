package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

/**
 * Constantes générales du jeu (timing, animations, etc...)
 */
public class ConstantesJeu {

    // Animation et timing
    public static final long FRAME_INTERVAL_NANOSEC = 16_666_666; // ~60 FPS
    public static final long DELAI_ATTAQUE_ENNEMI_NANOSEC = 600_000_000; // 0.6 secondes
    public static int DELAY = 0;


    // IDs pour les animations/sprites
    public static final int ANIMATION_ARRET = 0;
    public static final int ANIMATION_MARCHE_GAUCHE = 1;
    public static final int ANIMATION_MARCHE_DROITE = 2;

    // Dimensions d'affichage des objets
    public static final int TAILLE_COEUR = 60;
    public static final int TAILLE_OBJET_DROP = 32;

    // Positions d'objets spéciaux (exemple pour le sabre laser)
    public static final int OBJET_SABRE_X = 100;
    public static final int OBJET_SABRE_Y = 762;

    private ConstantesJeu() {

    }
}