package universite_paris8.iut.ylecoguic.saeterrarialike.test;

import org.junit.Test;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestTerraria {

    @Test
    public void testDeplacement() {
        Map map = new Map();
        Entite entite = new Entite(50, 50, map, 100, 10);
        entite.deplacement(1, 0);
        assertEquals(60, entite.getX());
        assertEquals(10, entite.getV());
    }

    @Test
    public void testCasserBlock() {
        Map map = new Map();
        Inventaire inventaire = new Inventaire();
        Objet objet = new Objet("un objet", "c'est un objet");
        Joueur joueur = new Joueur(map, inventaire, 500, 725);

        int ligne = 1;
        int col = 2;
        int idBloc = 2;

        map.setCase(ligne, col, idBloc);

        joueur.casserBlock(col, ligne, true);
        inventaire.addObjet(objet, 1);

        assertEquals(0, map.getCase(ligne, col), "La case doit être cassée (0)");
        assertEquals(1, inventaire.getQuantiteObjet("un objet"));
    }
}
