package utils;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static algorithms.DefaultTeam.algoCalculCercleMinNaïf;
import static algorithms.DefaultTeam.algoCalculCercleMinWelzl;

public class Benchmark {
    public static File[] listeRepertoire(String path) {
        File repertoire = new File(path);
        File[] list =null;

        if ( repertoire.isDirectory ( ) ) {
            list = repertoire.listFiles();

        }
        return list ;
    }

    public static ArrayList<Point> readFile(String filename) {
        String line;
        String[] coordinates;
        ArrayList<Point> points=new ArrayList<Point>();
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filename))
            );
            try {
                while ((line=input.readLine())!=null) {
                    coordinates=line.split("\\s+");
                    points.add(new Point(Integer.parseInt(coordinates[0]),
                            Integer.parseInt(coordinates[1])));
                }

            } catch (IOException e) {
                System.err.println("Exception: interrupted I/O.");
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println("I/O exception: unable to close "+filename);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found.");
        }
        return points ;
    }

    public static void main(String[] args) {
        long instant0 = System.currentTimeMillis();
        String path = "samples/";
        File[] listFic = listeRepertoire(path);
        Arrays.sort(listFic, new Comparator<>() {
            public int compare(File f1, File f2) {
                int num1 = extractTestNumber(f1.getName());
                int num2 = extractTestNumber(f2.getName());
                return Integer.compare(num1, num2);
            }

            private int extractTestNumber(String filename) {
                int startIndex = filename.indexOf('-') + 1;
                int endIndex = filename.lastIndexOf('.');
                return Integer.parseInt(filename.substring(startIndex, endIndex));
            }
        });

        try (PrintWriter writer = new PrintWriter(new FileWriter("benchmark.csv"))) {
            writer.println("fichier,Naïf,Welzl");
            ArrayList<Point> points;
            for (int i = 0; i < listFic.length; i++) {
                File x = listFic[i];
                points = readFile(x.getPath());
                long instant1 = System.currentTimeMillis();
                algoCalculCercleMinNaïf(points);
                long instant2 = System.currentTimeMillis() - instant1;
                long instant3 = System.currentTimeMillis();
                algoCalculCercleMinWelzl(points);
                long instant4 = System.currentTimeMillis() - instant3;

                System.out.println(x.getPath() + " -> Algorithme Naïf = " + instant2);
                System.out.println("                      -> Algorithme Welzl = " + instant4);

                writer.println(x.getPath() + "," + instant2 + "," + instant4);
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
