package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Rectangle2D;

public class Entite {

    private IntegerProperty xProperty;
    private IntegerProperty yProperty;
    private int v;
    private int vSautInitial;
    private int vGravite;
    private int vy;
    private boolean sautEnCours;

    private boolean collision;
    private Map map;
    private int hauteurEntite;
    private int largeurEntite;
    private int vie;

    private final int minXMap = 0;
    private final int maxXMap = 1854;
    private final int minYMap = 0;
    private final int maxYMap = 1024;


    public Entite (int x, int y, Map map, int vie, int v){
        this.xProperty = new SimpleIntegerProperty(x);
        this.yProperty = new SimpleIntegerProperty(y);
        this.map = map;
        this.v = v; //vitesse horizale droite/gauchey)
        this.vSautInitial = 21;
        this.vGravite = 4;
        this.collision = false;
        this.hauteurEntite = 60;
        this.largeurEntite = 30;
        this.vy = 0; //vitesse en y(vertical) monte/descent
        this.sautEnCours = false;
        this.vie = vie;
    }

    public void deplacement(int dx, int dy) {
        int nposx = getX() + v * dx;  //nposx = nex position
        int nposy = getY();

        if (nposx < minXMap) {
            nposx = minXMap;
        } else if (nposx + largeurEntite > maxXMap) {
            nposx = maxXMap - largeurEntite;
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
        } else if (nposy + hauteurEntite > maxYMap) {
            nposy = maxYMap - hauteurEntite;
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
        Rectangle2D hiboxEntite = new Rectangle2D(nposx, nposy, largeurEntite, hauteurEntite);
        for (Rectangle2D hitboxBloc : map.getHitboxList()) {
            if (hiboxEntite.intersects(hitboxBloc)) {
                nposx = siCollisionX(dx, nposx, hitboxBloc);
                nposy = siCollisionY(dy, nposy, hitboxBloc);
                collision(true);
            }
        }
        for (Rectangle2D hitboxBloc : map.getHurtboxList()) {
            if (hiboxEntite.intersects(hitboxBloc)) {
                decrementerVie(1);
                nposx = siCollisionX(dx, nposx, hitboxBloc);
                nposy = siCollisionY(dy, nposy, hitboxBloc);
                collision(true);
            }
        }
        xProperty.set(nposx);
        yProperty.set(nposy);
    }

    public boolean collision(boolean collision){
        return this.collision = collision;
    }

    public int siCollisionX(int dx, int nposx, Rectangle2D hitboxBloc){
        if (dx != 0) {
            if (dx > 0) {
                nposx = (int) (hitboxBloc.getMinX() - largeurEntite);
            } else {
                nposx = (int) (hitboxBloc.getMaxX());
            }
        }
        return nposx;
    }

    public int siCollisionY(int dy, int nposy, Rectangle2D hitboxBloc){
        if (dy != 0) {
            if (dy > 0) {
                nposy = (int) (hitboxBloc.getMinY() - hauteurEntite);
                vy = 0;
                sautEnCours = false;
            } else {
                nposy = (int) (hitboxBloc.getMaxY());
                vy = 0;
            }
        }
        return nposy;
    }

    public boolean estSurLeSol() {
        Rectangle2D hitboxSousEntite = new Rectangle2D(this.getX(), getY() + hauteurEntite + 1, largeurEntite, 1);
        for (Rectangle2D hitboxBloc : map.getHitboxList()) {
            if (hitboxSousEntite.intersects(hitboxBloc)) {
                return true;
            }
        }
        return false;
    }

    public void attaque(Entite cible, boolean adjacent){
        if (adjacent) {
            cible.decrementerVie(10);
        }
    }

    public boolean decrementerVie(int vieAenlever) {
        if (this.vie > 0) {
            this.vie -= vieAenlever;
        }
        System.out.println(vie);
        return true;
    }

    public int getX() {
        return xProperty.getValue();
    }

    public int getY() {
        return yProperty.getValue();
    }

    public int getVie() {
        return vie;
    }

    public int getVGravite() {
        return vGravite;
    }

    public int getVSaut() {
        return vSautInitial;
    }

    public int getTileX() {
        return (getX() + (largeurEntite / 2)) / 32;
    }

    public int getTileY() {
        return (getY() + (hauteurEntite / 2)) / 32;
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

    public boolean estMort() {
        return this.vie <= 0;
    }

    public void setV(int v) {
        this.v = v;
    }
}
