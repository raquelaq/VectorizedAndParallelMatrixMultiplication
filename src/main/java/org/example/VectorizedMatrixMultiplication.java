package org.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class VectorizedMatrixMultiplication {
    public static int[][] parallelMatrixMultiplication(int[][] A, int[][] B) {
        int rows = A.length;
        int cols = B[0].length;
        int commonDim = B.length;

        int[][] C = new int[rows][cols];

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < rows; i++) {
            final int row = i;
            executor.submit(() -> {
                for (int j = 0; j < cols; j++) {
                    int sum = 0;
                    for (int k = 0; k < commonDim; k++) {
                        sum += A[row][k] * B[k][j];
                    }
                    C[row][j] = sum;
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return C;
    }
}
