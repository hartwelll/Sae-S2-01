package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import universite_paris8.iut.ylecoguic.saeterrarialike.controller.Controller;

public class Joueur {

    private IntegerProperty xProperty;
    private IntegerProperty yProperty;
    private int v;
    private boolean marcheGauche = false;
    private boolean marcheDroite = false;
    private boolean saut = false;

    public Joueur(int x, int y){
        this.xProperty = new SimpleIntegerProperty(x);
        this.yProperty = new SimpleIntegerProperty(y);
        this.v = 10;
    }

    public void deplacement(int dx, int dy){
        xProperty.set(xProperty.getValue()+v*dx);
        yProperty.set(yProperty.getValue()-v*dy);
    }

    public void MAJ(Map map){
        Controller.seDeplace();
    }

    public IntegerProperty getxProperty() {
        return xProperty;
    }

    public IntegerProperty getyProperty(){
        return yProperty;
    }

    public int getX(){
        return xProperty.getValue();
    }

    public int getY(){
        return yProperty.getValue();
    }

    public void setMarcheGauche(boolean marcheGauche) {
        this.marcheGauche = marcheGauche;
    }

    public void setMarcheDroite(boolean marcheDroite) {
        this.marcheDroite = marcheDroite;
    }

    public void setSaut(boolean saut) {
        this.saut = saut;
    }
}
