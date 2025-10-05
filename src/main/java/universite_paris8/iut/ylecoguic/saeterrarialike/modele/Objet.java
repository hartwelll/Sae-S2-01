package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Cette class represente un objet de l'inventaire (item).
 * Responsabilités :
 * - Stocker les informations d'un objet (nom, description, quantité)
 * - Gérer la quantité via des properties JavaFX pour le binding
 * - Comparer les objets pour déterminer s'ils sont du même type
 */
public class Objet {
    private final StringProperty nom;
    private final StringProperty desc;
    private final IntegerProperty quantite;

    public Objet(String nom, String desc) {
        this.nom = new SimpleStringProperty(nom);
        this.desc = new SimpleStringProperty(desc);
        this.quantite = new SimpleIntegerProperty(1);
    }

    public Objet(String nom, String desc, int quantite) {
        this.nom = new SimpleStringProperty(nom);
        this.desc = new SimpleStringProperty(desc);
        this.quantite = new SimpleIntegerProperty(quantite);
    }

    public String getNom() {
        return nom.get();
    }

    public String getDesc() {
        return desc.get();
    }

    public int getQuantite() {
        return quantite.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public StringProperty descProperty() {
        return desc;
    }

    public IntegerProperty quantiteProperty() {
        return quantite;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public void setDesc(String desc) {
        this.desc.set(desc);
    }

    public void setQuantite(int quantite) {
        this.quantite.set(quantite);
    }

    public void incrementerQuantite(int nbQuantite) {
        this.quantite.set(this.quantite.get() + nbQuantite);
    }

    public void decrementerQuantite(int nb) {
        this.quantite.setValue(this.quantite.getValue() - nb);
    }

    public boolean estMemeType(Objet autre) {
        return this.getNom().equals(autre.getNom()) && this.getDesc().equals(autre.getDesc());
    }
}