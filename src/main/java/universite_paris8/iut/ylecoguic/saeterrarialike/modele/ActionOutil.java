package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

/**
 * Interface pour le Pattern Strategy.
 * Définit le comportement d'un "clic" du joueur, qui peut changer
 * en fonction de l'outil équipé (mains, pioche, épée...).
 */
public interface ActionOutil {

    /**
     * Définit l'action du clic gauche (principal).
     * @param joueur Le joueur qui effectue l'action.
     * @param col La colonne (tile) cliquée.
     * @param ligne La ligne (tile) cliquée.
     * @param env L'environnement de jeu (pour trouver les ennemis).
     */
    void actionPrincipale(Joueur joueur, int col, int ligne, Environnement env);

    /**
     * Définit l'action du clic droit (secondaire).
     * @param joueur Le joueur qui effectue l'action.
     * @param col La colonne (tile) cliquée.
     * @param ligne La ligne (tile) cliquée.
     * @param objetSelectionne L'objet tenu par le joueur (peut être null).
     */
    void actionSecondaire(Joueur joueur, int col, int ligne, Objet objetSelectionne);
}