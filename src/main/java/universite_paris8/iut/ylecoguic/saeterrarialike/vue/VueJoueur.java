package universite_paris8.iut.ylecoguic.saeterrarialike.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.HashMap;

public class VueJoueur {
    private HashMap<Integer, Image> imagesJoueur;
    private Pane pane;
    private ImageView imageView;

    public VueJoueur(Pane pane){
        imagesJoueur = new HashMap<>();
        this.pane = pane;
        initializePlayer();
        affichage();
    }

    public Image creerImage(String chemin){
        URL url = getClass().getResource(chemin);
        if (url == null) {
            System.out.println("Image non trouvée : " + chemin);
            return null;
        }
        return new Image(url.toString());
    }

    public void ajoutPoses(int id, String chemin){
        Image image = creerImage(chemin);
        if (image != null) {
            imagesJoueur.put(id, image);
        }
    }

    public void initializePlayer(){
        ajoutPoses(0, "/Perso/perso.gif");
    }

    public void affichage(){
        Image image = imagesJoueur.get(0);
        if (image != null) {
            imageView = new ImageView(image);
            imageView.setFitHeight(64);
            imageView.setFitWidth(32);
            pane.getChildren().add(imageView);
        }
    }

    public ImageView getImageView() {
        return imageView;
    }
}
