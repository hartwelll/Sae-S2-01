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

    public VueJoueur(Pane pane) {
        this.pane = pane;
        this.imagesJoueur = new HashMap<>();
        this.imageView = new ImageView();
        imageView.setFitHeight(64);
        imageView.setFitWidth(32);
        pane.getChildren().add(imageView); // Ajout une seule fois
        initializePlayer();
        affichage(0); // Image par défaut (statique)
    }

    private Image creerImage(String chemin) {
        URL url = getClass().getResource(chemin);
        if (url == null) {
            System.err.println("Image non trouvée : " + chemin);
            return null;
        }
        return new Image(url.toString());
    }

    private void ajoutPoses(int id, String chemin) {
        Image image = creerImage(chemin);
        if (image != null) {
            imagesJoueur.put(id, image);
        }
    }

    public void initializePlayer() {
        ajoutPoses(0, "/Perso/JoueurArret.png");
        ajoutPoses(1, "/Perso/JoueurMarcheGauche.gif");
        ajoutPoses(2, "/Perso/JoueurMarcheDroite.gif");
    }

    public void affichage(int id) {
        Image nouvelleImage = imagesJoueur.get(id);
        if (nouvelleImage != null) {
            imageView.setImage(nouvelleImage);
        } else {
            System.err.println("Aucune image associée à l'ID : " + id);
        }
    }

    public ImageView getImageView() {
        return imageView;
    }
}
