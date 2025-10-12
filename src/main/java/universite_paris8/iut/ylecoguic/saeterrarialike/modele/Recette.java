package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import java.util.ArrayList;

/**
 * Cette class represente une recette de crafting avec ses ingrédients et son résultat.
 * Responsabilités :
 * - Stocker les ingrédients nécessaires à la fabrication
 * - Vérifier si un inventaire contient les ingrédients requis
 * - Consommer les ingrédients d'un inventaire lors du crafting
 */
public class Recette {
    private final String nomResultat;
    private final String descriptionResultat;
    private final ArrayList<Ingredient> ingredients;

    public Recette(String nomResultat, String descriptionResultat, ArrayList<Ingredient> ingredients) {
        this.nomResultat = nomResultat;
        this.descriptionResultat = descriptionResultat;
        this.ingredients = new ArrayList<>();
        for (Ingredient ing : ingredients) {
            if (ing.getQuantite() > 0) {
                this.ingredients.add(ing);
            }
        }
    }

    public boolean aLesIngredients(Inventaire inventaire) {
        for (Ingredient ing : ingredients) {
            if (inventaire.getQuantiteObjet(ing.getNom()) < ing.getQuantite()) {
                return false;
            }
        }
        return true;
    }

    public void consommerIngredients(Inventaire inventaire) {
        for (Ingredient ing : ingredients) {
            inventaire.supprimerObjetParNom(ing.getNom(), ing.getQuantite());
        }
    }

    public String getNomResultat() {
        return nomResultat;
    }

    public String getDescriptionResultat() {
        return descriptionResultat;
    }
}