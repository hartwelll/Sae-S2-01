package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesEntite.*;

/**
 * Stratégie concrète pour l'action "mains nues".
 * Clic gauche = attaquer un ennemi OU casser un bloc.
 * Clic droit = poser un bloc.
 * (Contient la logique qui se trouvait avant dans Joueur.clicGauche/Droit)
 */
public class ActionMain implements ActionOutil {

    @Override
    public void actionPrincipale(Joueur joueur, int col, int ligne, Environnement env) {
        boolean peutCasser = joueur.estDansPortee(col, ligne, joueur.getTileX(), joueur.getTileY(), PORTEE_CASSER_BLOC);
        boolean aToucheEnnemi = false;

        for (Ennemis ennemi : env.getEnnemis()) {
            if (col == ennemi.getTileX() && ligne == ennemi.getTileY()) {
                joueur.attaque(ennemi, DEGATS_ATTAQUE_JOUEUR);
                aToucheEnnemi = true;
                break;
            }
        }

        if (!aToucheEnnemi) {
            joueur.casserBlock(col, ligne, peutCasser);
        }
    }

    @Override
    public void actionSecondaire(Joueur joueur, int col, int ligne, Objet objetSelectionne) {
        boolean peutPoser = joueur.estDansPortee(col, ligne, joueur.getTileX(), joueur.getTileY(), PORTEE_POSER_BLOC);
        joueur.poserBlock(col, ligne, peutPoser, objetSelectionne);
    }
}