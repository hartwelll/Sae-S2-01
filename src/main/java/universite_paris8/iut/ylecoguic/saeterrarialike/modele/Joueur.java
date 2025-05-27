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

    private Map map;

    private int hauteurJoueur;
    private int largeurJoueur;
    private int vie;

    private boolean sautEnCours;
    private int vy;

    public Joueur(int x, int y, Map map) {
        this.xProperty = new SimpleIntegerProperty(x);
        this.yProperty = new SimpleIntegerProperty(y);
        this.v = 8;
        this.vSautInitial = 21;
        this.vGravite = 4;
        this.map = map;
        this.hauteurJoueur = 60;
        this.largeurJoueur = 32;
        this.vie = 100;
        this.vy = 0;
        this.sautEnCours = false;
    }

    public void deplacement(int dx, int dy) {
        int nposx = getX() + v * dx;
        int nposy = getY();
        collisionDetectee(dx, dy, nposx, nposy);
    }

    public void demarrerSaut() {
        if (estSurLeSol() && !sautEnCours) {
            this.vy = -vSautInitial;
            this.sautEnCours = true;
        }
    }

    public void appliquerMouvementVertival() {
        int nposx = getX();
        int nposy = getY() + vy;
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
            }
        }
        for (Rectangle2D hitboxBloc : map.getHurtboxList()) {
            if (hitboxJoueur.intersects(hitboxBloc)) {
                decrementerVie(1);
                nposx = siCollisionX(dx, nposx, hitboxBloc);
                nposy = siCollisionY(dy, nposy, hitboxBloc);
            }
        }
        xProperty.set(nposx);
        yProperty.set(nposy);
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

    /*public boolean collision(int nposx, int nposy) {
        Rectangle2D hitboxJoueur = new Rectangle2D(nposx, nposy, largeurJoueur, hauteurJoueur);
        for (Rectangle2D hitboxBloc : map.getHitboxList()) {
            if (hitboxJoueur.intersects(hitboxBloc)) {
                return true;
            }
        }
        return false;
    }*/

    public void decrementerVie(int vie) {
        System.out.println(this.vie);
        if (this.vie > 0) {
            this.vie -= vie;
        } else {
            System.out.println("Game Over! Vie: " + this.vie);
            System.exit(0);
        }
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
}