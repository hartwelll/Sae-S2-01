package universite_paris8.iut.ylecoguic.saeterrarialike.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Objet;
import java.net.URL;

public class VueObjet extends ImageView {
    private final Objet objet;

    public VueObjet(Objet objet, double x, double y, double width, double height) {
        super();
        this.objet = objet;
        setTranslateX(x);
        setTranslateY(y);
        setFitWidth(width);
        setFitHeight(height);
        setPickOnBounds(true);
    }

    public VueObjet(Objet objet, double x, double y, double width, double height, String chemin) {
        super();
        this.objet = objet;
        setTranslateX(x);
        setTranslateY(y);
        setFitWidth(width);
        setFitHeight(height);
        setImage(creerImage(chemin));

        setPickOnBounds(true);
    }

    public Image creerImage(String chemin) {
        URL url = this.getClass().getResource(chemin);
        return new Image(url.toString());
    }

    public Objet getObjet() {return objet;}

}