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

        // Cherche si un ennemi est aux coordonnées du clic
        for (Ennemis ennemi : env.getEnnemis()) {
            if (col == ennemi.getTileX() && ligne == ennemi.getTileY()) {
                joueur.attaque(ennemi, DEGATS_ATTAQUE_JOUEUR);
                aToucheEnnemi = true;
                break;
            }
        }

        // Si aucun ennemi n'est touché, on casse le bloc
        if (!aToucheEnnemi) {
            joueur.casserBlock(col, ligne, peutCasser);
        }
    }

    @Override
    public void actionSecondaire(Joueur joueur, int col, int ligne, Objet objetSelectionne) {
        boolean peutPoser = joueur.estDansPortee(col, ligne, joueur.getTileX(), joueur.getTileY(), PORTEE_POSER_BLOC);

        // La stratégie "ActionMain" ne fait que poser des blocs.
        // La vérification de la table de craft est gérée par le Contrôleur
        // car c'est une action de l'interface (Vue).
        joueur.poserBlock(col, ligne, peutPoser, objetSelectionne);
    }
}