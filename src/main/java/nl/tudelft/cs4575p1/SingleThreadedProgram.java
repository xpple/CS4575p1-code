package nl.tudelft.cs4575p1;

public record SingleThreadedProgram(int inputSize) implements Program {
    @Override
    public void run() throws Exception {
        long sink = 0; // sink that ensures task is not optimized away by the JIT
        for (int i = 0; i < this.inputSize; i++) {
            sink ^= Program.task(i);
        }
        System.out.printf("Completed! %d%n", sink);
    }
}
