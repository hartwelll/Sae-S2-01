package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Rectangle2D;

public class Joueur {

    private IntegerProperty xProperty;
    private IntegerProperty yProperty;
    private int v;
    private int vSaut;
    private int vGravite;
    private Map map;
    private int hauteurJoueur;
    private int largeurJoueur;

    public Joueur(int x, int y, Map map){
        this.xProperty = new SimpleIntegerProperty(x);
        this.yProperty = new SimpleIntegerProperty(y);
        this.v = 8;
        this.vSaut = 100;
        this.vGravite = 4;
        this.map = map;
        hauteurJoueur = 64;
        largeurJoueur = 32;
    }

    public void deplacement(int dx, int dy){
        int nposx = getX() + v * dx;
        int nposy = getY() + v * dy;

        Rectangle2D hitboxJoueur = new Rectangle2D(nposx, nposy, largeurJoueur, hauteurJoueur);
        boolean collisionDetectee = false;

        //creer une nouvelle méthode pour opti la duplication de code et enlever le break parce que c'est moche
        for(Rectangle2D hitboxBloc : map.getHitboxList()) {
            if (hitboxJoueur.intersects(hitboxBloc)) {
                collisionDetectee = true;
                if (dx != 0) {
                    if (dx > 0) {
                        nposx = (int) (hitboxBloc.getMinX() - largeurJoueur);
                    } else {
                        nposx = (int) (hitboxBloc.getMaxX());
                    }
                }
                break;//temporaire
            }
        }
        xProperty.set(nposx);
    }

    public void saut(int dx, int dy){
        int nposx = getX() + v * dx;
        int nposy = getY() - vSaut * dy;

        Rectangle2D hitboxJoueur = new Rectangle2D(nposx, nposy, largeurJoueur, hauteurJoueur);
        boolean collisionDetectee = false;

        //creer une nouvelle méthode pour opti la duplication de code et enlever le break parce que c'est moche
        for(Rectangle2D hitboxBloc : map.getHitboxList()) {
            if (hitboxJoueur.intersects(hitboxBloc)) {
                collisionDetectee = true;
                if (dy != 0) {
                    if (dy > 0) {
                        nposy = (int) (hitboxBloc.getMinY());
                    }
                }
                break;//temporaire
            }
        }
        yProperty.set(nposy);
    }

    public void gravite(int dx, int dy){
        int nposx = getX() + v * dx;
        int nposy = getY() - vGravite * dy;

        Rectangle2D nouvelleHitbox = new Rectangle2D(nposx, nposy, largeurJoueur, hauteurJoueur);

        boolean collisionDetectee = false;
        for(Rectangle2D hitboxBloc : map.getHitboxList()) {
            if (nouvelleHitbox.intersects(hitboxBloc)) {
                collisionDetectee = true;
                if (dy < 0) {
                    nposy = (int) (hitboxBloc.getMinY() - hauteurJoueur);
                }
                break;
            }
        }
        yProperty.set(nposy);
    }

    public boolean collision(int nposx, int nposy){
        Rectangle2D hitboxJoueur = new Rectangle2D(nposx, nposy, largeurJoueur, hauteurJoueur);
        for(Rectangle2D hitboxBloc : map.getHitboxList()) {
            if (hitboxJoueur.intersects(hitboxBloc)) {
                return true;
            }
        }
        return false;
    }

    public IntegerProperty getxProperty() {
        return xProperty;
    }

    public IntegerProperty getyProperty(){
        return yProperty;
    }

    public int getX(){
        return xProperty.getValue();
    }

    public int getY(){
        return yProperty.getValue();
    }

    public int getVGravite() {
        return vGravite;
    }

    public void setV(int v) {
        this.v = v;
    }
}
