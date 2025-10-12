package universite_paris8.iut.ylecoguic.saeterrarialike.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Terrain;
import java.net.URL;
import java.util.HashMap;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesTerrain.*;


public class VueTerrain {
    private HashMap<Integer, Image> tileImages;
    private Terrain map;
    private TilePane pane;
    private ImageView[][] imageViewsTiles;

    public VueTerrain(TilePane pane, Terrain map){
        this.tileImages = new HashMap<>();
        this.map = map;
        this.pane = pane;
        this.pane.setPrefTileWidth(TAILLE_TUILE);
        this.pane.setPrefTileHeight(TAILLE_TUILE);
        this.imageViewsTiles = new ImageView[map.nbDeLignes()][map.nbDeColonnes()];
        this.imageViewsTiles = new ImageView[map.nbDeLignes()][map.nbDeColonnes()];
        initialiseTile();
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

    public void ajoutTile(int id, String chemin){
        Image image = creerImage(chemin);
        if (image != null) {
            tileImages.put(id, image);
        }
    }

    public void initialiseTile(){
        ajoutTile(0,"/Tile/newCiel.png");
        ajoutTile(1,"/Tile/pierre.png");
        ajoutTile(2,"/Tile/caisseBois.png");
        ajoutTile(3,"/Tile/barbele.png");
        ajoutTile(4,"/Tile/tableCraft.png");
        ajoutTile(5,"/Tile/bois.png");
    }

    public void affichage(){
        pane.getChildren().clear();
        for (int i = 0; i < pane.getPrefRows(); i++) {
            for (int j = 0; j < pane.getPrefColumns(); j++) {
                int tileId = map.codeTuile(i, j);
                Image image = tileImages.get(tileId);
                if (image != null) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(32);
                    imageView.setFitWidth(32);
                    imageViewsTiles[i][j] = imageView;
                    pane.getChildren().add(imageView);
                } else {
                    System.out.println("ID de tile inconnu : " + tileId + " à la position [" + i + ", " + j + "]");
                }
            }
        }
    }

    public void miseAJourAffichage(int ligne, int colonne){
        int tileId = map.codeTuile(ligne, colonne);
        Image newImage = tileImages.get(tileId);

        if (imageViewsTiles[ligne][colonne] != null) {
            imageViewsTiles[ligne][colonne].setImage(newImage);
        } else {
            System.out.println("image nulle aux coordonées [" + ligne + ", " + colonne + "]");
        }
    }
}