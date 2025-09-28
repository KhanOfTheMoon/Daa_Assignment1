package org.example;

import java.util.Random;

public class QuickSort {
    public static int comparisons = 0;
    public static int allocations = 0;
    public static int currentDepth = 0;
    public static int maxDepth = 0;

    private static final Random RAND = new Random();

    public static void resetMetrics() {
        comparisons = 0;
        allocations = 0;
        currentDepth = 0;
        maxDepth = 0;
    }

    // оставляем для тестов/демо, НИЧЕГО не пишет в файлы
    public static void sort(int[] arr) {
        resetMetrics();
        qs(arr, 0, arr.length - 1, 1);
    }

    // обёртка, которую зовёт AlgoBenchCLI (тоже без файлов)
    public static void quickSort(int[] arr, int left, int right) {
        qs(arr, left, right, 1);
    }

    private static void qs(int[] a, int l, int r, int depth) {
        if (depth > maxDepth) maxDepth = depth;
        while (l < r) {
            int p = part(a, l, r);
            int ls = p - l, rs = r - p;
            if (ls < rs) {
                if (l < p - 1) qs(a, l, p - 1, depth + 1);
                l = p + 1;
            } else {
                if (p + 1 < r) qs(a, p + 1, r, depth + 1);
                r = p - 1;
            }
        }
    }

    private static int part(int[] a, int l, int r) {
        int pi = l + RAND.nextInt(r - l + 1);
        int pv = a[pi];
        swap(a, pi, r);
        int s = l;
        for (int i = l; i < r; i++) {
            comparisons++;
            if (a[i] <= pv) swap(a, i, s++);
        }
        swap(a, s, r);
        return s;
    }

    private static void swap(int[] a, int i, int j) {
        if (i == j) return;
        allocations++;
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }
}
