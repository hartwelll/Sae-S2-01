package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import universite_paris8.iut.ylecoguic.saeterrarialike.vue.VueEnnemis;

import java.util.List;

public class Ennemis extends Entite {

    private Terrain map;
    private boolean enMarche;
    private int hauteurEnnemis;
    private int largeurEnnemis;
    private VueEnnemis vueEnnemis;
    private Dijkstra dijkstra;

    public Ennemis(int x, int y, Terrain map, int vie, int v, VueEnnemis vueEnnemis) {
        super(x, y, map, vie, v);
        this.map = map;
        this.enMarche = true;
        this.hauteurEnnemis = 60;
        this.largeurEnnemis = 30;
        this.vueEnnemis = vueEnnemis;
        this.dijkstra = new Dijkstra(map);
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
                if (idCase != 0 && idCase != 3) {
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

    public void deplacementAleatoire() { //passe a didjtra  this.demarrerSaut  didjtra autre class??
        int dx = (int) (Math.random()*3);
        int dy = (int) (Math.random()*2);
        if(dx == 2){
            dx = -1;
        }
        super.deplacement(dx, dy);
    }

    public void deplacementVersJoueur(int joueurX, int joueurY, int distanceVue) {
        int ennemisX = getTileX();
        int ennemisY = getTileY();

        // Si l'ennemi n'est pas en marche, on arrête
        if (!enMarche) return;

        // Si l'ennemi ne peut pas voir le joueur, déplacement aléatoire
        if (!peutVoirJoueurAvecLigneDeVue(ennemisX, ennemisY, joueurX, joueurY, distanceVue)) {
            deplacementAleatoire();
            return;
        }

        // Calcul du chemin vers le joueur avec Dijkstra
        List<int[]> chemin = dijkstra.trouverChemin(ennemisX, ennemisY, joueurX, joueurY);

        // Si on a trouvé un chemin et qu'il y a au moins 2 positions (départ + prochaine)
        if (chemin != null && chemin.size() > 1) {
            int[] direction = dijkstra.trouveProchaineDirection(chemin);

            if (direction != null) {
                // Convertir la direction en coordonnées de déplacement
                int dx = direction[0];
                int dy = direction[1];

                // Si le déplacement est vers le haut (saut nécessaire)
                int prochaineX = ennemisX + dx;
                int prochaineY = ennemisY + dy;

                //int idCaseDevant = map.getCase(prochaineY, prochaineX);
                //boolean obstacleDevant = (idCaseDevant != 0);

                if ((dx == 1 || dx == -1) && dy != 0) {
                    // Vérifie si un obstacle bloque horizontalement
                    int idCaseDevant = map.codeTuile(ennemisY, ennemisX + dx);
                    int idCaseDessus = map.codeTuile(ennemisY-1, ennemisX + dx);

                    if (idCaseDevant != 0 && idCaseDessus == 0) {
                        // Si obstacle devant mais espace au-dessus, on saute
                        super.demarrerSaut();
                    }
                }
                if (dx == -1){
                    vueEnnemis.affichage(1);
                } else if (dx == 1) {
                    vueEnnemis.affichage(2);
                } else vueEnnemis.affichage(0);

                // Appliquer le déplacement
                super.deplacement(dx, dy);
            }
        } else {
            // Si aucun chemin trouvé, déplacement aléatoire
            deplacementAleatoire();
        }
    }

    public void mettreAJourComportement(int joueurX, int joueurY, int distanceVue) {
        // Convertir les coordonnées du joueur en coordonnées de tuile
        int joueurTileX = joueurX / 32;
        int joueurTileY = joueurY / 32;

        // Appeler le déplacement vers le joueur
        deplacementVersJoueur(joueurTileX, joueurTileY, distanceVue);

        // Appliquer la gravité et les mouvements verticaux
        super.appliquerMouvementVertical();
    }
    @Override
    public int getTileX() {
        return (getX() + (largeurEnnemis / 2)) / 32;
    }

    @Override
    public int getTileY() {
        return (getY() + (hauteurEnnemis / 2)) / 32;
    }

    public boolean isEnMarche() {
        return enMarche;
    }

    public void setEnMarche(boolean enMarche) {
        this.enMarche = enMarche;
    }
}