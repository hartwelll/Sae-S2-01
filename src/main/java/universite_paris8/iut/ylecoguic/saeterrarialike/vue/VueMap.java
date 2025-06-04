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
    // Ajout d'un tableau pour stocker toutes les ImageView du plateau
    private ImageView[][] imageViewsTiles; // Pour un accès facile par coordonnée

    public VueMap(TilePane pane, Map map){
        this.tileImages = new HashMap<>();
        this.map = map;
        this.pane = pane;
        this.pane.setPrefTileWidth(32);
        this.pane.setPrefTileHeight(32);
        // Initialiser le tableau d'ImageView avec les dimensions de la map
        this.imageViewsTiles = new ImageView[map.getLigne()][map.getColonne()];
        initialiseTile();
        affichage(); // L'affichage initial va maintenant remplir imageViewsTiles
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

    // Cette méthode crée toutes les ImageView et les ajoute au TilePane ET au tableau
    public void affichage(){
        // On vide le TilePane avant de tout recréer (utile si on réinitialise la map)
        pane.getChildren().clear();
        for (int i = 0; i < map.getLigne(); i++) {
            for (int j = 0; j < map.getColonne(); j++) {
                int tileId = map.getCase(i, j);
                Image image = tileImages.get(tileId);
                if (image != null) {
                    ImageView imageView = new ImageView(image);
                    imageViewsTiles[i][j] = imageView; // Stocke l'ImageView dans le tableau
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

        // Vérifie si l'ImageView existe déjà à cet emplacement
        if (imageViewsTiles[ligne][colonne] != null) {
            // Met à jour l'image de l'ImageView existante
            imageViewsTiles[ligne][colonne].setImage(newImage);
        } else {
            System.err.println("WARN: ImageView was null at [" + ligne + ", " + colonne + "], recreated.");
        }
    }
}