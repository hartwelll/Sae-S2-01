package universite_paris8.iut.ylecoguic.saeterrarialike.test;


import javafx.geometry.Rectangle2D;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Map;

import java.util.ArrayList;

// On crée une sous-classe de Map juste pour les besoins des tests
public class MapDeTest extends Map {

    private ArrayList<Rectangle2D> testHitboxList;
    private ArrayList<Rectangle2D> testHurtboxList;

    public MapDeTest() {
        // Appelez le constructeur parent si nécessaire, ou laissez-le vide si Map n'a pas d'initialisation complexe.
        // Si votre constructeur Map() fait des choses complexes (lecture de fichier, etc.),
        // vous devrez peut-être le simuler ici ou adapter.
        super(); // Appelle le constructeur par défaut de Map
        this.testHitboxList = new ArrayList<>();
        this.testHurtboxList = new ArrayList<>();
    }

    // Méthode pour définir la liste des hitboxes pour le test
    public void setTestHitboxList(Rectangle2D hitboxes) {
        this.testHitboxList.add(hitboxes);
    }

    // Méthode pour définir la liste des hurtboxes pour le test
    public void setTestHurtboxList(Rectangle2D hurtboxes) {
        this.testHurtboxList.add(hurtboxes);
    }

    @Override
    public ArrayList<Rectangle2D> getHitboxList() {
        return this.testHitboxList; // Retourne notre liste de test
    }

    @Override
    public ArrayList<Rectangle2D> getHurtboxList() {
        return this.testHurtboxList; // Retourne notre liste de test
    }

    // Si Map a d'autres méthodes utilisées par Entite (ex: getCase, setCase),
    // vous devrez peut-être les surcharger ici aussi pour contrôler leur comportement
    // ou vous assurer que leur implémentation par défaut est suffisante pour le test.
    // Par exemple, si getColId ou getLigneId sont appelées:
    // @Override
    // public int getColId(int col) { return col; }
    // @Override
    // public int getLigneId(int ligne) { return ligne; }
}
