package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

/**
 * Cette class represente un ingrédient nécessaire pour une recette de crafting.
 * Contient le nom de l'objet et la quantité requise.
 */
public class Ingredient {
    private final String nom;
    private final int quantite;

    public Ingredient(String nom, int quantite) {
        this.nom = nom;
        this.quantite = quantite;
    }

    public String getNom() {
        return nom;
    }

    public int getQuantite() {
        return quantite;
    }

}