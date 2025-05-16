package universite_paris8.iut.ylecoguic.saeterrarialike.vue;

import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Map;

import java.net.URL;
import java.util.HashMap;

public class VueMap {
    private HashMap<Integer, URL> tile;
    private Map map;

    public VueMap(){
        tile = new HashMap<>();
        map = new Map();
    }

    public void ajout(){
        tile.put(816, getClass().getResource("@caisse.png"));
        tile.put(815, getClass().getResource("@pierre.png"));
    }

    public void affichage(){

    }
}