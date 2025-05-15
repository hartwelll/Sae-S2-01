package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Joueur {

    private IntegerProperty xProperty;
    private IntegerProperty yProperty;
    private int v;

    public Joueur(int x, int y){
        this.xProperty = new SimpleIntegerProperty(x);
        this.yProperty = new SimpleIntegerProperty(y);
        this.v = 10;
    }

    public void deplacement(int dx, int dy){
        int nposX=this.getX()+(this.v*dx);
        int nposY=this.getY()+(this.v*dy);

        this.xProperty.set(nposX);
        this.yProperty.set(nposY);
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
}
