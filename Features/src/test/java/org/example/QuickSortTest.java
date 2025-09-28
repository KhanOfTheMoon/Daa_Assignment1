package org.example;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class QuickSortTest {

    private static int[] sortedCopy(int[] a) {
        int[] b = a.clone();
        Arrays.sort(b);
        return b;
    }

    @Test
    void trivialAndEdge() {
        int[][] cases = {
                new int[]{},
                new int[]{4},
                new int[]{5,5,5,5,5,5,5,5,5},
                new int[]{1,2,3,4,5,6,7,8,9},
                new int[]{9,8,7,6,5,4,3,2,1},
                new int[]{2,2,1,3,4,5,6,7,9},
                new int[]{1,2,3,5,4,6,7,8,9},
                new int[]{7,1,3,2,6,4,8,5,9},
                new int[]{0,-1,-1,2,-3,5,-8,13,-21}
        };
        for (int[] a : cases) {
            int[] expect = sortedCopy(a);
            int[] got = a.clone();
            QuickSort.sort(got);
            assertArrayEquals(expect, got);
        }
    }

    @RepeatedTest(30)
    void randomBatches() {
        Random rnd = new Random(2025);
        int n = rnd.nextInt(3000) + 1;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt(200_000) - 100_000;
        int[] expect = sortedCopy(a);
        int[] got = a.clone();
        QuickSort.sort(got);
        assertArrayEquals(expect, got);
    }
}
