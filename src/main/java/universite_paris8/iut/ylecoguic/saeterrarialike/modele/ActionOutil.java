package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

/**
 * Interface pour le Pattern Strategy.
 * Définit le comportement d'un "clic" du joueur, qui peut changer
 * en fonction de l'outil équipé (mains, pioche, épée...).
 */
public interface ActionOutil {
    void actionPrincipale(Joueur joueur, int col, int ligne, Environnement env);

    void actionSecondaire(Joueur joueur, int col, int ligne, Objet objetSelectionne);
}