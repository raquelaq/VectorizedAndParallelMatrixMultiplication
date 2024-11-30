package org.example;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

import java.util.Random;

public class BasicMatrixMultiplication {
    static int ROWS;
    static int COLS;

    int[][] A = new int[ROWS][COLS];
    int[][] B = new int[ROWS][COLS];

    @Setup(Level.Trial)
    public void setUp() {
        generateMatrix(A, 1, 9);
        generateMatrix(B, 1, 9);
    }

    public static void generateMatrix(int[][] matrix, int min, int max) {
        Random random = new Random();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                matrix[i][j] = random.nextInt(max - min + 1) + min;
            }
        }
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    public static int[][] matrixMultiplication(int[][] A, int[][] B) {
        int[][] C = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                C[i][j] = 0;
                for (int k = 0; k < COLS; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }
}
