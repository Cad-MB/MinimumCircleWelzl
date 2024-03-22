package utils;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static algorithms.DefaultTeam.algoCalculCercleMinNaïf;
import static algorithms.DefaultTeam.algoCalculCercleMinWelzl;

public class BenchmarkO {

    public static ArrayList<Point> readFile(String filename) {
        String line;
        String[] coordinates;
        ArrayList<Point> points = new ArrayList<>();
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filename))
            );
            try {
                while ((line = input.readLine()) != null) {
                    coordinates = line.split("\\s+");
                    points.add(new Point(Integer.parseInt(coordinates[0]),
                            Integer.parseInt(coordinates[1])));
                }

            } catch (IOException e) {
                System.err.println("Exception: interrupted I/O.");
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println("I/O exception: unable to close " + filename);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found.");
        }
        return points;
    }

    public static void main(String[] args) throws IOException {
        long instant0 = System.currentTimeMillis();
        try (PrintWriter writer = new PrintWriter(new FileWriter("benchmarkO.csv"))) {
            writer.println("nbr Points,Naïf,Welzl");
            ArrayList<Point> points;
            int[] indices = new int[10];
            for (int i = 0; i < indices.length; i++) {
                indices[i] = (i + 1) * 200;
            }
            points = readFile("input.points");
            for (int i = 0; i < indices.length; i++) {
                ArrayList<Point> subList = new ArrayList<>(points.subList(0, indices[i]));
                long instant1 = System.currentTimeMillis();
                algoCalculCercleMinNaïf(subList);
                long instant2 = System.currentTimeMillis() - instant1;
                long instant3 = System.currentTimeMillis();
                algoCalculCercleMinWelzl(subList);
                long instant4 = System.currentTimeMillis() - instant3;

                System.out.println(indices[i] + " -> Algorithme Naïf = " + instant2);
                System.out.println("                      -> Algorithme Welzl = " + instant4);

                writer.println(indices[i] + "," + instant2 + "," + instant4);
            }
        }
    }
}
