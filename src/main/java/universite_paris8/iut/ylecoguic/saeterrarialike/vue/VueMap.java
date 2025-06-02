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
    private ImageView imageView;

    public VueMap(TilePane pane, Map map){
        this.tileImages = new HashMap<>();
        this.map = map;
        this.pane = pane;
        this.pane.setPrefTileWidth(32);
        this.pane.setPrefTileHeight(32);
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
        ajoutTile(0,"/Tile/ciel.png");
        ajoutTile(1,"/Tile/pierre.png");
        ajoutTile(2,"/Tile/caisse.png");
        ajoutTile(3,"/Tile/barbele.png");
    }

    public void affichage(){
        for (int i = 0; i < map.getLigne(); i++) {
            for (int j = 0; j < map.getColonne(); j++) {
                int tileId = map.getCase(i, j); // getid
                Image image = tileImages.get(tileId);
                //System.out.println("ID de la tile récupéré : " + tileId);
                if (image != null) {
                    imageView = new ImageView(image);
                    pane.getChildren().add(imageView);
                    //System.out.println("ID de tile connu : " + tileId + " à la position [" + i + ", " + j + "]");
                } else {
                    System.out.println("ID de tile inconnu : " + tileId + " à la position [" + i + ", " + j + "]"); // Gérer le cas où l'ID de la tile n'a pas d'image correspondante
                }
            }
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void miseAJourAffichage(){
        affichage();
    }
}