package org.example;

import static org.example.BasicMatrixMultiplication.*;

public class Main {
    public static void main(String[] args) {
        int[] sizes = {10};
        int blockSize = 50;
        for (int size : sizes) {
            ROWS = size;
            COLS = size;
            int[][] A = new int[ROWS][COLS];
            int[][] B = new int[ROWS][COLS];

            generateMatrix(A, 1, 9);
            generateMatrix(B, 1, 9);

            int[][] basic = matrixMultiplication(A, B);
            int[][] vectorized = VectorizedMatrixMultiplication.parallelMatrixMultiplication(A, B);
            int[][] parallel = ParallelMatrixMultiplication.parallelMatrixMultiplication(A, B, blockSize);

            System.out.println("Matrix Size: " + size + "\n");
            System.out.println("\nBasic Matrix Multiplication Result:");
            printMatrix(basic);
            System.out.println("\nVectorized Matrix Multiplication Result:");
            printMatrix(vectorized);
            System.out.println("\nParallel Matrix Multiplication Result:");
            printMatrix(parallel);
        }
    }
}