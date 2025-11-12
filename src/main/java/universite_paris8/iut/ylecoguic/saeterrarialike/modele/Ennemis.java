package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Dijkstra;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Entite;
import universite_paris8.iut.ylecoguic.saeterrarialike.modele.Terrain;
import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueEnnemis;

import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesJeu.ANIMATION_ARRET;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesJeu.ANIMATION_MARCHE_GAUCHE;
import static universite_paris8.iut.ylecoguic.saeterrarialike.modele.ConstantesTerrain.*;

public class Ennemis extends Entite {

    Terrain map;
    private boolean enMarche;
    private int hauteurEnnemis;
    private int largeurEnnemis;
    private VueEnnemis vueEnnemis;
    private Dijkstra dijkstra;

    // Pattern Strategy : comportement de déplacement interchangeable
    private StrategieDeplacement strategie;

    public Ennemis(int x, int y, Terrain map, int vie, int v, VueEnnemis vueEnnemis) {
        super(x, y, map, vie, v);
        this.map = map;
        this.enMarche = true;
        this.hauteurEnnemis = 60;
        this.largeurEnnemis = 30;
        this.vueEnnemis = vueEnnemis;
        this.dijkstra = new Dijkstra(map);
        // Par défaut, comportement aléatoire
        this.strategie = new DeplacementAleatoire();
    }

    public boolean peutVoirJoueur(int ennemisX, int ennemisY, int joueurX, int joueurY, int distanceVision) {
        double distance = Math.sqrt(Math.pow(joueurX - ennemisX, 2) + Math.pow(joueurY - ennemisY, 2));
        return distance <= distanceVision;
    }

    public boolean peutVoirJoueurAvecLigneDeVue(int ennemisX, int ennemisY, int joueurX, int joueurY, int distanceVue) {
        int dx = Math.abs(joueurX - ennemisX);
        int dy = Math.abs(joueurY - ennemisY);
        int x = ennemisX;
        int y = ennemisY;
        int x_inc = (joueurX > ennemisX) ? 1 : -1;
        int y_inc = (joueurY > ennemisY) ? 1 : -1;
        int error = dx - dy;

        if (!peutVoirJoueur(ennemisX, ennemisY, joueurX, joueurY, distanceVue)) {
            return false;
        }
        dx *= 2;
        dy *= 2;

        for (int n = 1 + dx + dy; n > 0; n--) {
            if (x != ennemisX || y != ennemisY) {
                int idCase = map.codeTuile(y, x);
                if (idCase != TUILE_VIDE && idCase != TUILE_BARBELE) {
                    return false;
                }
            }
            if (error > 0) {
                x += x_inc;
                error -= dy;
            } else {
                y += y_inc;
                error += dx;
            }
        }
        return true;
    }

    public void setStrategie(StrategieDeplacement strategie) {
        this.strategie = strategie;
    }

    public StrategieDeplacement getStrategie() {
        return strategie;
    }

    public void mettreAJourComportement(int joueurX, int joueurY, int distanceVue) {
        int joueurTileX = joueurX / TAILLE_TUILE;
        int joueurTileY = joueurY / TAILLE_TUILE;

        int ennemisX = getTileX();
        int ennemisY = getTileY();

        if (peutVoirJoueurAvecLigneDeVue(ennemisX, ennemisY, joueurTileX, joueurTileY, distanceVue)) {
            setStrategie(new DeplacementVersJoueur(dijkstra, vueEnnemis));
        } else {
            setStrategie(new DeplacementAleatoire());
        }

        strategie.deplacer(this, joueurTileX, joueurTileY, distanceVue);
        super.appliquerMouvementVertical();
    }

    @Override
    public int getTileX() {
        return (getX() + (largeurEnnemis / 2)) / TAILLE_TUILE;
    }

    @Override
    public int getTileY() {
        return (getY() + (hauteurEnnemis / 2)) / TAILLE_TUILE;
    }

    public boolean isEnMarche() {
        return enMarche;
    }

    public void setEnMarche(boolean enMarche) {
        this.enMarche = enMarche;
    }
}

// Interface du pattern Strategy
interface StrategieDeplacement {
    void deplacer(Ennemis ennemi, int joueurX, int joueurY, int distanceVue);
}

// Stratégie 1 : Déplacement aléatoire
class DeplacementAleatoire implements StrategieDeplacement {
    @Override
    public void deplacer(Ennemis ennemi, int joueurX, int joueurY, int distanceVue) {
        int dx = (int) (Math.random() * 3) - 1;
        int dy = (int) (Math.random() * 2);
        ennemi.deplacement(dx, dy);
    }
}

// Stratégie 2 : Déplacement vers le joueur (avec Dijkstra)
class DeplacementVersJoueur implements StrategieDeplacement {

    private Dijkstra dijkstra;
    private VueEnnemis vueEnnemis;

    public DeplacementVersJoueur(Dijkstra dijkstra, VueEnnemis vueEnnemis) {
        this.dijkstra = dijkstra;
        this.vueEnnemis = vueEnnemis;
    }

    @Override
    public void deplacer(Ennemis ennemi, int joueurX, int joueurY, int distanceVue) {
        int ennemisX = ennemi.getTileX();
        int ennemisY = ennemi.getTileY();

        java.util.List<int[]> chemin = dijkstra.trouverChemin(ennemisX, ennemisY, joueurX, joueurY);

        if (chemin != null && chemin.size() > 1) {
            int[] direction = dijkstra.trouveProchaineDirection(chemin);

            if (direction != null) {
                int dx = direction[0];
                int dy = direction[1];

                int prochaineX = ennemisX + dx;
                int prochaineY = ennemisY + dy;

                int idCaseDevant = ennemi.map.codeTuile(ennemisY, ennemisX + dx);
                int idCaseDessus = ennemi.map.codeTuile(ennemisY - 1, ennemisX + dx);

                if ((dx == 1 || dx == -1) && dy != 0) {
                    if (idCaseDevant != TUILE_VIDE && idCaseDessus == TUILE_VIDE) {
                        ennemi.demarrerSaut();
                    }
                }

                if (dx == -1) {
                    vueEnnemis.affichage(ANIMATION_MARCHE_GAUCHE);
                } else if (dx == 1) {
                    vueEnnemis.affichage(ANIMATION_MARCHE_GAUCHE);
                } else {
                    vueEnnemis.affichage(ANIMATION_ARRET);
                }

                ennemi.deplacement(dx, dy);
            }
        } else {
            new DeplacementAleatoire().deplacer(ennemi, joueurX, joueurY, distanceVue);
        }
    }
}
