package universite_paris8.iut.ylecoguic.saeterrarialike.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

public class Dijkstra {

    private Terrain map;
    private int largeurMap;
    private int hauteurMap;

    private static class Noeud implements Comparable<Noeud> {
        int x, y;
        int distance;
        Noeud parent;

        public Noeud(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
            this.parent = null;
        }

        @Override
        public int compareTo(Noeud autreN) {
            return Integer.compare(this.distance, autreN.distance);
        }

        @Override
        public boolean equals(Object obj) {//2 noeurds au meme coordonée
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Noeud noeud = (Noeud) obj;
            return x == noeud.x && y == noeud.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        public String getCle() {
            return x + "," + y;
        }
    }

    public Dijkstra(Terrain map) {
        this.map = map;
        this.largeurMap = map.nbDeColonnes();
        this.hauteurMap = map.nbDeLignes();
    }

    public List<int[]> trouverChemin(int ennemisX, int ennemisY, int joueurX, int joueurY) {

        PriorityQueue<Noeud> filepriorite = new PriorityQueue<>();
        Set<String> visite = new HashSet<>();
        HashMap<String, Noeud> noeuds = new HashMap<>();
        Noeud depart = new Noeud(ennemisX, ennemisY, 0);
        filepriorite.add(depart);
        noeuds.put(clePosition(ennemisX, ennemisY), depart);

        if (!estPositionValide(ennemisX, ennemisY) || !estPositionValide(joueurX, joueurY)) {
            return null;
        }
        if (ennemisX == joueurX && ennemisY == joueurY) {
            return new ArrayList<>();
        }

        int[][] directions = {
                {0, -1}, {0, 1}, {-1, 0}, {1, 0}, // haut, bas, gauche, droite
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}, // diagonales
                {-1, -2}, {1, -2} // sauts de 2 blocs vers la gauche/droite
        };


        while (!filepriorite.isEmpty()) {
            Noeud actuel = filepriorite.poll();
            String cleActuelle = clePosition(actuel.x, actuel.y);

            if (visite.contains(cleActuelle)) {
                continue;
            }
            visite.add(cleActuelle);

            if (actuel.x == joueurX && actuel.y == joueurY) {
                return reconstruireChemin(actuel);
            }

            for (int[] direction : directions) {
                int nouveauX = actuel.x + direction[0];
                int nouveauY = actuel.y + direction[1];
                String cleVoisin = clePosition(nouveauX, nouveauY);

                if (visite.contains(cleVoisin) || !peutSeDeplacer(actuel.x, actuel.y, nouveauX, nouveauY)) {
                    continue;
                }

                int coutDeplacement = (Math.abs(direction[0]) + Math.abs(direction[1]) == 2) ? 14 : 10;
                int nouvelleDistance = actuel.distance + coutDeplacement;

                Noeud voisin = noeuds.get(cleVoisin);
                if (voisin == null || nouvelleDistance < voisin.distance) {
                    voisin = new Noeud(nouveauX, nouveauY, nouvelleDistance);
                    voisin.parent = actuel;
                    noeuds.put(cleVoisin, voisin);
                    filepriorite.add(voisin);
                }
            }
        }
        return null;
    }

    private List<int[]> reconstruireChemin(Noeud noeudFinal) {
        List<int[]> chemin = new ArrayList<>();
        Noeud actuel = noeudFinal;

        while (actuel != null) {
            chemin.add(0, new int[]{actuel.x, actuel.y});
            actuel = actuel.parent;
        }
        return chemin;
    }

    private boolean estPositionValide(int x, int y) {
        return x >= 0 && x < largeurMap && y >= 0 && y < hauteurMap;
    }

    private boolean peutSeDeplacer(int depuisX, int depuisY, int versX, int versY) {
        if (!estPositionValide(versX, versY)) {
            return false;
        }

        int idCaseDestination = map.codeTuile(versY, versX);

        // Cas simple : la destination est vide (sol ou air)
        if (idCaseDestination == 0) {
            // Vérifie que l'on ne traverse pas des coins bloqués en diagonale
            if (Math.abs(versX - depuisX) == 1 && Math.abs(versY - depuisY) == 1) {
                int idCaseX = map.codeTuile(depuisY, versX);
                int idCaseY = map.codeTuile(versY, depuisX);

                if ((idCaseX != 0) || (idCaseY != 0)) {
                    return false;
                }
            }
            return true;
        }

        // Gérer les sauts par-dessus un obstacle horizontal
        int dx = versX - depuisX;
        int dy = versY - depuisY;

        if (dy == -1 && Math.abs(dx) == 1) {
            // Saut de 1 bloc vers le haut + côté
            int obstacleDevant = map.codeTuile(depuisY, versX);
            int espaceAuDessus = map.codeTuile(depuisY - 1, versX);

            if ((obstacleDevant != 0) && (espaceAuDessus == 0)) {
                return true;
            }
        }

        if (dy == -2 && Math.abs(dx) == 1) {
            // Saut de 2 blocs vers le haut + côté
            int obstacleDevant = map.codeTuile(depuisY, versX);
            int espace1 = map.codeTuile(depuisY - 1, versX);
            int espace2 = map.codeTuile(depuisY - 2, versX);

            if ((obstacleDevant != 0) && espace1 == 0 && espace2 == 0) {
                return true;
            }
        }

        return false; // Sinon, on ne peut pas y aller
    }


    private String clePosition(int x, int y) {
        return x + "," + y;
    }

    public int[] trouveProchaineDirection(List<int[]> chemin) {
        int[] positionActuelle = chemin.get(0);
        int[] prochainePposition = chemin.get(1);

        if (chemin == null || chemin.size() < 2) {
                return null;
            }
        return new int[]{
                prochainePposition[0] - positionActuelle[0],
                prochainePposition[1] - positionActuelle[1]
        };
    }

}