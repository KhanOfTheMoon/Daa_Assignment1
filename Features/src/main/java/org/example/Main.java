package org.example;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] arr1 = {12, 43, 1, 8, 34, 23};
        int[] arr2 = {7, 2, 19, 5, 10, 3};
        int[] arr3 = {15, 6, 27, 11, 9, 4};

        System.out.println("Original arrays:");
        System.out.println("arr1: " + Arrays.toString(arr1));
        System.out.println("arr2: " + Arrays.toString(arr2));
        System.out.println("arr3: " + Arrays.toString(arr3));

        int[] mergeSorted = arr1.clone();
        MergeSort.resetMetrics();
        MergeSort.mergesort(mergeSorted);
        System.out.println("MergeSort arr1: " + Arrays.toString(mergeSorted));

        int[] quickSorted = arr2.clone();
        QuickSort.resetMetrics();
        if (quickSorted.length > 0) QuickSort.quickSort(quickSorted, 0, quickSorted.length - 1);
        System.out.println("QuickSort arr2: " + Arrays.toString(quickSorted));

        int[] selectArr = arr3.clone();
        int k = selectArr.length / 2;
        int median = DeterministicSelect.select(selectArr, k);
        System.out.println("DeterministicSelect arr3 median: " + median);

        double[][] points = {
                {2.0, 3.0},
                {5.0, 4.0},
                {1.0, 2.0},
                {6.0, 1.0}
        };
        ClosestPair.resetMetrics();
        ClosestPair.Point[] pts = new ClosestPair.Point[points.length];
        for (int i = 0; i < points.length; i++) {
            pts[i] = new ClosestPair.Point(points[i][0], points[i][1]);
        }
        double minDist = ClosestPair.findClosest(pts);
        System.out.println("ClosestPair min distance: " + minDist);

        AlgoBenchCLI.main(args);
    }
}
