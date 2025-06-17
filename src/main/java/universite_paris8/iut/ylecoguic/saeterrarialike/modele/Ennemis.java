package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

public class Ennemis extends Entite {

    private int direction;
    private int vMarche;
    private boolean enMarche;
    private int hauteurEnnemis;
    private int largeurEnnemis;

    public Ennemis(int x, int y, Map map, int vie) {
        super(x, y, map, vie, 4);
        this.vMarche = 4;
        this.direction = 1;
        this.enMarche = true;
        this.hauteurEnnemis = 60;
        this.largeurEnnemis = 30;
    }

    public void deplacement() {
        int dx = (int) (Math.random()*3);
        int dy = (int) (Math.random()*2);
        if(dx == 2){
            dx = -1;
        }
        super.deplacement(dx, dy);
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

