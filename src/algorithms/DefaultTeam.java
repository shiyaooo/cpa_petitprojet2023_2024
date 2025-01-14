package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/***************************************************************
 * TME 1: calcul de diamètre et de cercle couvrant minimum.    *
 *   - Trouver deux points les plus éloignés d'un ensemble de  *
 *     points donné en entrée.                                 *
 *   - Couvrir l'ensemble de poitns donné en entrée par un     *
 *     cercle de rayon minimum.                                *
 *                                                             *
 * class Circle:                                               *
 *   - Circle(Point c, int r) constructs a new circle          *
 *     centered at c with radius r.                            *
 *   - Point getCenter() returns the center point.             *
 *   - int getRadius() returns the circle radius.              *
 *                                                             *
 * class Line:                                                 *
 *   - Line(Point p, Point q) constructs a new line            *
 *     starting at p ending at q.                              *
 *   - Point getP() returns one of the two end points.         *
 *   - Point getQ() returns the other end point.               *
 ***************************************************************/
import supportGUI.Circle;
import supportGUI.Line;

public class DefaultTeam {

  /**
   * Calculer une paire de points de la liste, de distance maximum.
   * @param points : une liste de coordonnées de points en 2D
   * @return une paire de points de la liste, de distance maximum.
  **/
  public Line calculDiametre(ArrayList<Point> points) {
    if (points.size()<3) {
      return null;
    }

    Point p=points.get(0);
    Point q=points.get(1);

    // Recherche du diamètre
    Point maxp;
    Point maxq;
    maxp=p;
    maxq=q;
    double dist_max = maxp.distance(maxq);
    for(int i = 0;i<points.size();i++) {
 		Point p1 = points.get(i);
    	for(int j = 0;j<points.size();j++) {
    		Point p2 = points.get(j);
    		double dist = p1.distance(p2);
    		if(dist>dist_max) {
    			dist_max = dist;
    			maxp = p1;
    			maxq = p2;
    		}
    		
    	}
    }
    return new Line(maxp,maxq);
  }


  
  /**
   * Choisissez entre l'algorithme naïf et l'algorithme de Welzl pour calculer le plus petit cercle
   * @param points : une liste de coordonnées de points en 2D
   * @return un cercle couvrant tout point de la liste, de rayon minimum.
  **/
  public Circle calculCercleMin(ArrayList<Point> points) {
	 
	  if (points.isEmpty()) {
		  return null;
	  }
	  //return algo_Naif(points);
	  return algo_welzl(points);

  }

  
  /*==================================================Algorithme Naïf ==========================================================*/
  /**
   * Algorithme naïf pour calculer le plus petit cercle d'un ensemble de points
   * @param inputPoints : une liste de coordonnées de points en 2D
   * @return un cercle couvrant tout point de la liste, de rayon minimum
  **/
  public Circle algo_Naif(ArrayList<Point> inputPoints){
	  // Clone de la liste des points pour éviter la modification de la liste originale
	  ArrayList<Point> points = (ArrayList<Point>) inputPoints.clone();
	  
	  // Vérifie si l'ensemble de points est vide
	  if (points.size()<1) return null;
	  
	  // Variables pour stocker les coordonnées du cercle minimum
	  double cX,cY,cRadius,cRadiusSquared;
	  
	  // Première boucle parcourant toutes les paires de points
	  for (Point p: points){
		  for (Point q: points){
			  
			  // Calcul des coordonnées du centre du cercle et de son rayon
			  cX = .5*(p.x+q.x);
			  cY = .5*(p.y+q.y);
			  cRadiusSquared = 0.25*((p.x-q.x)*(p.x-q.x)+(p.y-q.y)*(p.y-q.y));
			  boolean allHit = true;
	          
			  // Vérifie si tous les autres points sont contenus à l'intérieur du cercle
			  for (Point s: points)
				  if ((s.x-cX)*(s.x-cX)+(s.y-cY)*(s.y-cY)>cRadiusSquared){
					  allHit = false;
					  break;
				  }
			  if (allHit) return new Circle(new Point((int)cX,(int)cY),(int)Math.sqrt(cRadiusSquared));
		  }
	  }
	  
	  // Recherche du cercle minimum parmi toutes les combinaisons de trois points
	  double resX=0;
	  double resY=0;
	  double resRadiusSquared=Double.MAX_VALUE;
	  for (int i=0;i<points.size();i++){
		  for (int j=i+1;j<points.size();j++){
			  for (int k=j+1;k<points.size();k++){
				  
				  // Sélection des trois points du triangle
				  Point p=points.get(i);
				  Point q=points.get(j);
				  Point r=points.get(k);
				  
				  //si les trois sont colineaires on passe
				  if ((q.x-p.x)*(r.y-p.y)-(q.y-p.y)*(r.x-p.x)==0) continue;
				  
				  //si p et q sont sur la meme ligne, ou p et r sont sur la meme ligne, on les echange
				  if ((p.y==q.y)||(p.y==r.y)) {
					  if (p.y==q.y){
						  p=points.get(k); //ici on est certain que p n'est sur la meme ligne de ni q ni r
						  r=points.get(i); //parce que les trois points sont non-colineaires
					  } else {
						  p=points.get(j); //ici on est certain que p n'est sur la meme ligne de ni q ni r
						  q=points.get(i); //parce que les trois points sont non-colineaires
					  }
				  }
				  
				  //on cherche les coordonnees du cercle circonscrit du triangle pqr
				  //soit m=(p+q)/2 et n=(p+r)/2
				  double mX=.5*(p.x+q.x);
				  double mY=.5*(p.y+q.y);
				  double nX=.5*(p.x+r.x);
				  double nY=.5*(p.y+r.y);
				  
				  //soit y=alpha1*x+beta1 l'equation de la droite passant par m et perpendiculaire a la droite (pq)
				  //soit y=alpha2*x+beta2 l'equation de la droite passant par n et perpendiculaire a la droite (pr)
				  double alpha1=(q.x-p.x)/(double)(p.y-q.y);
				  double beta1=mY-alpha1*mX;
				  double alpha2=(r.x-p.x)/(double)(p.y-r.y);
				  double beta2=nY-alpha2*nX;
				  
				  //le centre c du cercle est alors le point d'intersection des deux droites ci-dessus
				  cX=(beta2-beta1)/(double)(alpha1-alpha2);
				  cY=alpha1*cX+beta1;
				  cRadiusSquared=(p.x-cX)*(p.x-cX)+(p.y-cY)*(p.y-cY);
				  
	              // Vérifie si le cercle est plus petit que le cercle minimum actuel
				  if (cRadiusSquared>=resRadiusSquared) continue;
				  boolean allHit = true;
	              
				  // Vérifie si tous les autres points sont contenus à l'intérieur du cercle
				  for (Point s: points)
					  if ((s.x-cX)*(s.x-cX)+(s.y-cY)*(s.y-cY)>cRadiusSquared){
						  allHit = false;
						  break;
					  }
				  if (allHit) {
				  	//System.out.println("Found r="+Math.sqrt(cRadiusSquared));
				  	resX=cX;resY=cY;resRadiusSquared=cRadiusSquared;}
			  }
		  }
	  }
	  System.out.println("rayon =" + (int)Math.sqrt(resRadiusSquared));
	  return new Circle(new Point((int)resX,(int)resY),(int)Math.sqrt(resRadiusSquared));
      
   }
	
	
  /**
   * function procedure B_MINIDISK(P,R); comment: returns b_md(P,R)
		if P = Ø [or |R|=3] then
			D := b_md(Ø, R)
		else
			choose random p ∈ P;
 			D := B_MINIDISK(P -- {p}, R);
 			if [D defined and] p ∉ D then
				D := B_MINIDISK(P -- {p}, R ⋃ {p});
		return D;
	function procedure MINIDISK(P); comment: returns md(P)
		return B_MINIDTSK(P, Ø ) ;

   *
  **/


  /**
   * Algorithme de Welzl pour le calcul du cercle couvrant minimum
   * @param points : une liste de coordonnées de points en 2D
   * @return un cercle couvrant tout point de la liste, de rayon minimum
  **/
  public Circle algo_welzl(ArrayList<Point> points) {
	  return b_MINIDTSK(points, new ArrayList<Point>()) ;
  }
  
  /**
   * Algorithme de Welzl pour le calcul du cercle couvrant minimum
   * @param points : une liste de coordonnées de points en 2D
   * @param R : une liste de points sur le bord du cercle
   * @return un cercle couvrant tout point de la liste, de rayon minimum
  **/
  private Circle b_MINIDTSK(ArrayList<Point> points, ArrayList<Point> R ) {
	  
	  // Copie des points restants
	  ArrayList<Point> ps = new ArrayList<Point>(points);
	  
	  // Initialisation du cercle minimum couvrant
	  Circle D = null;
	  
	  // Si plus de points ou 3 points sur le bord
	  if(ps.isEmpty()|| R.size()==3) {
		  D = b_md(new ArrayList<Point>(),R); // Appel à la fonction auxiliaire pour les cas triviaux
	  }
	  else {
		  Point p = ps.get((new Random()).nextInt(ps.size())); // Choix aléatoire d'un point restant
		  ps.remove(p); // Retrait de ce point de la liste
		  D = b_MINIDTSK(ps, R); // Appel récursif avec un point en moins
		  
		  // Si le nouveau point n'est pas déjà inclus dans le cercle couvrant
		  if(D!=null && !pointInDisk(D,p)) {
			  R.add(p); // Ajout du point au bord du cercle
			  D = b_MINIDTSK(ps, R); // Appel récursif avec le nouveau point sur le bord
			  R.remove(p); // Retrait du point du bord du cercle
		  }
	  }
	  
	  // Retourne le cercle minimum couvrant
	  //System.out.println("rayon =" + D.getRadius());
	  return D;
  }
  
  /**
   * Fonction auxiliaire pour calculer les cercles minimaux dans les cas trivials
   * @param points : une liste de coordonnées de points en 2D
   * @param R : une liste de points sur le bord du cercle
   * @return un cercle couvrant minimum pour les cas trivials
  **/
  private Circle b_md(ArrayList<Point> points,ArrayList<Point> R) {
	  // Si aucune donnée de points et aucun point sur le bord
	  if(points.isEmpty() && R.size()==0) {
		  return new Circle(new Point(0,0), 0); // Aucun point donné, cercle de rayon nul au centre (0,0)
	  }
	  Circle d = null;
	  if(R.size()==1) {
		  //un seul point, alors c'est le centre du cercle
		  d = new Circle(R.get(0),0);
	  }
	  if(R.size()==2){
		  //diametre = la distance entre les 2 points
		  double ray = R.get(0).distance(R.get(1))/2;
		  
		  //le centre du cercle = le point au milieu de ces 2 points
		  double dx = (R.get(0).x+R.get(1).x)/2;
		  double dy = (R.get(0).y+R.get(1).y)/2;
		  Point p =new Point((int)dx,(int)dy);
		  
		  d = new Circle(p,(int)Math.ceil(ray));
	  }
	  else {
		  if(R.size()==3) {
			  //calculer le cercle circonscrit à ces trois points 
			  d =  circleThreePoints(R.get(0), R.get(1), R.get(2));
		  }
	  }
	  return d;
  }

  
  /*==================================================Méthode Auxiliaires ==========================================================*/

  /**
   * Fonction pour vérifier si un cercle couvre tous les points
   * @param points : une liste de coordonnées de points en 2D
   * @param c : une cercle qui contient les points
   * @return true si ce cercle contient tous les points que on veut tester sinon false
  **/
  public boolean containsAllPoints(ArrayList<Point> points, Circle c)
  {
	  Point center = c.getCenter();
	  int radius = c.getRadius();
	  for (Point p : points)
	  {
		  if (((center.x-p.x)*(center.x-p.x)+(center.y-p.y)*(center.y-p.y)) > (radius*radius))
		  {
			  return false;
		  }
	  }
	  return true;
  }
  
  /**
   * Fonction pour vérifier si un point est dans le cercle
   * @param c : une cercle qui contient les points
   * @param p : un point que l'on veut tester
   * @return true si le cercle c contient ce point p sinon false
  **/
  private boolean pointInDisk(Circle c, Point p) {
	  if((c!=null) &&(p!=null)) {
		  if(p.distance(c.getCenter())<=c.getRadius()) {
			  return true;
		  }
	  }
	  return false;
  }


  /**
   * Méthode pour calculer le cercle circonscrit à trois points donnés
   * @param p1 : le point p1
   * @param p2 : le point p2
   * @param p3 : le point p3
   * @return un cercle circonscrit à trois points p1, p2, p3
  **/
  private Circle circleThreePoints(Point p1, Point p2, Point p3) {
//	  if ((2*((p3.x-p1.x)*(p2.y-p1.y)-(p2.x-p1.x)*(p3.y-p1.y))==0) || (2*((p3.y-p1.y)*(p2.x-p1.x)-(p2.y-p1.y)*(p3.x-p1.x))==0)){
//		  System.out.print("is null");
//		  return null;
//	  }
//	  double x= ((p2.y-p1.y)*(p3.y*p3.y-p1.y*p1.y+p3.x*p3.x-p1.x*p1.x)-(p3.y-p1.y)*(p2.y*p2.y-p1.y*p1.y+p2.x*p2.x-p1.x*p1.x))/(2*((p3.x-p1.x)*(p2.y-p1.y)-(p2.x-p1.x)*(p3.y-p1.y)));
//	  double y= ((p2.x-p1.x)*(p3.x*p3.x-p1.x*p1.x+p3.y*p3.y-p1.y*p1.y)-(p3.x-p1.x)*(p2.x*p2.x-p1.x*p1.x+p2.y*p2.y-p1.y*p1.y))/(2*((p3.y-p1.y)*(p2.x-p1.x)-(p2.y-p1.y)*(p3.x-p1.x)));
	  Point p = getCentreThree(p1,p2,p3);
//	  //System.out.println("p = ("+x+","+y+")");
	  return new Circle(p, (int) Math.ceil(p.distance(p1)));
  }
  
  /**
   * Méthode pour calculer le centre d'un cercle circonscrit à trois points donnés
   * @param p1 : le point p1
   * @param p2 : le point p2
   * @param p3 : le point p3
   * @return un centre d'un circonscrit à trois points p1, p2, p3
  **/
  private Point getCentreThree(Point p, Point q, Point r) {
	  // 如果三点共线，则取两个点
      if ((q.x - p.x) * (r.y - p.y) - (q.y - p.y) * (r.x - p.x) == 0) {
    	  // 如果共线，找出最远的两个点
          double distPQ = Math.hypot(p.x - q.x, p.y - q.y);
          double distPR = Math.hypot(p.x - r.x, p.y - r.y);
          double distQR = Math.hypot(q.x - r.x, q.y - r.y);
	            
          Point point1 = p;
          Point point2 = q;
	            
         // 比较并确定最远的点对
         if (distPR > distPQ && distPR > distQR) {
        	 point2 = r;
    	 } else if (distQR > distPQ && distQR > distPR) {
    		 point1 = q;
             point2 = r;
         }
         return getCentreTwo(point1,point2);
      }
      if ((p.y == q.y) || (p.y == r.y)) {
    	  Point a;
          if (p.y == q.y) {
        	  a = p;
        	  p = r;
              r = a;
          } else {
              a = p;
              p = q;
              q = a;
          }
       }
	   double mX = .5 * (p.x + q.x);
       double mY = .5 * (p.y + q.y);
       double nX = .5 * (p.x + r.x);
       double nY = .5 * (p.y + r.y);
       double alpha1 = (q.x - p.x) / (double) (p.y - q.y);//斜率
       double beta1 = mY - alpha1 * mX;
       double alpha2 = (r.x - p.x) / (double) (p.y - r.y);
       double beta2 = nY - alpha2 * nX;
       double cX = (beta2 - beta1) / (double) (alpha1 - alpha2);
       double cY = alpha1 * cX + beta1;
       return new Point((int)cX,(int)cY);
	     
  }
  
  /**
   * Méthode pour calculer le centre d'un cercle circonscrit à deux points donnés
   * @param p1 : le point p1
   * @param p2 : le point p2
   * @param p3 : le point p3
   * @return un centre d'un circonscrit à deux points p1, p2, p3
  **/
  private static Point getCentreTwo(Point p,Point q) {
	  double cX,cY;
      cX = .5 * (p.x + q.x);
      cY = .5 * (p.y + q.y);
	  return new Point((int)cX,(int)cY);
	     
  }
 
}
