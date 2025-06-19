package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueCoeur;

public class Coeur {

    private Joueur joueur;
    private VueCoeur vueCoeur;

    public Coeur(Joueur joueur, VueCoeur vueCoeur){
        this.joueur = joueur;
        this.vueCoeur = vueCoeur;
    }

    public void enleverCoeur(){
        if (!vueCoeur.getCoeurList().isEmpty()) {
            if (joueur.getVie() % 10 == 0 && joueur.decrementerVie() && joueur.getVie() <= 90) {
                vueCoeur.getCoeurList().get(0).setVisible(false);
                vueCoeur.getCoeurList().remove(0);
            }
        }
    }
}