package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;
import java.util.Arrays;

public class MergeSortTest {

    @Test
    void testFixedArrays() {
        int[][] cases = {
                {7,1,3,2,6,4,8,5,9},
                {},
                {4},
                {1,2,3,4,5,6,7,8,9},
                {9,8,7,6,5,4,3,2,1},
                {2,2,1,3,4,5,6,7,9}
        };
        for (int[] arr : cases) {
            int[] expected = arr.clone();
            Arrays.sort(expected);
            int[] a = arr.clone();
            MergeSort.mergesort(a);
            assertArrayEquals(expected, a);
        }
    }

    @Test
    void testRandomArrays() {
        Random rnd = new Random(12);
        for (int t = 0; t < 100; t++) {
            int n = rnd.nextInt(1000) + 1;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) arr[i] = rnd.nextInt(10000) - 5000;
            int[] expected = arr.clone();
            Arrays.sort(expected);
            int[] a = arr.clone();
            MergeSort.mergesort(a);
            assertArrayEquals(expected, a);
        }
    }
}
