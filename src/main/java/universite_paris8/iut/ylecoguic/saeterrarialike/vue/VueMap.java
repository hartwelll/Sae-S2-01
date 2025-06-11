package universite_paris8.iut.ylecoguic.saeterrarialike.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Map;
import java.net.URL;
import java.util.HashMap;

public class VueMap {
    private HashMap<Integer, Image> tileImages;
    private Map map;
    private TilePane pane;
    private ImageView[][] imageViewsTiles;

    public VueMap(TilePane pane, Map map){
        this.tileImages = new HashMap<>();
        this.map = map;
        this.pane = pane;
        this.pane.setPrefTileWidth(32);
        this.pane.setPrefTileHeight(32);
        this.imageViewsTiles = new ImageView[map.getLigne()][map.getColonne()];
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
        ajoutTile(2,"/Tile/caisse.png");
        ajoutTile(3,"/Tile/barbele.png");
        ajoutTile(4, "/Tile/tableCraft.png");
    }

    public void affichage(){
        pane.getChildren().clear();
        for (int i = 0; i < map.getLigne(); i++) {
            for (int j = 0; j < map.getColonne(); j++) {
                int tileId = map.getCase(i, j);
                Image image = tileImages.get(tileId);
                if (image != null) {
                    ImageView imageView = new ImageView(image);
                    imageViewsTiles[i][j] = imageView;
                    pane.getChildren().add(imageView);
                } else {
                    System.out.println("ID de tile inconnu : " + tileId + " à la position [" + i + ", " + j + "]");
                }
            }
        }
    }

    public void miseAJourAffichage(int ligne, int colonne){
        int tileId = map.getCase(ligne, colonne);
        Image newImage = tileImages.get(tileId);

        if (imageViewsTiles[ligne][colonne] != null) {
            imageViewsTiles[ligne][colonne].setImage(newImage);
        } else {
            System.out.println("image nulle aux coordonées [" + ligne + ", " + colonne + "]");
        }
    }
}