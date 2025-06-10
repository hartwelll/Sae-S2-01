package universite_paris8.iut.ylecoguic.saeterrarialike.vue;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Objet;

public class VueObjet extends Rectangle {
    private final Objet objet;

    public VueObjet(Objet objet, double x, double y, double width, double height) {
        super(width, height);//herite rectrangle
        this.objet = objet;
        setTranslateX(x);//utilisable via l'herite de rectangle
        setTranslateY(y);
        setFill(Color.GOLD); //remplissage carre
        setStroke(Color.BLACK); //bord carre
        setPickOnBounds(true);
    }

    public Objet getObjet() {
        return objet;
    }
}