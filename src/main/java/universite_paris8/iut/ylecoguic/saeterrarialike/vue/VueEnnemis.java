package universite_paris8.iut.ylecoguic.saeterrarialike.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesEntite.*;


import java.util.HashMap;

public class VueEnnemis {
    private HashMap<Integer, Image> imagesEnnemi;
    private ImageView imageView;
    private Pane pane;

    public VueEnnemis(Pane pane) {
        this.pane = pane;
        this.imagesEnnemi = new HashMap<>();
        this.imageView = new ImageView();
        imageView.setFitHeight(HAUTEUR_SPRITE);
        imageView.setFitWidth(LARGEUR_SPRITE);
        this.pane.getChildren().add(imageView); // Ajout une seule fois
        initialiserEnnemis();
        affichage(0); // Image par défaut (statique)
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

    public void initialiserEnnemis() {
        ajoutPoses(0, "/Perso/EnnemiArret.png");
        ajoutPoses(1, "/Perso/EnnemisMarcheGauche.gif");
        ajoutPoses(2, "/Perso/EnnemisMarcheDroite.gif");
    }

    public void affichage(int id) {
        Image nouvelleImage = imagesEnnemi.get(id);
        if (nouvelleImage != null) {
            imageView.setImage(nouvelleImage);
        }
    }

    public void supprimerAffichage() {
        this.pane.getChildren().remove(imageView);
    }

    public ImageView getImageView() {
        return imageView;
    }
}