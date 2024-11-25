package org.example;

import static org.example.BasicMatrixMultiplication.*;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class Main {
    public static void main(String[] args) {
        int[] sizes = {50, 100, 400, 600, 800, 1024, 2048, 4096};
        int blockSize = 50;
        String filename = "matrix_results.csv";

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
            logResults(filename, "Basic", size, BasicExecutionTime, cpuAfterBasic - cpuBeforeBasic, memoryAfterBasic - memoryBeforeBasic, 1.0, 1.0);
            logResults(filename, "Vectorized", size, VectorizedExecutionTime, cpuAfterVectorized - cpuBeforeVectorized, memoryAfterVectorized - memoryBeforeVectorized, speedupVectorized, efficiencyVectorized);
            logResults(filename, "Parallel", size, ParallelExecutionTime, cpuAfterParallel - cpuBeforeParallel, memoryAfterParallel - memoryBeforeParallel, speedupParallel, efficiencyParallel);
        }


    }
    public static void logResults(String filename, String algorithm, int size, long executionTime, double cpuUsage, long memoryUsage, double speedup, double efficiency) {
        System.out.printf("Algorithm: %s | Matrix Size: %dx%d | Execution Time: %dms | CPU Usage: %.2f%% | Memory Usage: %d bytes | Speedup: %.2fx | Efficiency: %.2f%n",
                algorithm, size, size, executionTime, cpuUsage * 100, memoryUsage, speedup, efficiency);

        try (FileWriter writer = new FileWriter(filename, true)) {
            if (new java.io.File(filename).length() == 0) {
                writer.append("Algorithm,Matrix Size,Execution Time (ms),CPU Usage (%),Memory Usage (bytes),Speedup,Efficiency\n");
            }
            writer.append(String.format("%s,%d,%d,%.2f,%d,%.2f,%.2f%n",
                    algorithm, size, executionTime, cpuUsage * 100, memoryUsage, speedup, efficiency));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}