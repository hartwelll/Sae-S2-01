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
        hauteurJoueur = 63;
        largeurJoueur = 32;
    }

    public void deplacement(int dx, int dy){
        int nposx = getX() + v * dx;
        int nposy = getY() + v * dy;
        collisionDetectee(dx, dy, nposx, nposy);
    }

    public void saut(int dx, int dy){
        int nposx = getX() + v * dx;
        int nposy = getY() - vSaut * dy;
        collisionDetectee(dx, dy, nposx, nposy);
    }

    public void gravite(int dx, int dy){
        int nposx = getX() + v * dx;
        int nposy = getY() - vGravite * dy;
        collisionDetectee(dx, dy, nposx, nposy);
    }

    public void collisionDetectee(int dx, int dy, int nposx, int nposy){
        Rectangle2D hitboxJoueur = new Rectangle2D(nposx, nposy, largeurJoueur, hauteurJoueur);
        for(Rectangle2D hitboxBloc : map.getHitboxList()) {
            if (hitboxJoueur.intersects(hitboxBloc)) {
                if (dx != 0) {
                    if (dx > 0) {
                        nposx = (int) (hitboxBloc.getMinX() - largeurJoueur);
                    } else {
                        nposx = (int) (hitboxBloc.getMaxX());
                    }

                } else if (dy != 0) {
                    if (dy > 0) {
                        nposy = (int) (hitboxBloc.getMinY());
                    } else {
                        nposy = (int) (hitboxBloc.getMinY() - hauteurJoueur);
                    }

                }
            }
        }
        xProperty.set(nposx);
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

    public int getvSaut() {
        return vSaut;
    }
}
