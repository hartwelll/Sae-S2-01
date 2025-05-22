package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Joueur {

    private IntegerProperty xProperty;
    private IntegerProperty yProperty;
    private int v;
    private int vSaut;
    private int vGravite;
    private boolean marcheGauche = false;
    private boolean marcheDroite = false;
    private boolean saut = false;

    public Joueur(int x, int y){
        this.xProperty = new SimpleIntegerProperty(x);
        this.yProperty = new SimpleIntegerProperty(y);
        this.v = 10;
        this.vSaut = 100;
        this.vGravite = 4;
    }

    public void deplacement(int dx){
        xProperty.set(xProperty.getValue()+v*dx);
    }

    public void saut(int dy){
        yProperty.set(yProperty.getValue()-vSaut*dy);
    }

    public void gravite(int dy){
        yProperty.set(yProperty.getValue()-vGravite*dy);
    }

    public void MAJ(Map map){
        //seDeplace();
        if(getY() < 905){
            gravite(-1);
        }
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

    public void setSaut(boolean saut) {
        this.saut = saut;
    }
}
