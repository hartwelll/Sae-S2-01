package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Cette class represente le système de crafting gérant les recettes et la fabrication d'objets.
 * Responsabilités :
 * - Enregistrer et stocker les recettes de crafting
 * - Vérifier si un craft est possible
 * - Exécuter le crafting (consommer les ingrédients et produire l'objet)
 */
public class CraftingSystem {

    private final Map<String, Recette> recipes;
    private final ArrayList<String> recipesNeedingCraftingTable;

    public CraftingSystem() {
        this.recipes = new HashMap<>();
        this.recipesNeedingCraftingTable = new ArrayList<>();
        initialiserRecettes();
    }

    private void initialiserRecettes() {
        ajouterRecette(new Recette(
                "Table De Craft",
                "une simple table de craft",
                new Ingredient("Bois", 4),
                new Ingredient("Pierre", 0)
        ), false);

        ajouterRecette(new Recette(
                "Caisse En Bois",
                "une caisse qui caisse",
                new Ingredient("Bois", 2),
                new Ingredient("Pierre", 0)
        ), false);

        ajouterRecette(new Recette(
                "Pioche",
                "Une pioche brillante",
                new Ingredient("Bois", 2),
                new Ingredient("Pierre", 3)
        ), true);

        ajouterRecette(new Recette(
                "Pelle",
                "Une pelle brillante",
                new Ingredient("Bois", 3),
                new Ingredient("Pierre", 1)
        ), true);

        ajouterRecette(new Recette(
                "Épée",
                "Une épée brillante",
                new Ingredient("Bois", 1),
                new Ingredient("Pierre", 2)
        ), true);
    }

    public void ajouterRecette(Recette recipe, boolean needsCraftingTable) {
        recipes.put(recipe.getNomResultat(), recipe);
        if (needsCraftingTable) {
            recipesNeedingCraftingTable.add(recipe.getNomResultat());
        }
    }

    public boolean peutCrafter(String nomObjet, Inventaire inventaire, boolean hasAccessToCraftingTable) {
        Recette recipe = recipes.get(nomObjet);
        if (recipe == null) {
            return false;
        }

        if (recipesNeedingCraftingTable.contains(nomObjet) && !hasAccessToCraftingTable) {
            return false;
        }

        return recipe.aLesIngredients(inventaire);
    }

    public Objet crafter(String nomObjet, Inventaire inventaire, boolean hasAccessToCraftingTable) {
        if (!peutCrafter(nomObjet, inventaire, hasAccessToCraftingTable)) {
            return null;
        }

        Recette recipe = recipes.get(nomObjet);
        recipe.consommerIngredients(inventaire);

        return new Objet(recipe.getNomResultat(), recipe.getDescriptionResultat(), 1);
    }

    public ArrayList<Recette> getRecettesDisponibles(boolean hasAccessToCraftingTable) {
        ArrayList<Recette> disponibles = new ArrayList<>();
        for (Recette recipe : recipes.values()) {
            boolean needsTable = recipesNeedingCraftingTable.contains(recipe.getNomResultat());
            if (!needsTable || hasAccessToCraftingTable) {
                disponibles.add(recipe);
            }
        }
        return disponibles;
    }

    public Recette getRecette(String nomObjet) {
        return recipes.get(nomObjet);
    }
}