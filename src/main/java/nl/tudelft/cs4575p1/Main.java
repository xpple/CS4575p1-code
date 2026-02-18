package nl.tudelft.cs4575p1;

import com.google.common.hash.Hashing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Expected input size argument");
        }

        int size = Integer.parseInt(args[0]);

        List<Long> list = new ArrayList<>(size);
        IntStream.range(0, size)
            //.parallel()
            .mapToLong(i -> Hashing.sha256().hashInt(i).asLong())
            .forEach(list::add);
    }
}
