package nl.tudelft.cs4575p1;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public sealed interface Program permits
    SingleThreadedProgram,
    MultiThreadedProgram,
    MultiThreadedCachedProgram {

    void run() throws Exception;

    static long task(int value) {
        long sink = 0;
        for (int i = 0; i < Task.INTERNAL_ITERATION_COUNT; i++) {
            sink ^= Task.HASH_FUNCTION.hashInt(value + i).asLong();
        }
        return sink;
    }
}

class Task {
    // is thread-safe, so does not need to be thread-local
    static final HashFunction HASH_FUNCTION = Hashing.sha512();

    static final int INTERNAL_ITERATION_COUNT = 1_000_000;
}
