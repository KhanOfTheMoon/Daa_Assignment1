package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class ClosestPair {
    private static final String CSV_FILE = "closestpair_metrics.csv";

    public static final class Point {
        public final double x, y;
        public Point(double x, double y) { this.x = x; this.y = y; }
    }

    public static int currentDepth = 0;
    public static int maxDepth = 0;
    public static int allocations = 0;
    public static int comparisons = 0;

    public static void resetMetrics() {
        currentDepth = 0;
        maxDepth = 0;
        allocations = 0;
        comparisons = 0;
    }

    public static double findClosest(Point[] points) {
        Point[] px = points.clone();
        allocations++;
        Arrays.sort(px, Comparator.comparingDouble(p -> p.x));
        return closest(px, 0, px.length - 1);
    }

    public static double runWithCsv(Point[] points) {
        resetMetrics();
        long s = System.nanoTime();
        double ans = findClosest(points);
        long e = System.nanoTime();
        long ms = (e - s) / 1_000_000;
        writeCsv(points.length, ms);
        return ans;
    }

    private static double closest(Point[] a, int l, int r) {
        currentDepth++;
        if (currentDepth > maxDepth) maxDepth = currentDepth;
        if (r - l <= 3) {
            double m = Double.POSITIVE_INFINITY;
            for (int i = l; i <= r; i++)
                for (int j = i + 1; j <= r; j++) {
                    comparisons++;
                    double d = dist(a[i], a[j]);
                    if (d < m) m = d;
                }
            currentDepth--;
            return m;
        }
        int m = (l + r) / 2;
        double midX = a[m].x;
        double d = Math.min(closest(a, l, m), closest(a, m + 1, r));

        Point[] strip = new Point[r - l + 1];
        allocations++;
        int sz = 0;
        for (int i = l; i <= r; i++) {
            comparisons++;
            if (Math.abs(a[i].x - midX) < d) strip[sz++] = a[i];
        }
        Arrays.sort(strip, 0, sz, Comparator.comparingDouble(p -> p.y));
        for (int i = 0; i < sz; i++) {
            for (int j = i + 1; j < sz && (strip[j].y - strip[i].y) < d; j++) {
                comparisons++;
                double dd = dist(strip[i], strip[j]);
                if (dd < d) d = dd;
            }
        }
        currentDepth--;
        return d;
    }

    private static double dist(Point p, Point q) {
        double dx = p.x - q.x, dy = p.y - q.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static void writeCsv(int n, long timeMs) {
        ensureHeader(CSV_FILE, "Algorithm,n,time_ms,comparisons,allocations,max_depth\n");
        try (FileWriter fw = new FileWriter(CSV_FILE, true)) {
            fw.write(String.format("ClosestPair,%d,%d,%d,%d,%d%n",
                    n, timeMs, comparisons, allocations, maxDepth));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void ensureHeader(String path, String header) {
        File f = new File(path);
        if (!f.exists() || f.length() == 0) {
            try (FileWriter fw = new FileWriter(f, false)) {
                fw.write(header);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
