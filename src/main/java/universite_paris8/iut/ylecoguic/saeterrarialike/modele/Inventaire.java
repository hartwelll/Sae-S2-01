package universite_paris8.iut.ylecoguic.saeterrarialike.modele;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventaire {
    private final ObservableList<Objet> objets = FXCollections.observableArrayList();

    public ObservableList<Objet> getObjets() {
        return objets;
    }
    public void addObjet(Objet objet) {
        objets.add(objet);
    }
}
