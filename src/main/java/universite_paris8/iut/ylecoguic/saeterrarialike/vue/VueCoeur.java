package universite_paris8.iut.ylecoguic.saeterrarialike.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Coeur;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Joueur;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesJeu.*;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesEntite.*;


import java.net.URL;
import java.util.ArrayList;

public class VueCoeur {

    private ArrayList<ImageView> coeurList;
    private HBox coeurs;

    public VueCoeur(HBox coeurs){
        coeurList = new ArrayList<>();
        this.coeurs = coeurs;
        initialiserCoeur();
    }

    public void initialiserCoeur(){
        for (int i = 0; i < NOMBRE_COEURS_MAX; i++) {
            ImageView coeurImageView = new ImageView(creerImage("/Coeur/coeur.png"));
            coeurImageView.setFitHeight(TAILLE_COEUR);
            coeurImageView.setFitWidth(TAILLE_COEUR);
            coeurImageView.setPreserveRatio(true);
            coeurList.add(coeurImageView);
            coeurs.getChildren().add(coeurImageView);
        }
    }

    public Image creerImage(String chemin){
        URL url = getClass().getResource(chemin);
        return new Image(url.toString());
    }

    public void enleverCoeurVue(Joueur joueur, Coeur coeur){
        if (!coeurList.isEmpty()){
            coeur.enleverCoeur(joueur);
        }
    }

    public ArrayList<ImageView> getCoeurList() {
        return coeurList;
    }
}
