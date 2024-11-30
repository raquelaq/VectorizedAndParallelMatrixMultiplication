package org.example;

import static org.example.BasicMatrixMultiplication.*;

import com.sun.management.OperatingSystemMXBean;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        int[] sizes = {50, 100, 400, 600, 800, 1024, 2048, 4096};
        int blockSize = 50;
        String filename = "matrix_results.csv";

        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        Runtime runtime = Runtime.getRuntime();

        for (int size : sizes) {
            ROWS = size;
            COLS = size;
            int[][] A = new int[ROWS][COLS];
            int[][] B = new int[ROWS][COLS];

            generateMatrix(A, 1, 9);
            generateMatrix(B, 1, 9);

            // Basic
            long startTimeBasic = System.currentTimeMillis();
            double cpuBeforeBasic = osBean.getProcessCpuLoad();
            long memoryBeforeBasic = runtime.totalMemory() - runtime.freeMemory();
            int[][] basic = matrixMultiplication(A, B);
            long endTimeBasic = System.currentTimeMillis();
            double cpuAfterBasic = osBean.getProcessCpuLoad();
            long memoryAfterBasic = runtime.totalMemory() - runtime.freeMemory();
            long BasicExecutionTime = endTimeBasic - startTimeBasic;
            double cpuUsageBasic = Math.max(0, (cpuAfterBasic - cpuBeforeBasic) * 100);

            // Vectorized
            long startTimeVectorized = System.currentTimeMillis();
            double cpuBeforeVectorized = osBean.getProcessCpuLoad();
            long memoryBeforeVectorized = runtime.totalMemory() - runtime.freeMemory();
            int[][] vectorized = VectorizedMatrixMultiplication.vectorizedMatrixMultiplication(A, B);
            long endTimeVectorized = System.currentTimeMillis();
            double cpuAfterVectorized = osBean.getProcessCpuLoad();
            long memoryAfterVectorized = runtime.totalMemory() - runtime.freeMemory();
            long VectorizedExecutionTime = endTimeVectorized - startTimeVectorized;
            double cpuUsageVectorized = Math.max(0, (cpuAfterVectorized - cpuBeforeVectorized) * 100);

            // Parallel
            long startTimeParallel = System.currentTimeMillis();
            double cpuBeforeParallel = osBean.getProcessCpuLoad();
            long memoryBeforeParallel = runtime.totalMemory() - runtime.freeMemory();
            int[][] parallel = ParallelMatrixMultiplication.parallelMatrixMultiplication(A, B, blockSize);
            long endTimeParallel = System.currentTimeMillis();
            double cpuAfterParallel = osBean.getProcessCpuLoad();
            long memoryAfterParallel = runtime.totalMemory() - runtime.freeMemory();
            long ParallelExecutionTime = endTimeParallel - startTimeParallel;
            double cpuUsageParallel = Math.max(0, (cpuAfterParallel - cpuBeforeParallel) * 100);

            // Metrics
            double speedupVectorized = (double) BasicExecutionTime / VectorizedExecutionTime;
            double speedupParallel = (double) BasicExecutionTime / ParallelExecutionTime;
            int numThreads = Runtime.getRuntime().availableProcessors();
            double efficiencyVectorized = speedupVectorized / numThreads;
            double efficiencyParallel = speedupParallel / numThreads;

            System.out.println("-----Matrix Size: " + ROWS + "x" + COLS + "-----");
            logResults(filename, "Basic", size, BasicExecutionTime, cpuUsageBasic, memoryAfterBasic - memoryBeforeBasic, 1.0, 1.0);
            logResults(filename, "Vectorized", size, VectorizedExecutionTime, cpuUsageVectorized, memoryAfterVectorized - memoryBeforeVectorized, speedupVectorized, efficiencyVectorized);
            logResults(filename, "Parallel", size, ParallelExecutionTime, cpuUsageParallel, memoryAfterParallel - memoryBeforeParallel, speedupParallel, efficiencyParallel);
        }
    }

    public static void logResults(String filename, String algorithm, int size, long executionTime, double cpuUsage, long memoryUsage, double speedup, double efficiency) {
        System.out.printf(Locale.US, "Algorithm: %s | Matrix Size: %dx%d | Execution Time: %dms | CPU Usage: %.2f%% | Memory Usage: %d bytes | Speedup: %.2fx | Efficiency: %.2f%n",
                algorithm, size, size, executionTime, cpuUsage, memoryUsage, speedup, efficiency);

        // Guardar los resultados en el archivo CSV
        try (FileWriter writer = new FileWriter(filename, true)) {
            // Escribir encabezado si el archivo está vacío
            if (new java.io.File(filename).length() == 0) {
                writer.append("Algorithm,Matrix Size,Execution Time (ms),CPU Usage (%),Memory Usage (bytes),Speedup,Efficiency\n");
            }
            // Asegurar formato correcto con Locale.US
            writer.append(String.format(Locale.US, "%s,%d,%d,%.2f,%d,%.2f,%.2f%n",
                    algorithm, size, executionTime, cpuUsage, memoryUsage, speedup, efficiency));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
