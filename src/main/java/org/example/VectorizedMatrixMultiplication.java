package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class VectorizedMatrixMultiplication {
    public static int[][] vectorizedMatrixMultiplication(int[][] A, int[][] B) {
        int rows = A.length;
        int cols = B[0].length;
        int commonDim = B.length;

        int[][] C = new int[rows][cols];

        AtomicInteger rowCounter = new AtomicInteger(0);
        java.util.stream.IntStream.range(0, rows).parallel().forEach(row -> {
            for (int col = 0; col < cols; col++) {
                int sum = 0;
                for (int k = 0; k < commonDim; k++) {
                    sum += A[row][k] * B[k][col];
                }
                C[row][col] = sum;
            }
        });

        return C;
    }
}
