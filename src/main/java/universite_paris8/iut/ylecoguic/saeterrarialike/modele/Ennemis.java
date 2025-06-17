package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

public class Ennemis extends Entite {

    private boolean enMarche;
    private int hauteurEnnemis;
    private int largeurEnnemis;

    public Ennemis(int x, int y, Map map, int vie, int v) {
        super(x, y, map, vie, v);
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
    public int getTileX() {
        return (getX() + (largeurEnnemis / 2)) / 32;
    }

    @Override
    public int getTileY() {
        return (getY() + (hauteurEnnemis / 2)) / 32;
    }

    public boolean isEnMarche() {
        return enMarche;
    }

    public void setEnMarche(boolean enMarche) {
        this.enMarche = enMarche;
    }
}

