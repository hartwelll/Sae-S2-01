package universite_paris8.iut.ylecoguic.saeterrarialike.vue;

import java.net.URL;
import java.util.HashMap;

public class VueMap {
    private HashMap<Integer, URL> map;

    public VueMap(){
        this.map = new HashMap<>();
    }

    public void ajout(){
        this.map.put(815, getClass().getResource("@pierre.png"));
        this.map.put(816, getClass().getResource("@caisse.png"));
    }
}
