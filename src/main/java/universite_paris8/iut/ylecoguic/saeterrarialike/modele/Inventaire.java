package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventaire {
    private final ObservableList<Objet> objets = FXCollections.observableArrayList();
    public ObservableList<Objet> getObjets() {
        return objets;
    }

    public void addObjet(Objet nouvelObjet) {
        for (Objet objetExistant : objets) {
            if (objetExistant.estMemeType(nouvelObjet)) {
                objetExistant.incrementerQuantite();
                return;
            }
        }
        objets.add(nouvelObjet);
    }

    public void removeObjet(Objet objetAremove, int nbAremove) {
        for (Objet objetExistant : objets) {
            if (objetExistant.estMemeType(objetAremove)) {
                objetExistant.decrementerQuantite(nbAremove);
                if(objetExistant.getQuantite() <= 0){
                    objets.remove(objetExistant);
                }
                return;
            }
        }
        objets.remove(objetAremove);
    }

    public int getQuantiteObjet(String nomObjet) {
        for (Objet o : objets) {
            if (o.getNom().equals(nomObjet)) {
                return o.getQuantite();
            }
        }
        return 0;
    }
}