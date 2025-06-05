package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

public class Objet {
    private String nom;
    private String desc;

    public Objet(String nom, String desc) {
        this.nom = nom;
        this.desc = desc;
    }

    public String getNom() {
        return nom;
    }

    public String getDesc() {  // ✅ Le getter était déjà bon
        return desc;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}