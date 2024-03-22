package utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Plot {
    public static void main(String[] args) {
        // Read the CSV file into a DefaultCategoryDataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DefaultCategoryDataset datasetO = new DefaultCategoryDataset();
        DefaultCategoryDataset gainDataset = new DefaultCategoryDataset(); // Dataset for gain in time
        DefaultCategoryDataset datasetNaif = new DefaultCategoryDataset(); // Dataset for gain in time
        DefaultCategoryDataset datasetWelzl = new DefaultCategoryDataset(); // Dataset for gain in time
        DefaultCategoryDataset datasetNaifO = new DefaultCategoryDataset(); // Dataset for gain in time
        DefaultCategoryDataset datasetWelzlO = new DefaultCategoryDataset(); // Dataset for gain in time
        double naiveAverage = 0;
        double welzlAverage = 0;
        int size=0;

        try {
            BufferedReader br = new BufferedReader(new FileReader("benchmark.csv"));
            BufferedReader brO = new BufferedReader(new FileReader("benchmarkO.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String file = values[0];

                // Skip the header line and non-numeric labels
                if (file.equals("fichier")) continue;
                double naiveTime = Double.parseDouble(values[1]);
                double welzlTime = Double.parseDouble(values[2]);
                dataset.addValue(naiveTime, "Naïf", file);
                dataset.addValue(welzlTime, "Welzl", file);

                size++;
                naiveAverage = naiveAverage + naiveTime;
                welzlAverage = welzlAverage + welzlTime;
                double gain = naiveTime - welzlTime;
                datasetNaif.addValue(naiveTime, "Naïf", file);
                datasetWelzl.addValue(welzlTime, "Welzl", file);
                gainDataset.addValue(gain, "Gain", file);
            }
            while ((line = brO.readLine()) != null) {
                String[] values = line.split(",");
                String file = values[0];

                // Skip the header line and non-numeric labels
                if (file.equals("nbr Points")) continue;
                double naiveTimeO = Double.parseDouble(values[1]);
                double welzlTimeO = Double.parseDouble(values[2]);
                datasetO.addValue(naiveTimeO, "Naïf", file);
                datasetO.addValue(welzlTimeO, "Welzl", file);

                datasetNaifO.addValue(naiveTimeO, "Naïf", file);
                datasetWelzlO.addValue(welzlTimeO, "Welzl", file);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the chart for benchmark results
        JFreeChart benchmarkChart = ChartFactory.createLineChart(
                "Benchmark Results",  // chart title
                "Size",               // domain axis label
                "Time (milliseconds)", // range axis label
                datasetO,              // data
                PlotOrientation.VERTICAL,
                true,                 // include legend
                true,                 // tooltips
                false                 // urls
        );

        // Create the chart for benchmark results Naif
        JFreeChart benchmarkChartNaif = ChartFactory.createLineChart(
                "Benchmark Results (Naif)",  // chart title
                "Size",               // domain axis label
                "Time (milliseconds)", // range axis label
                datasetNaifO,              // data
                PlotOrientation.VERTICAL,
                true,                 // include legend
                true,                 // tooltips
                false                 // urls
        );

        // Create the chart for benchmark results Welzl
        JFreeChart benchmarkChartWelzl = ChartFactory.createLineChart(
                "Benchmark Results (Welzl)",  // chart title
                "Size",               // domain axis label
                "Time (milliseconds)", // range axis label
                datasetWelzlO,              // data
                PlotOrientation.VERTICAL,
                true,                 // include legend
                true,                 // tooltips
                false                 // urls
        );

        // Create the chart for gain in time
        JFreeChart gainChart = ChartFactory.createBarChart(
                "Gain in Time (Welzl vs Naïf)",  // chart title
                "File",                           // domain axis label
                "Time Saved (milliseconds)",       // range axis label
                gainDataset,                      // data
                PlotOrientation.VERTICAL,
                true,                             // include legend
                true,                             // tooltips
                false                             // urls
        );

        // Customize benchmark chart
        benchmarkChart.setBackgroundPaint(Color.white);
        benchmarkChart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        // Customize benchmark chart Naif
        benchmarkChartNaif.setBackgroundPaint(Color.white);
        benchmarkChartNaif.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        // Customize benchmark chart Welzl
        benchmarkChartWelzl.setBackgroundPaint(Color.white);
        benchmarkChartWelzl.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        // Customize gain chart
        gainChart.setBackgroundPaint(Color.white);
        gainChart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        // Display charts in frames
        JFrame benchmarkFrame = new JFrame("Benchmark Results");
        benchmarkFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        benchmarkFrame.getContentPane().add(new ChartPanel(benchmarkChart));
        benchmarkFrame.pack();
        benchmarkFrame.setVisible(true);

        // Display charts in frames Naif
        JFrame naifFrame = new JFrame("Benchmark Results (Naif)");
        naifFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        naifFrame.getContentPane().add(new ChartPanel(benchmarkChartNaif));
        naifFrame.pack();
        naifFrame.setVisible(true);

        // Display charts in frames Welzl
        JFrame welzlFrame = new JFrame("Benchmark Results (Welzl)");
        welzlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welzlFrame.getContentPane().add(new ChartPanel(benchmarkChartWelzl));
        welzlFrame.pack();
        welzlFrame.setVisible(true);

        JFrame gainFrame = new JFrame("Gain in Time (Welzl vs Naïf)");
        gainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gainFrame.getContentPane().add(new ChartPanel(gainChart));
        gainFrame.pack();
        gainFrame.setVisible(true);

        // Calculer la moyenne de tous les temps pour l'algorithme Naïf
        naiveAverage = naiveAverage/size;
        System.out.println(naiveAverage);

// Calculer la moyenne de tous les temps pour l'algorithme de Welzl
        welzlAverage = welzlAverage/size;
        System.out.println(welzlAverage);

// Créer un dataset pour les moyennes
        DefaultCategoryDataset averageDataset = new DefaultCategoryDataset();
        averageDataset.addValue(naiveAverage, "Naïf", "");
        averageDataset.addValue(welzlAverage, "Welzl", "");

// Créer le diagramme en bâtons pour comparer les moyennes
        JFreeChart averageChart = ChartFactory.createBarChart(
                "Comparison of Averages (Naïf vs Welzl)",  // titre du graphique
                "",                                         // label de l'axe des abscisses
                "Average Time (milliseconds)",              // label de l'axe des ordonnées
                averageDataset,                            // données
                PlotOrientation.VERTICAL,
                true,                                     // inclure la légende
                true,                                     // activer les infobulles
                false                                     // activer les URLs
        );

// Personnaliser le graphique des moyennes
        averageChart.setBackgroundPaint(Color.white);

// Afficher le graphique des moyennes dans un frame
        JFrame averageFrame = new JFrame("Comparison of Averages (Naïf vs Welzl)");
        averageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        averageFrame.getContentPane().add(new ChartPanel(averageChart));
        averageFrame.pack();
        averageFrame.setVisible(true);

    }


}
