package universite_paris8.iut.ylecoguic.saeterrarialike.vue;

import javax.swing.text.Element;
import javax.swing.text.html.ImageView;
import javafx.scene.image.Image;

import java.net.URL;

public class CreerImage {

    public ImageView creerImage(String chemin){
        URL url = getClass().getResource(chemin);
        Image image = new Image(String.valueOf(url));
        ImageView imageview = new ImageView((Element) image);
        return imageview;
    }
}
