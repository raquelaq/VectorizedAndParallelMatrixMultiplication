package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelMatrixMultiplication {
    public static int[][] parallelMatrixMultiplication(int[][] A, int[][] B, int blockSize) {
        int rows = A.length;
        int cols = B[0].length;
        int commonDim = B.length;

        int[][] C = new int[rows][cols];

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < rows; i += blockSize) {
            for (int j = 0; j < cols; j += blockSize) {
                final int rowStart = i;
                final int colStart = j;

                executor.submit(() -> {
                    for (int row = rowStart; row < Math.min(rowStart + blockSize, rows); row++) {
                        for (int col = colStart; col < Math.min(colStart + blockSize, cols); col++) {
                            int sum = 0;
                            for (int k = 0; k < commonDim; k++) {
                                sum += A[row][k] * B[k][col];
                            }
                            C[row][col] = sum;
                        }
                    }
                });
            }
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
