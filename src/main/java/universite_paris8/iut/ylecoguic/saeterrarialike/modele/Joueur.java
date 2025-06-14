package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Rectangle2D;

public class Joueur extends Entite {

    private IntegerProperty xProperty;
    private IntegerProperty yProperty;
    private int v;
    private int vSautInitial;
    private int vGravite;
    private boolean collision;
    private Map map;
    private int hauteurJoueur;
    private int largeurJoueur;
    private boolean sautEnCours;
    private int vy;
    private final int minXMap = 0;
    private final int maxXMap = 1824;
    private final int minYMap = 0;
    private final int maxYMap = 1024;

    public Joueur(int x, int y, Map map, int vie) {
        super(x, y, map, vie);
        this.v = 8; //vitesse horizale droite/gauchey)
        this.hauteurJoueur = 60;
        this.largeurJoueur = 30;
    }

    public boolean decrementerVie() {
        super.decrementerVie();
         if(this.getVie() <= 0) {
            System.exit(0);
        }
        return true;
    }
}