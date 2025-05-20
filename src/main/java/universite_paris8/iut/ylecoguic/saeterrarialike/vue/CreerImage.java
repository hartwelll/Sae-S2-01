package universite_paris8.iut.ylecoguic.saeterrarialike.vue;

import javax.swing.text.Element;
import javax.swing.text.html.ImageView;
import javafx.scene.image.Image;

import java.net.URL;

public class CreerImage {
    public Image creerImage(String chemin){
        URL url = getClass().getResource(chemin);
        if (url == null) {
            System.out.println("Image non trouvée : " + chemin);
            return null;
        }
        return new Image(url.toString());
    }
}
