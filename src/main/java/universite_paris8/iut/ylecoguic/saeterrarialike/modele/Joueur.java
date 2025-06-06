package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Rectangle2D;

public class Joueur {

    private IntegerProperty xProperty;
    private IntegerProperty yProperty;

    private int v;
    private int vSautInitial;
    private int vGravite;
    private boolean collision;

    private Map map;

    private int hauteurJoueur;
    private int largeurJoueur;
    private int vie;

    private boolean sautEnCours;
    private int vy;

    private final int minXMap = 0;
    private final int maxXMap = 1824;
    private final int minYMap = 0;
    private final int maxYMap = 1024;

    public Joueur(int x, int y, Map map) {
        this.xProperty = new SimpleIntegerProperty(x);
        this.yProperty = new SimpleIntegerProperty(y);
        this.map = map;
        this.v = 8;
        this.vSautInitial = 21;
        this.vGravite = 4;
        this.collision = false;
        this.hauteurJoueur = 60;
        this.largeurJoueur = 32;
        this.vie = 100;
        this.vy = 0;
        this.sautEnCours = false;
    }

    public void deplacement(int dx, int dy) {
        int nposx = getX() + v * dx;
        int nposy = getY();

        if (nposx < minXMap) {
            nposx = minXMap;
        } else if (nposx + largeurJoueur > maxXMap) {
            nposx = maxXMap - largeurJoueur;
        }

        collisionDetectee(dx, dy, nposx, nposy);
    }

    public void demarrerSaut() {
        if (collision && !sautEnCours) {
            this.vy = -vSautInitial;
            this.sautEnCours = true;
        }
    }

    public void appliquerMouvementVertival() {
        int nposx = getX();
        int nposy = getY() + vy;

        if (nposy < minYMap) {
            nposy = minYMap;
            vy = 0;
        } else if (nposy + hauteurJoueur > maxYMap) {
            nposy = maxYMap - hauteurJoueur;
            vy = 0;
            sautEnCours = false;
        }

        vy += vGravite;
        if (vy > 20) {
            vy = 20;
        }
        if (vy > 0){
            collisionDetectee(0, 1, nposx, nposy);
        } else if (vy < 0) {
            collisionDetectee(0, -1, nposx, nposy);
        }
        if (estSurLeSol() && vy >= 0) {
            vy = 0;
            sautEnCours = false;
        }
    }

    public void collisionDetectee(int dx, int dy, int nposx, int nposy) {
        Rectangle2D hitboxJoueur = new Rectangle2D(nposx, nposy, largeurJoueur, hauteurJoueur);
        for (Rectangle2D hitboxBloc : map.getHitboxList()) {
            if (hitboxJoueur.intersects(hitboxBloc)) {
                nposx = siCollisionX(dx, nposx, hitboxBloc);
                nposy = siCollisionY(dy, nposy, hitboxBloc);
                collision(true);
            }
        }
        for (Rectangle2D hitboxBloc : map.getHurtboxList()) {
            if (hitboxJoueur.intersects(hitboxBloc)) {
                decrementerVie();
                nposx = siCollisionX(dx, nposx, hitboxBloc);
                nposy = siCollisionY(dy, nposy, hitboxBloc);
                collision(true);
            }
        }
        /*if (nposx <= 0 || nposx >= 1792){
            setV(0);
        }
        if (nposy <= 0){
            setvSautInitial(0);
        }*/
        xProperty.set(nposx);
        yProperty.set(nposy);
    }

    public boolean collision(boolean collision){
        return this.collision = collision;
    }

    public int siCollisionX(int dx, int nposx, Rectangle2D hitboxBloc){
        if (dx != 0) {
            if (dx > 0) {
                nposx = (int) (hitboxBloc.getMinX() - largeurJoueur);
            } else {
                nposx = (int) (hitboxBloc.getMaxX());
            }
        }
        return nposx;
    }

    public int siCollisionY(int dy, int nposy, Rectangle2D hitboxBloc){
        if (dy != 0) {
            if (dy > 0) {
                nposy = (int) (hitboxBloc.getMinY() - hauteurJoueur);
                vy = 0;
                sautEnCours = false;
            } else {
                nposy = (int) (hitboxBloc.getMaxY());
                vy = 0;
            }
        }
        return nposy;
    }

    public boolean decrementerVie() {
        if (this.vie > 0) {
            this.vie -= 1;
        } else {
            System.out.println("Game Over! Vie: " + this.vie);
            System.exit(0);
        }
        System.out.println(this.vie);
        return true;
    }

    public boolean estSurLeSol() {
        Rectangle2D hitboxSousJoueur = new Rectangle2D(getX(), getY() + hauteurJoueur + 1, largeurJoueur, 1);
        for (Rectangle2D hitboxBloc : map.getHitboxList()) {
            if (hitboxSousJoueur.intersects(hitboxBloc)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSautEnCours() {
        return sautEnCours;
    }

    public void setSautEnCours(boolean sautEnCours) {
        this.sautEnCours = sautEnCours;
    }

    public IntegerProperty getxProperty() {
        return xProperty;
    }

    public IntegerProperty getyProperty() {
        return yProperty;
    }

    public int getX() {
        return xProperty.getValue();
    }

    public int getY() {
        return yProperty.getValue();
    }

    public int getVGravite() {
        return vGravite;
    }

    public int getVSaut() {
        return vSautInitial;
    }

    public int getVie() {
        return vie;
    }

    public int getTileX() {
        return (getX() + (largeurJoueur / 2)) / 32;
    }

    public int getTileY() {
        return (getY() + (hauteurJoueur / 2)) / 32;
    }
}