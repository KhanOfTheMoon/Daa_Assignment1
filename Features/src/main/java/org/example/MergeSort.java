package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MergeSort {
    private static final String CSV_FILE = "mergesort_metrics.csv";

    static int currentDepth = 0;
    static int maxDepth = 0;
    static int allocation = 0;
    static int comparisons = 0;

    public static void resetMetrics() {
        comparisons = 0;
        maxDepth = 0;
        allocation = 0;
        currentDepth = 0;
    }

    public static void mergesort(int[] arr) {
        currentDepth++;
        if (currentDepth > maxDepth) maxDepth = currentDepth;
        int len = arr.length;
        if (len <= 1) {
            currentDepth--;
            return;
        }
        int mid = len / 2;
        int[] left = new int[mid];
        allocation += left.length;
        int[] right = new int[len - mid];
        allocation += right.length;
        int j = 0;
        for (int i = 0; i < len; i++) {
            if (i < mid) left[i] = arr[i];
            else right[j++] = arr[i];
        }
        mergesort(left);
        mergesort(right);
        merge(left, right, arr);
        currentDepth--;
    }

    public static void merge(int[] leftArray, int[] rightArray, int[] array) {
        int leftLen = leftArray.length;
        int rightLen = rightArray.length;
        int i = 0, l = 0, r = 0;
        while (l < leftLen && r < rightLen) {
            comparisons++;
            if (leftArray[l] <= rightArray[r]) array[i++] = leftArray[l++];
            else array[i++] = rightArray[r++];
        }
        while (l < leftLen) array[i++] = leftArray[l++];
        while (r < rightLen) array[i++] = rightArray[r++];
    }

    public static void runWithCsv(int[] arr) {
        resetMetrics();
        long s = System.nanoTime();
        mergesort(arr);
        long e = System.nanoTime();
        long ms = (e - s) / 1_000_000;
        writeCsvRow(arr.length, ms);
    }

    private static void writeCsvRow(int n, long timeMs) {
        ensureHeader(CSV_FILE, "Algorithm,n,time_ms,comparisons,allocations,max_depth\n");
        try (FileWriter fw = new FileWriter(CSV_FILE, true)) {
            fw.write(String.format("MergeSort,%d,%d,%d,%d,%d%n",
                    n, timeMs, comparisons, allocation, maxDepth));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void ensureHeader(String path, String header) {
        File f = new File(path);
        if (!f.exists() || f.length() == 0) {
            try (FileWriter fw = new FileWriter(f, false)) {
                fw.write(header);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
