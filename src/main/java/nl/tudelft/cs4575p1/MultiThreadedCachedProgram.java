package nl.tudelft.cs4575p1;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public record MultiThreadedCachedProgram(int inputSize, int numThreads) implements Program {
    @Override
    public void run() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(this.numThreads);
        AtomicInteger sink = new AtomicInteger(0);
        for (int i = 0; i < this.inputSize; i++) {
            final int value = i;
            executor.submit(() -> sink.getAndUpdate(current -> current ^ Arrays.hashCode(Program.task(value))), executor);
        }
        executor.close();
        executor.awaitTermination(1L, TimeUnit.HOURS);
        System.out.printf("Completed! %d%n", sink.get());
    }
}
