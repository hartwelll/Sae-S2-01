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

    public VueMap(TilePane pane){
        this.tileImages = new HashMap<>();
        this.map = new Map(); // Assure-toi que ta classe Map est correctement initialisée avec les données de la carte
        this.pane = pane;
        this.pane.setPrefTileWidth(32);
        this.pane.setPrefTileHeight(32);
        initialiseTile();
        affichage();
    }

    public Image creerImage(String chemin){
        URL url = getClass().getResource(chemin);
        if (url == null) {
            System.err.println("Image non trouvée : " + chemin);
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
    }

    public void affichage(){
        for (int i = 0; i < map.getLigne(); i++) {
            for (int j = 0; j < map.getColonne(); j++) {
                int tileId = map.getCase(i, j); // Récupère l'ID de la tile à cette position
                Image image = tileImages.get(tileId);
                System.out.println("ID de la tile récupéré : " + tileId);
                if (image != null) {
                    ImageView imageView = new ImageView(image);
                    pane.getChildren().add(imageView);
                } else {
                    System.err.println("ID de tile inconnu : " + tileId + " à la position [" + i + ", " + j + "]"); // Gérer le cas où l'ID de la tile n'a pas d'image correspondante
                }
            }
        }
    }

    // Méthode pour mettre à jour l'affichage de la carte si le modèle change
    public void miseAJourAffichage(){
        affichage();
    }
}