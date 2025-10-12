package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Cette class represente le système de crafting gérant les recettes et la fabrication d'objets.
 * Responsabilités :
 * - Enregistrer et stocker les recettes de crafting
 * - Vérifier si un craft est possible
 * - Exécuter le crafting (consommer les ingrédients et produire l'objet)
 */
public class CraftingSystem {

    private final HashMap<String, Recette> recettes;
    private final ArrayList<String> recettesAvecTableDeCraft;

    public CraftingSystem() {
        this.recettes = new HashMap<>();
        this.recettesAvecTableDeCraft = new ArrayList<>();
        initialiserRecettes();
    }

    private void initialiserRecettes() {
        ArrayList<Ingredient> ingredientsTableCraft = new ArrayList<>();
        ingredientsTableCraft.add(new Ingredient("Bois", 4));
        ingredientsTableCraft.add(new Ingredient("Pierre", 0));
        ajouterRecette(new Recette("Table De Craft", "une simple table de craft", ingredientsTableCraft), false);

        ArrayList<Ingredient> ingredientsCaisse = new ArrayList<>();
        ingredientsCaisse.add(new Ingredient("Bois", 2));
        ingredientsCaisse.add(new Ingredient("Pierre", 0));
        ajouterRecette(new Recette("Caisse En Bois", "une caisse qui caisse", ingredientsCaisse), false);


        ArrayList<Ingredient> ingredientsPioche = new ArrayList<>();
        ingredientsPioche.add(new Ingredient("Bois", 2));
        ingredientsPioche.add(new Ingredient("Pierre", 3));
        ajouterRecette(new Recette("Pioche", "Une pioche brillante", ingredientsPioche), true);

        ArrayList<Ingredient> ingredientsPelle = new ArrayList<>();
        ingredientsPelle.add(new Ingredient("Bois", 3));
        ingredientsPelle.add(new Ingredient("Pierre", 1));
        ajouterRecette(new Recette("Pelle", "Une pelle brillante", ingredientsPelle), true);

        ArrayList<Ingredient> ingredientsEpee = new ArrayList<>();
        ingredientsEpee.add(new Ingredient("Bois", 1));
        ingredientsEpee.add(new Ingredient("Pierre", 2));
        ajouterRecette(new Recette("Épée", "Une épée brillante", ingredientsEpee),true );
    }

    public void ajouterRecette(Recette recipe, boolean needsCraftingTable) {
        recettes.put(recipe.getNomResultat(), recipe);
        if (needsCraftingTable) {
            recettesAvecTableDeCraft.add(recipe.getNomResultat());
        }
    }

    public boolean peutCrafter(String nomObjet, Inventaire inventaire, boolean aAccesATableCraft) {
        Recette recipe = recettes.get(nomObjet);
        if (recipe == null) {
            return false;
        }

        if (recettesAvecTableDeCraft.contains(nomObjet) && !aAccesATableCraft) {
            return false;
        }

        return recipe.aLesIngredients(inventaire);
    }

    public Objet crafter(String nomObjet, Inventaire inventaire, boolean aAccesATableCraft) {
        if (!peutCrafter(nomObjet, inventaire, aAccesATableCraft)) {
            return null;
        }

        Recette recipe = recettes.get(nomObjet);
        recipe.consommerIngredients(inventaire);

        return new Objet(recipe.getNomResultat(), recipe.getDescriptionResultat(), 1);
    }

}