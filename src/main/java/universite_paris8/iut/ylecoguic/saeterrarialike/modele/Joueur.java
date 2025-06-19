package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

public class Joueur extends Entite {

    private int hauteurJoueur;
    private int largeurJoueur;
    private Inventaire inv;


    public Joueur(int x, int y, Map map, int vie, int v, Inventaire inv) {
        super(x, y, map, vie, v);
        this.hauteurJoueur = 60;
        this.largeurJoueur = 30;
        this.inv = inv;
    }

    public boolean decrementerVie() {
        super.decrementerVie(1);
         if(this.getVie() <= 0) {
            System.exit(0);
        }
        return true;
    }
}