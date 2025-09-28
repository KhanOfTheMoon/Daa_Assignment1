package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class AlgoBenchCLI {
    public static void main(String[] args) {
        int[] sizes = {10, 100, 1000, 5000};
        String[] algs = {"MergeSort", "QuickSort", "DeterministicSelect", "ClosestPair"};

        double[][] timeMs = new double[sizes.length][4];
        long[][] comparisons = new long[sizes.length][4];
        long[][] allocations = new long[sizes.length][4];
        long[][] maxDepth = new long[sizes.length][4];

        Random rnd = new Random(2025);

        for (int si = 0; si < sizes.length; si++) {
            int n = sizes[si];

            int[] base = new int[n];
            for (int i = 0; i < n; i++) base[i] = rnd.nextInt(10000) - 5000;

            // MergeSort
            int[] ms = base.clone();
            MergeSort.resetMetrics();
            long t1s = System.nanoTime();
            MergeSort.mergesort(ms);
            long t1e = System.nanoTime();
            timeMs[si][0] = (t1e - t1s) / 1_000_000.0;
            comparisons[si][0] = MergeSort.comparisons;
            allocations[si][0] = MergeSort.allocation;
            maxDepth[si][0] = MergeSort.maxDepth;

            // QuickSort (через quickSort, без каких-либо CSV побочно)
            int[] qs = base.clone();
            QuickSort.resetMetrics();
            long t2s = System.nanoTime();
            if (qs.length > 0) QuickSort.quickSort(qs, 0, qs.length - 1);
            long t2e = System.nanoTime();
            timeMs[si][1] = (t2e - t2s) / 1_000_000.0;
            comparisons[si][1] = QuickSort.comparisons;
            allocations[si][1] = QuickSort.allocations;
            maxDepth[si][1] = QuickSort.maxDepth;

            // Deterministic Select
            int[] ds = base.clone();
            int k = n == 0 ? 0 : (n - 1) / 2;
            long t3s = System.nanoTime();
            if (n > 0) DeterministicSelect.select(ds, k);
            long t3e = System.nanoTime();
            timeMs[si][2] = (t3e - t3s) / 1_000_000.0;
            comparisons[si][2] = DeterministicSelect.comparisons;
            allocations[si][2] = DeterministicSelect.allocations;
            maxDepth[si][2] = DeterministicSelect.maxDepth;

            // Closest Pair
            ClosestPair.Point[] pts = new ClosestPair.Point[Math.max(n, 2)];
            for (int i = 0; i < pts.length; i++) {
                double x = rnd.nextDouble() * 10000 - 5000;
                double y = rnd.nextDouble() * 10000 - 5000;
                pts[i] = new ClosestPair.Point(x, y);
            }
            ClosestPair.resetMetrics();
            long t4s = System.nanoTime();
            ClosestPair.findClosest(pts);
            long t4e = System.nanoTime();
            timeMs[si][3] = (t4e - t4s) / 1_000_000.0;
            comparisons[si][3] = ClosestPair.comparisons;
            allocations[si][3] = ClosestPair.allocations;
            maxDepth[si][3] = ClosestPair.maxDepth;
        }

        saveWideCSV(sizes, algs, timeMs, comparisons, allocations, maxDepth, "benchmark_wide.csv");
    }

    private static void saveWideCSV(int[] sizes, String[] algs,
                                    double[][] timeMs, long[][] comparisons,
                                    long[][] allocations, long[][] maxDepth,
                                    String filename) {
        try (FileWriter w = new FileWriter(filename)) {
            writeMetric(w, "TimeMs", sizes, algs, timeMs);
            writeMetric(w, "Comparisons", sizes, algs, comparisons);
            writeMetric(w, "Allocations", sizes, algs, allocations);
            writeMetric(w, "MaxDepth", sizes, algs, maxDepth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeMetric(FileWriter w, String title, int[] sizes, String[] algs,
                                    double[][] vals) throws IOException {
        w.write(title + "\n");
        w.write("Size");
        for (String a : algs) w.write("," + a);
        w.write("\n");
        for (int i = 0; i < sizes.length; i++) {
            w.write(Integer.toString(sizes[i]));
            for (int j = 0; j < algs.length; j++) w.write("," + vals[i][j]);
            w.write("\n");
        }
        w.write("\n");
    }

    private static void writeMetric(FileWriter w, String title, int[] sizes, String[] algs,
                                    long[][] vals) throws IOException {
        w.write(title + "\n");
        w.write("Size");
        for (String a : algs) w.write("," + a);
        w.write("\n");
        for (int i = 0; i < sizes.length; i++) {
            w.write(Integer.toString(sizes[i]));
            for (int j = 0; j < algs.length; j++) w.write("," + vals[i][j]);
            w.write("\n");
        }
        w.write("\n");
    }
}
