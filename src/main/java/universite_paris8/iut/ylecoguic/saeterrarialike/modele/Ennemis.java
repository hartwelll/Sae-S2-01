package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

public class Ennemis extends Joueur {

    private int direction;
    private int vMarche;
    private boolean enMarche;
    private int hauteurEnnemis;
    private int largeurEnnemis;

    public Ennemis(int x, int y, Map map) {
        super(x, y, map);
        this.direction = 1;
        this.vMarche = 2;
        this.enMarche = true;
        this.hauteurEnnemis = 60;
        this.largeurEnnemis = 30;
    }

    @Override
    public void deplacement(int dx, int dy) {
        super.deplacement(dx * vMarche, dy);
    }

    @Override
    public void collisionDetectee(int dx, int dy, int nposx, int nposy) {
        super.collisionDetectee(dx, dy, nposx, nposy);
        if (dx != 0) {
            changerDirection();
        }
    }

    public void changerDirection() {
        this.direction = -this.direction;
    }

    @Override
    public int getTileX() {
        return (getX() + (largeurEnnemis / 2)) / 32;
    }

    @Override
    public int getTileY() {
        return (getY() + (hauteurEnnemis / 2)) / 32;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getVMarche() {
        return vMarche;
    }

    public void setVMarche(int vMarche) {
        this.vMarche = vMarche;
    }

    public boolean isEnMarche() {
        return enMarche;
    }

    public void setEnMarche(boolean enMarche) {
        this.enMarche = enMarche;
    }
}

