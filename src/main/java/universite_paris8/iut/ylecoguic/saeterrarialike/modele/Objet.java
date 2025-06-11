package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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

    // Getters pour les valeurs
    public String getNom() {
        return nom.get();
    }

    public String getDesc() {
        return desc.get();
    }

    public int getQuantite() {
        return quantite.get();
    }

    // Getters pour les Properties (utilisés par JavaFX)
    public StringProperty nomProperty() {
        return nom;
    }

    public StringProperty descProperty() {
        return desc;
    }

    public IntegerProperty quantiteProperty() {
        return quantite;
    }

    // Setters
    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public void setDesc(String desc) {
        this.desc.set(desc);
    }

    public void setQuantite(int quantite) {
        this.quantite.set(quantite);
    }

    public void incrementerQuantite() {
        this.quantite.set(this.quantite.get() + 1);
    }

    public void decrementerQuantite(int nb) {
        this.quantite.setValue(this.quantite.getValue() - nb);
    }

    public boolean estMemeType(Objet autre) {
        return this.getNom().equals(autre.getNom()) && this.getDesc().equals(autre.getDesc());
    }
}