package universite_paris8.iut.ylecoguic.saeterrarialike.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Joueur;

import java.net.URL;
import java.util.HashMap;

public class VueJoueur {
    private HashMap<Integer, Image> imagesJoueur;
    private Pane pane;
    private Joueur joueur;
    private ImageView imageView;

    public VueJoueur(Pane pane){
        imagesJoueur = new HashMap<>();
        joueur = new Joueur(0, 0);
        this.pane = pane;
        this.pane.setPrefHeight(32);
        this.pane.setPrefWidth(32);
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
            pane.getChildren().add(imageView);
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Joueur getJoueur() {
        return joueur;
    }
}
