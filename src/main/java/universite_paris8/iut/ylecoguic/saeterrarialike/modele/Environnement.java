package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import java.util.ArrayList;

import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesEntite.ENNEMI_DEGATS_ATTAQUE;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesEntite.ENNEMI_DISTANCE_VISION;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesJeu.DELAI_ATTAQUE_ENNEMI_NANOSEC;

public class Environnement {

    private ArrayList<Entite> entites;
    public Environnement() {
        entites = new ArrayList<>();
    }

    public void ajouterEntite(Entite e){
        entites.add(e);
    }

    public void unTour() {
        joueur.appliquerMouvementVertical();

        for(Entite e : entites){
            if (e.estMort()){
                panneauJoueur.getChildren().remove(e);
                entites.remove(e);
            }
        }
        if(ennemis.getVie() > 0) {
            ennemis.appliquerMouvementVertical();
            ennemis.mettreAJourComportement(joueur.getX(), joueur.getY(), ENNEMI_DISTANCE_VISION);
            if (delay >= DELAI_ATTAQUE_ENNEMI_NANOSEC) {
                ennemis.attaque(joueur, ENNEMI_DEGATS_ATTAQUE);
                delay = 0;
            } else delay += frameInterval;
        }
    }
}
