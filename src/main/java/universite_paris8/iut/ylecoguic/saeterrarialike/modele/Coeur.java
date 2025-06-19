package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueCoeur;

public class Coeur {

    private VueCoeur vueCoeur;

    public Coeur(VueCoeur vueCoeur){
        this.vueCoeur = vueCoeur;
    }

    public void enleverCoeur(Joueur joueur){
        if (joueur.getVie() % 10 == 0 && joueur.getVie() <= 90) {
            vueCoeur.getCoeurList().get(0).setVisible(false);
            vueCoeur.getCoeurList().remove(0);
        }
    }
}