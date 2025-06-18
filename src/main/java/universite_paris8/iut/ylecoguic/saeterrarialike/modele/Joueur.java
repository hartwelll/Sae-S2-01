package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Rectangle2D;

public class Joueur extends Entite {

    private Map map;
    private int hauteurJoueur;
    private int largeurJoueur;

    public Joueur(int x, int y, Map map, int vie, int v) {
        super(x, y, map, vie, v);
        this.hauteurJoueur = 60;
        this.largeurJoueur = 30;
    }

    public boolean decrementerVie() {
        super.decrementerVie(1);
         if(this.getVie() <= 0) {
            System.exit(0);
        }
        return true;
    }


}