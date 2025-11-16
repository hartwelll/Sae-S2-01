package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import java.util.ArrayList;

import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesEntite.ENNEMI_DEGATS_ATTAQUE;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesEntite.ENNEMI_DISTANCE_VISION;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesJeu.*;

public class Environnement {

    private Joueur joueur;
    private ArrayList<Ennemis> ennemis;

    public Environnement(Joueur joueur) {
        this.joueur = joueur;
        ennemis = new ArrayList<>();
    }

    public void ajouterEnnemis(Ennemis e){
        ennemis.add(e);
    }

    public void unTour() {
        joueur.appliquerMouvementVertical();

        for(Ennemis e : ennemis){
            if(e.getVie() > 0) {
                e.appliquerMouvementVertical();
                e.mettreAJourComportement(joueur.getX(), joueur.getY(), ENNEMI_DISTANCE_VISION);
                if (DELAY >= DELAI_ATTAQUE_ENNEMI_NANOSEC) {
                    e.attaque(joueur, ENNEMI_DEGATS_ATTAQUE);
                    DELAY = 0;
                } else DELAY += FRAME_INTERVAL_NANOSEC;
            }
        }
    }

    public ArrayList<Ennemis> getEnnemis() {
        return ennemis;
    }
}
