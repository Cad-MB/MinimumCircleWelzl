package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import GUI.Circle;
import GUI.Line;

/**
 * Implémente les algorithmes pour le calcul du diamètre et du cercle minimum à partir d'un ensemble de points.
 */
public class DefaultTeam {

  /**
   * Calcule le diamètre de l'ensemble de points donné.
   *
   * @param points Ensemble de points pour lesquels calculer le diamètre.
   * @return Un objet Line représentant le diamètre de l'ensemble de points.
   */
  public Line calculDiametre(ArrayList<Point> points) {
    return diametre(points);
  }

  /**
   * Calcule le cercle minimum englobant l'ensemble de points donné.
   *
   * @param points Ensemble de points pour lesquels calculer le cercle minimum.
   * @return Un objet Circle représentant le cercle minimum englobant les points.
   */
  public Circle calculCercleMinNaïf(ArrayList<Point> points) {
    if (points.isEmpty()) {
      return null;
    }
    points = quickHull(points);
    assert points != null;
    return algoCalculCercleMinNaïf(points);
  }

  public Circle calculCercleMinWlzl(ArrayList<Point> points) {
    if (points.isEmpty()) {
      return null;
    }
    points = quickHull(points);
    assert points != null;
    return algoCalculCercleMinWelzl(points);
  }

  /**
   * Applique l'algorithme Quick Hull pour calculer l'enveloppe convexe des points donnés.
   *
   * @param points Ensemble de points pour lesquels calculer l'enveloppe convexe.
   * @return Une ArrayList de points représentant l'enveloppe convexe.
   */
  public ArrayList<Point> quickHull(ArrayList<Point> points){
    if (points.size()<4) return points;

    Point ouest = points.get(0);
    Point sud = points.get(0);
    Point est = points.get(0);
    Point nord = points.get(0);
    for (Point p: points){
      if (p.x<ouest.x) ouest=p;
      if (p.y>sud.y) sud=p;
      if (p.x>est.x) est=p;
      if (p.y<nord.y) nord=p;
    }
    ArrayList<Point> result = new ArrayList<Point>();
    result.add(ouest);
    result.add(sud);
    result.add(est);
    result.add(nord);

    ArrayList<Point> rest = (ArrayList<Point>)points.clone();
    for (int i=0;i<rest.size();i++) {
      if (triangleContientPoint(ouest,sud,est,rest.get(i)) ||
              triangleContientPoint(ouest,est,nord,rest.get(i))) {
        rest.remove(i);
        i--;
      }
    }

    for (int i=0;i<result.size();i++) {
      Point a = result.get(i);
      Point b = result.get((i+1)%result.size());
      Point ref = result.get((i+2)%result.size());

      double signeRef = crossProduct(a,b,a,ref);
      double maxValue = 0;
      Point maxPoint = a;

      for (Point p: points) {
        double piki = crossProduct(a,b,a,p);
        if (signeRef*piki<0 && Math.abs(piki)>maxValue) {
          maxValue = Math.abs(piki);
          maxPoint = p;
        }
      }
      if (maxValue!=0){
        for (int j=0;j<rest.size();j++) {
          if (triangleContientPoint(a,b,maxPoint,rest.get(j))){
            rest.remove(j);
            j--;
          }
        }
        result.add(i+1,maxPoint);
        i--;
      }
    }
    return result;
  }

  /**
   * Vérifie si un point est contenu dans un triangle.
   *
   * @param a Premier sommet du triangle.
   * @param b Deuxième sommet du triangle.
   * @param c Troisième sommet du triangle.
   * @param x Point à vérifier.
   * @return True si le point est contenu dans le triangle, sinon False.
   */
  public boolean triangleContientPoint(Point a, Point b, Point c, Point x) {
    double l1 = ((b.y-c.y)*(x.x-c.x)+(c.x-b.x)*(x.y-c.y))/(double)((b.y-c.y)*(a.x-c.x)+(c.x-b.x)*(a.y-c.y));
    double l2 = ((c.y-a.y)*(x.x-c.x)+(a.x-c.x)*(x.y-c.y))/(double)((b.y-c.y)*(a.x-c.x)+(c.x-b.x)*(a.y-c.y));
    double l3 = 1-l1-l2;
    return (0<l1 && l1<1 && 0<l2 && l2<1 && 0<l3 && l3<1);
  }

  /**
   * Calcule le produit vectoriel entre deux vecteurs.
   *
   * @param p Premier point du premier vecteur.
   * @param q Deuxième point du premier vecteur.
   * @param s Premier point du deuxième vecteur.
   * @param t Deuxième point du deuxième vecteur.
   * @return La valeur du produit vectoriel.
   */
  public double crossProduct(Point p, Point q, Point s, Point t){
    return ((q.x-p.x)*(t.y-s.y)-(q.y-p.y)*(t.x-s.x));
  }

  /**
   * Calcule le diamètre de l'ensemble de points donné.
   *
   * @param points Ensemble de points pour lesquels calculer le diamètre.
   * @return Un objet Line représentant le diamètre de l'ensemble de points.
   */
  public Line diametre(ArrayList<Point> points) {
    if (points.size()<2) return null;
    Point p=points.get(0);
    Point q=points.get(1);
    for (Point s: points)
      for (Point t: points)
        if (s.distance(t)>p.distance(q))
        {p=s;q=t;}
    return new Line(p,q);
  }

  /**
   * Calcule le cercle minimum englobant l'ensemble de points donné de manière naïve.
   *
   * @param inputPoints Ensemble de points pour lesquels calculer le cercle minimum.
   * @return Un objet Circle représentant le cercle minimum englobant les points.
   */
  public static Circle algoCalculCercleMinNaïf(ArrayList<Point> inputPoints){
    ArrayList<Point> points = (ArrayList<Point>) inputPoints.clone();
    if (points.size()<1) return null;
    double cX,cY,cRadius,cRadiusSquared;
    for (Point p: points){
      for (Point q: points){
        cX = .5*(p.x+q.x);
        cY = .5*(p.y+q.y);
        cRadiusSquared = 0.25*((p.x-q.x)*(p.x-q.x)+(p.y-q.y)*(p.y-q.y));
        boolean allHit = true;
        for (Point s: points)
          if ((s.x-cX)*(s.x-cX)+(s.y-cY)*(s.y-cY)>cRadiusSquared){
            allHit = false;
            break;
          }
        if (allHit) return new Circle(new Point((int)cX,(int)cY),(int)Math.sqrt(cRadiusSquared));
      }
    }
    double resX=0;
    double resY=0;
    double resRadiusSquared=Double.MAX_VALUE;
    for (int i=0;i<points.size();i++){
      for (int j=i+1;j<points.size();j++){
        for (int k=j+1;k<points.size();k++){
          Point p=points.get(i);
          Point q=points.get(j);
          Point r=points.get(k);
          //si les trois sont colinéaires on passe
          if ((q.x-p.x)*(r.y-p.y)-(q.y-p.y)*(r.x-p.x)==0) continue;
          //si p et q sont sur la même ligne, ou p et r sont sur la même ligne, on les échange
          if ((p.y==q.y)||(p.y==r.y)) {
            if (p.y==q.y){
              p=points.get(k); //ici on est certain que p n'est sur la même ligne de ni q ni r
              r=points.get(i); //parce que les trois points sont non-colinéaires
            } else {
              p=points.get(j); //ici on est certain que p n'est sur la même ligne de ni q ni r
              q=points.get(i); //parce que les trois points sont non-colinéaires
            }
          }
          //on cherche les coordonnées du cercle circonscrit du triangle pqr
          //soit m=(p+q)/2 et n=(p+r)/2
          double mX=.5*(p.x+q.x);
          double mY=.5*(p.y+q.y);
          double nX=.5*(p.x+r.x);
          double nY=.5*(p.y+r.y);
          //soit y=alpha1*x+beta1 l'équation de la droite passant par m et perpendiculaire à la droite (pq)
          //soit y=alpha2*x+beta2 l'équation de la droite passant par n et perpendiculaire à la droite (pr)
          double alpha1=(q.x-p.x)/(double)(p.y-q.y);
          double beta1=mY-alpha1*mX;
          double alpha2=(r.x-p.x)/(double)(p.y-r.y);
          double beta2=nY-alpha2*nX;
          //le centre c du cercle est alors le point d'intersection des deux droites ci-dessus
          cX=(beta2-beta1)/(double)(alpha1-alpha2);
          cY=alpha1*cX+beta1;
          cRadiusSquared=(p.x-cX)*(p.x-cX)+(p.y-cY)*(p.y-cY);
          if (cRadiusSquared>=resRadiusSquared) continue;
          boolean allHit = true;
          for (Point s: points)
            if ((s.x-cX)*(s.x-cX)+(s.y-cY)*(s.y-cY)>cRadiusSquared){
              allHit = false;
              break;
            }
          if (allHit) {
            //System.out.println("Found r="+Math.sqrt(cRadiusSquared));
            resX=cX;
            resY=cY;
            resRadiusSquared=cRadiusSquared;
          }
        }
      }
    }
    return new Circle(new Point((int)resX,(int)resY),(int)Math.sqrt(resRadiusSquared));
  }

  /**
   * Calcule le cercle minimum englobant l'ensemble de points donné selon l'algorithme de Welzl.
   *
   * @param points Ensemble de points pour lesquels calculer le cercle minimum.
   * @return Un objet Circle représentant le cercle minimum englobant les points.
   */
  public static Circle algoCalculCercleMinWelzl(ArrayList<Point> points) {
    return bMinDisk(points, new ArrayList<Point>());
  }

  /**
   * Calcule le cercle minimum englobant l'ensemble de points donné selon l'algorithme de Welzl.
   * Cette méthode est utilisée récursivement.
   *
   * @param Ps Ensemble de points pour lesquels calculer le cercle minimum.
   * @param R Ensemble de points déjà sélectionnés pour former le cercle minimum.
   * @return Un objet Circle représentant le cercle minimum englobant les points.
   */
  public static Circle bMinDisk(ArrayList<Point> Ps, ArrayList<Point> R) {
    ArrayList<Point> P = new ArrayList<Point>(Ps);
    Random r = new Random();
    Circle D = null;

    if (P.isEmpty() || R.size() == 3) {
      D = bmd(new ArrayList<Point>(), R);

    } else {
      Point p = P.get((r.nextInt(P.size())));
      P.remove(p);

      D = bMinDisk(P, R);
      if (D != null && !pointInCircle(D, p)) {
        R.add(p);
        D = bMinDisk(P, R);
        R.remove(p);
      }
    }

    return D;
  }

  /**
   * Calcule le cercle minimum englobant l'ensemble de points donné à l'aide du cercle de Welzl.
   *
   * @param P Ensemble de points pour lesquels calculer le cercle minimum.
   * @param R Ensemble de points déjà sélectionnés pour former le cercle minimum.
   * @return Un objet Circle représentant le cercle minimum englobant les points.
   */
  public static Circle bmd(ArrayList<Point> P, ArrayList<Point> R) {
    if (P.isEmpty() && R.size() == 0)
      return new Circle(new Point(0, 0), 10);
    Random r = new Random();
    Circle D = null;
    if (R.size() == 1) {
      D = new Circle(R.get(0), 0);
    }
    if (R.size() == 2) {

      double cx = (R.get(0).x + R.get(1).x) / 2;
      double cy = (R.get(0).y + R.get(1).y) / 2;
      double d = R.get(0).distance(R.get(1)) / 2;
      Point p = new Point((int) cx, (int) cy);
      D = new Circle(p, (int) Math.ceil(d));
    } else {
      if (R.size() == 3)
        D = calculateCircumcircle(R.get(0), R.get(1), R.get(2));
    }
    return D;
  }

  /**
   * Vérifie si un point donné est situé à l'intérieur ou sur le bord d'un cercle donné.
   *
   * @param c Le cercle à vérifier.
   * @param p Le point à vérifier.
   * @return true si le point est à l'intérieur ou sur le bord du cercle, sinon false.
   */
  public static boolean pointInCircle(Circle c, Point p) {
    // Calcul de la distance entre le point p et le centre du cercle c
    double distance = p.distance(c.getCenter());

    // Soustraction du rayon du cercle de la distance calculée
    double result = distance - c.getRadius();

    // Comparaison avec une valeur très petite pour gérer les erreurs de précision numérique
    if (result < 0.00001) {
      return true; // Le point p est contenu dans le cercle c
    } else {
      return false; // Le point p n'est pas contenu dans le cercle c
    }
  }

  /**
   * Calcule le cercle circonscrit d'un triangle donné par trois points.
   *
   * @param a Premier point du triangle.
   * @param b Deuxième point du triangle.
   * @param c Troisième point du triangle.
   * @return Un objet Circle représentant le cercle circonscrit au triangle.
   */
  public static Circle calculateCircumcircle(Point a, Point b, Point c) {
    // Calcul de deux fois l'aire du triangle formé par les points a, b et c
    double d = 2 * ((a.x * (b.y - c.y)) + (b.x * (c.y - a.y)) + (c.x * (a.y - b.y)));

    // Vérification si les points sont alignés
    if (d == 0) {
      return null; // Les points sont alignés, donc le cercle circonscrit ne peut pas être trouvé
    }

    // Calcul des coordonnées du centre du cercle circonscrit
    double x = ((((a.x * a.x) + (a.y * a.y)) * (b.y - c.y))
            + (((b.x * b.x) + (b.y * b.y)) * (c.y - a.y))
            + (((c.x * c.x) + (c.y * c.y)) * (a.y - b.y))) / d;

    double y = ((((a.x * a.x) + (a.y * a.y)) * (c.x - b.x))
            + (((b.x * b.x) + (b.y * b.y)) * (a.x - c.x))
            + (((c.x * c.x) + (c.y * c.y)) * (b.x - a.x))) / d;

    // Création du point centre
    Point p = new Point((int) x, (int) y);

    // Calcul du rayon (distance entre le centre et l'un des points donnés, arrondie à l'entier supérieur)
    int radius = (int) Math.ceil(p.distance(a));

    // Création et retour du cercle
    Circle circumcircle = new Circle(p, radius);
    return circumcircle;
  }
}