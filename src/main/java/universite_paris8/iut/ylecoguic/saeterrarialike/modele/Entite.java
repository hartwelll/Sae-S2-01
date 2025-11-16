package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Rectangle2D;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesEntite.*;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesTerrain.TAILLE_TUILE;

public abstract class Entite {

    private Terrain terrain; // Sera initialisé via le Singleton

    private IntegerProperty xProperty;
    private IntegerProperty yProperty;
    private int v;
    private int vDeBase;
    private int vSautInitial;
    private int vGravite;
    private int vy;
    private boolean sautEnCours;

    private boolean collision;
    private int hauteurEntite;
    private int largeurEntite;

    private IntegerProperty vieProperty;

    // Constructeur SIMPLIFIÉ : 'Terrain terrain' a été supprimé
    public Entite (int x, int y, int vie, int v){
        this.xProperty = new SimpleIntegerProperty(x);
        this.yProperty = new SimpleIntegerProperty(y);
        this.terrain = Terrain.getInstance(); // Utilise le Singleton
        this.v = v;
        this.vDeBase = v;
        this.vSautInitial = VITESSE_SAUT_INITIAL;
        this.vGravite = VITESSE_GRAVITE;
        this.collision = false;
        this.hauteurEntite = HAUTEUR_ENTITE;
        this.largeurEntite = LARGEUR_ENTITE;
        this.vy = 0;
        this.sautEnCours = false;
        this.vieProperty = new SimpleIntegerProperty(vie);
    }

    // ... (Toutes les autres méthodes de Entite restent inchangées) ...

    public void deplacement(int dx, int dy) {
        int nposx = getX() + v * dx;
        int nposy = getY();

        if (nposx < terrain.getMinXMap()) {
            nposx = terrain.getMinXMap();
        } else if (nposx + largeurEntite > terrain.getMaxXMap()) {
            nposx = terrain.getMaxXMap() - largeurEntite;
        }
        collisionDetectee(dx, dy, nposx, nposy);
        setV(vDeBase);
    }

    public void demarrerSaut() {
        if (collision && !sautEnCours) {
            this.vy = -vSautInitial;
            this.sautEnCours = true;
        }
    }

    public void appliquerMouvementVertical() {
        int nposx = getX();
        int nposy = getY() + vy;

        if (nposy < terrain.getMinYMap()) {
            nposy = terrain.getMaxYMap();
            vy = 0;
        } else if (nposy + hauteurEntite > terrain.getMaxYMap()) {
            nposy = terrain.getMaxYMap() - hauteurEntite;
            vy = 0;
            sautEnCours = false;
        }
        vy += vGravite;
        if (vy > VITESSE_VERTICALE_MAX) {
            vy = VITESSE_VERTICALE_MAX;
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
        Rectangle2D hitboxEntite = new Rectangle2D(nposx, nposy, largeurEntite, hauteurEntite);
        for (Rectangle2D hitboxBloc : terrain.getHitboxList()) {
            if (hitboxEntite.intersects(hitboxBloc)) {
                nposx = siCollisionX(dx, nposx, hitboxBloc);
                nposy = siCollisionY(dy, nposy, hitboxBloc);
                collision(true);
            }
        }
        for (Rectangle2D hitboxBloc : terrain.getHurtboxList()) {
            if (hitboxEntite.intersects(hitboxBloc)) {
                int vBarbele = v/2;
                decrementerVie(DEGATS_BARBELE);
                setV(vBarbele);
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

    public boolean collisionAvecEntite(Rectangle2D hitbox, Rectangle2D hitboxCible){
        if (hitbox.intersects(hitboxCible)){
            return true;
        }
        return false;
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
        for (Rectangle2D hitboxBloc : terrain.getHitboxList()) {
            if (hitboxSousEntite.intersects(hitboxBloc)) {
                return true;
            }
        }
        return false;
    }

    public void attaque(Entite cible, int dgt){
        Rectangle2D hitboxEntite = new Rectangle2D(getX(), getY(), hauteurEntite, largeurEntite);
        Rectangle2D hitboxCible = new Rectangle2D(cible.getX(), cible.getY(), hauteurEntite, largeurEntite);
        if (collisionAvecEntite(hitboxEntite, hitboxCible)) {
            cible.decrementerVie(dgt);
        }
    }

    public void decrementerVie(int vieAenlever) {
        if (getVie() > 0) {
            this.vieProperty.set(getVie() - vieAenlever);
            System.out.println(this.getVie());
        } else if (this.getClass().equals(Joueur.class)) {
            System.exit(0);
        }
    }

    public int getX() {
        return xProperty.getValue();
    }

    public int getY() {
        return yProperty.getValue();
    }

    public int getVie() {
        return vieProperty.get();
    }

    public IntegerProperty vieProperty() {
        return vieProperty;
    }

    public int getVGravite() {
        return vGravite;
    }

    public int getVSaut() {
        return vSautInitial;
    }

    public int getTileX() {
        return (getX() + (largeurEntite / 2)) / TAILLE_TUILE;
    }

    public int getTileY() {
        return (getY() + (hauteurEntite / 2)) / TAILLE_TUILE;
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
        return this.getVie() <= 0;
    }

    public void setV(int v) {
        this.v = v;
    }
}