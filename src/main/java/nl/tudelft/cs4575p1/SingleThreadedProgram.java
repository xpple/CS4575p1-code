package nl.tudelft.cs4575p1;

import java.util.Arrays;

public record SingleThreadedProgram(int inputSize) implements Program {
    @Override
    public void run() throws Exception {
        int sink = 0; // sink that ensures task is not optimized away by the JIT
        for (int i = 0; i < this.inputSize; i++) {
            sink ^= Arrays.hashCode(Program.task(i));
        }
        System.out.printf("Completed! %d%n", sink);
    }
}
