package org.example;

import static org.example.BasicMatrixMultiplication.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class Main {
    public static void main(String[] args) {
        int[] sizes = {10, 100, 500};
        int blockSize = 50;

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        Runtime runtime = Runtime.getRuntime();

        for (int size : sizes) {
            ROWS = size;
            COLS = size;
            int[][] A = new int[ROWS][COLS];
            int[][] B = new int[ROWS][COLS];

            generateMatrix(A, 1, 9);
            generateMatrix(B, 1, 9);

            long startTimeBasic = System.currentTimeMillis();
            double cpuBeforeBasic = osBean.getSystemLoadAverage();
            long memoryBeforeBasic = runtime.totalMemory() - runtime.freeMemory();
            int[][] basic = matrixMultiplication(A, B);
            long endTimeBasic = System.currentTimeMillis();
            double cpuAfterBasic = osBean.getSystemLoadAverage();
            long memoryAfterBasic = runtime.totalMemory() - runtime.freeMemory();
            long BasicExecutionTime = endTimeBasic - startTimeBasic;

            long startTimeVectorized = System.currentTimeMillis();
            double cpuBeforeVectorized = osBean.getSystemLoadAverage();
            long memoryBeforeVectorized = runtime.totalMemory() - runtime.freeMemory();
            int[][] vectorized = VectorizedMatrixMultiplication.parallelMatrixMultiplication(A, B);
            long endTimeVectorized = System.currentTimeMillis();
            double cpuAfterVectorized = osBean.getSystemLoadAverage();
            long memoryAfterVectorized = runtime.totalMemory() - runtime.freeMemory();
            long VectorizedExecutionTime = endTimeVectorized - startTimeVectorized;

            long startTimeParallel = System.currentTimeMillis();
            double cpuBeforeParallel = osBean.getSystemLoadAverage();
            long memoryBeforeParallel = runtime.totalMemory() - runtime.freeMemory();
            int[][] parallel = ParallelMatrixMultiplication.parallelMatrixMultiplication(A, B, blockSize);
            long endTimeParallel = System.currentTimeMillis();
            double cpuAfterParallel = osBean.getSystemLoadAverage();
            long memoryAfterParallel = runtime.totalMemory() - runtime.freeMemory();
            long ParallelExecutionTime = endTimeParallel - startTimeParallel;

            double speedupVectorized = (double) BasicExecutionTime / VectorizedExecutionTime;
            double speedupParallel = (double) BasicExecutionTime / ParallelExecutionTime;
            int numThreads = Runtime.getRuntime().availableProcessors();
            double efficiencyVectorized = speedupVectorized / numThreads;
            double efficiencyParallel = speedupParallel / numThreads;

            System.out.println("-----Matrix Size: " + ROWS + "x" + COLS + "-----");
            System.out.println("Basic Execution Time: " + BasicExecutionTime + "ms");
            System.out.println("Basic CPU Usage: " + (cpuAfterBasic - cpuBeforeBasic) + "%");
            System.out.println("Basic Memory Usage: " + (memoryAfterBasic - memoryBeforeBasic) + " bytes");
            System.out.println("Vectorized Execution Time: " + VectorizedExecutionTime + "ms");
            System.out.println("Vectorized CPU Usage: " + (cpuAfterVectorized - cpuBeforeVectorized) + "%");
            System.out.println("Vectorized Memory Usage: " + (memoryAfterVectorized - memoryBeforeVectorized) + " bytes");
            System.out.println("Parallel Execution Time: " + ParallelExecutionTime + "ms");
            System.out.println("Parallel CPU Usage: " + (cpuAfterParallel - cpuBeforeParallel) + "%");
            System.out.println("Parallel Memory Usage: " + (memoryAfterParallel - memoryBeforeParallel) + " bytes");
        }


    }
}