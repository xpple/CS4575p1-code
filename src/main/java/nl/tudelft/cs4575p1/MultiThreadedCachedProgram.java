package nl.tudelft.cs4575p1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public record MultiThreadedCachedProgram(int inputSize, int numThreads) implements Program {
    @Override
    public void run() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(this.numThreads);
        long[] sinkArr = new long[this.inputSize];
        for (int i = 0; i < this.inputSize; i++) {
            final int value = i;
            executor.submit(() -> {
                sinkArr[value] = Program.task(value);
            });
        }
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.HOURS);
        long sink = 0;
        for (long s : sinkArr) {
            sink ^= s;
        }
        System.out.printf("Completed! %d%n", sink);
    }
}
