package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;
import java.util.Arrays;

public class DeterministicSelectTest {

    @Test
    public void testFixedMedians() {
        int[][] cases = {
                {7, 1, 3, 2, 6, 4, 8, 5, 9},
                {1},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {9, 8, 7, 6, 5, 4, 3, 2, 1},
                {1, 2, 3, 5, 4, 6, 7, 8, 9},
                {2, 2, 1, 3, 4, 5, 6, 7, 9},
                {-5, -1, -3, -2, -4}
        };
        for (int[] arr : cases) {
            int[] a = arr.clone();
            int[] sorted = arr.clone();
            Arrays.sort(sorted);
            int k = (arr.length - 1) / 2;
            int got = DeterministicSelect.select(a, k);
            assertEquals(sorted[k], got, "median failed: " + Arrays.toString(arr));
        }
    }

    @Test
    public void testManyKsRandom() {
        Random rnd = new Random(123);
        for (int t = 0; t < 50; t++) {
            int n = rnd.nextInt(200) + 1;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) arr[i] = rnd.nextInt(2000) - 1000;
            int[] sorted = arr.clone();
            Arrays.sort(sorted);
            for (int step = Math.max(1, n / 7), k = 0; k < n; k += step) {
                int got = DeterministicSelect.select(arr.clone(), k);
                assertEquals(sorted[k], got, "k=" + k + ", n=" + n);
            }
        }
    }

    @Test
    public void testDuplicates() {
        int[] arr = {5, 1, 5, 2, 5, 3, 5, 4, 5};
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        for (int k = 0; k < arr.length; k++) {
            int got = DeterministicSelect.select(arr.clone(), k);
            assertEquals(sorted[k], got, "k=" + k);
        }
    }

    @Test
    public void testBounds() {
        assertThrows(IllegalArgumentException.class, () -> DeterministicSelect.select(new int[]{}, 0));
        assertThrows(IllegalArgumentException.class, () -> DeterministicSelect.select(new int[]{1,2,3}, -1));
        assertThrows(IllegalArgumentException.class, () -> DeterministicSelect.select(new int[]{1,2,3}, 3));
    }
}
