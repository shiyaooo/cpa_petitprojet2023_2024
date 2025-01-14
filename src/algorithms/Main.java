package algorithms;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import supportGUI.Circle;

public class Main {
	 public static void main(String[] args) {
		 DefaultTeam dt = new DefaultTeam() ;
		// Spécifiez le répertoire contenant les fichiers de points
	    //File directory = new File("samples/");
	    File directory = new File("samples/");
	        
	    // Liste des fichiers dans le répertoire
	    File[] files = directory.listFiles();
	    
		//.ArrayList<Point> points = new ArrayList<Point>() ;

		if (files != null) {
			try {
                FileWriter writer = new FileWriter("temps_execution.txt"); 
                //FileWriter writer = new FileWriter("temps_ev.txt"); 
                writer.write("Fichier          " + "Welzl(nanosecondes)    " + "Naif(nanosecondes)\n" );
                //writer.write("Fichier          "+ "Naif(nanosecondes)            \n" );

	            for (File file : files) {
	            	// Lire les points à partir du fichier
	            	ArrayList<Point> points = readPointsFromFile(file);
	            	
	            	// Mesurer le temps d'exécution pour chaque algorithme
	           
	            	/***algorithme de Welzl***/
	                long startTimeWelzl = System.nanoTime();
	                Circle resultWelzl = dt.algo_welzl(points);
	                long endTimeWelzl = System.nanoTime();
	                long elapsedTimeWelzl = endTimeWelzl - startTimeWelzl;
	                
	                /***algorithme naïf***/
	                long startTimeNaif = System.nanoTime();
	                Circle resultNaif = dt.algo_Naif(points);
	                long endTimeNaif = System.nanoTime();
	                long elapsedTimeNaif = endTimeNaif - startTimeNaif;

	                double raduis_naif = resultNaif.getRadius();
	                double raduis_welzl = resultWelzl.getRadius();
	                
	                System.out.println("Temps d'exécution de l'algorithme pour le fichier " + file.getName() + " welzl " + elapsedTimeWelzl + " nanosecondes ;  naif: " + elapsedTimeNaif + " nanosecondes");
	                writer.write(file.getName() + "      "+elapsedTimeWelzl + "                 " + elapsedTimeNaif + "\n");
	                //System.out.println("Temps d'exécution de l'algorithme pour le fichier " + file.getName() + "  naif: " + elapsedTimeNaif + " nanosecondes");
	                //writer.write(file.getName() +  "                 " + elapsedTimeNaif + "\n");
	            }
	            writer.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
		}    
	 }
	 
	 // Fonction pour lire les points à partir d'un fichier
	 private static ArrayList<Point> readPointsFromFile(File file) {
		 ArrayList<Point> points = new ArrayList<>();
	        try (Scanner scanner = new Scanner(file)) {
	            while (scanner.hasNextLine()) {
	                String[] coordinates = scanner.nextLine().split(" ");
	                int x = Integer.parseInt(coordinates[0]);
	                int y = Integer.parseInt(coordinates[1]);
	                points.add(new Point(x, y));
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return points;
    }
		

}
