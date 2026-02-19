package nl.tudelft.cs4575p1;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public record MultiThreadedProgram(int inputSize, int numThreads) implements Program {
    @Override
    public void run() throws Exception {
        ExecutorService executor = new ThreadPoolExecutor(
            0, // keep zero threads in the pool, i.e. never reuse threads
            this.numThreads, // maximum number of threads to use
            0L, TimeUnit.MILLISECONDS, // do not keep threads alive
            new LinkedBlockingQueue<>(this.inputSize),
            Executors.defaultThreadFactory()
        );
        AtomicInteger sink = new AtomicInteger(0);
        for (int i = 0; i < this.inputSize; i++) {
            final int value = i;
            executor.submit(() -> sink.updateAndGet(current -> current ^ Arrays.hashCode(Program.task(value))), executor);
        }
        executor.close();
        executor.awaitTermination(1L, TimeUnit.HOURS);
        System.out.printf("Completed! %d%n", sink.get());
    }
}
