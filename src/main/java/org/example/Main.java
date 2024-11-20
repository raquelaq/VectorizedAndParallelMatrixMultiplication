package org.example;

import static org.example.BasicMatrixMultiplication.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class Main {
    public static void main(String[] args) {
        int[] sizes = {10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1024};
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

            //Basic
            long startTimeBasic = System.currentTimeMillis();
            double cpuBeforeBasic = osBean.getSystemLoadAverage();
            long memoryBeforeBasic = runtime.totalMemory() - runtime.freeMemory();
            int[][] basic = matrixMultiplication(A, B);
            long endTimeBasic = System.currentTimeMillis();
            double cpuAfterBasic = osBean.getSystemLoadAverage();
            long memoryAfterBasic = runtime.totalMemory() - runtime.freeMemory();
            long BasicExecutionTime = endTimeBasic - startTimeBasic;

            //Vectorized
            long startTimeVectorized = System.currentTimeMillis();
            double cpuBeforeVectorized = osBean.getSystemLoadAverage();
            long memoryBeforeVectorized = runtime.totalMemory() - runtime.freeMemory();
            int[][] vectorized = VectorizedMatrixMultiplication.parallelMatrixMultiplication(A, B);
            long endTimeVectorized = System.currentTimeMillis();
            double cpuAfterVectorized = osBean.getSystemLoadAverage();
            long memoryAfterVectorized = runtime.totalMemory() - runtime.freeMemory();
            long VectorizedExecutionTime = endTimeVectorized - startTimeVectorized;

            //Parallel
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
            logResults("Basic", size, BasicExecutionTime, cpuAfterBasic - cpuBeforeBasic, memoryAfterBasic - memoryBeforeBasic, 1.0, 1.0);
            logResults("Vectorized", size, VectorizedExecutionTime, cpuAfterVectorized - cpuBeforeVectorized, memoryAfterVectorized - memoryBeforeVectorized, speedupVectorized, efficiencyVectorized);
            logResults("Parallel", size, ParallelExecutionTime, cpuAfterParallel - cpuBeforeParallel, memoryAfterParallel - memoryBeforeParallel, speedupParallel, efficiencyParallel);
        }


    }
    public static void logResults(String algorithm, int size, long executionTime, double cpuUsage, long memoryUsage, double speedup, double efficiency) {
        System.out.printf("Algorithm: %s | Matrix Size: %dx%d | Execution Time: %dms | CPU Usage: %.2f%% | Memory Usage: %d bytes | Speedup: %.2fx | Efficiency: %.2f%n",
                algorithm, size, size, executionTime, cpuUsage * 100, memoryUsage, speedup, efficiency);
    }
}