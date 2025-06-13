package universite_paris8.iut.ylecoguic.saeterrarialike.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class VueEnnemis {
    private HashMap<Integer, Image> imagesEnnemi;
    private ImageView imageView;
    private Pane pane;

    public VueEnnemis(Pane pane) {
        this.pane = pane;
        this.imagesEnnemi = new HashMap<>();
        this.imageView = new ImageView();
        initializeEnnemis();
        affichage();
    }

    protected Image creerImage(String chemin) {
        try {
            return new Image(getClass().getResourceAsStream(chemin));
        } catch (Exception e) {
            System.out.println("Erreur d'image: " + chemin);
            return null;
        }
    }

    public void ajoutPoses(int id, String chemin) {
        Image image = creerImage(chemin);
        if (image != null) {
            imagesEnnemi.put(id, image);
        }
    }

    public void initializeEnnemis() {
        ajoutPoses(0, "/Perso/EnnemiArret.png");
    }

    public void affichage() {
        Image image = imagesEnnemi.get(0);
        if (image != null) {
            imageView.setImage(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(30);
            pane.getChildren().add(imageView);
        }
    }

    public ImageView getImageView() {
        return imageView;
    }
}