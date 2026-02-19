package nl.tudelft.cs4575p1;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private enum ProgramOption {
        SINGLE_THREADED {
            @Override
            public Program parse(Deque<String> args) {
                return new SingleThreadedProgram(parseInputSize(args));
            }
        },
        MULTI_THREADED {
            @Override
            public Program parse(Deque<String> args) {
                return new MultiThreadedProgram(parseInputSize(args), parseNumThreads(args));
            }
        },
        MULTI_THREADED_CACHED {
            @Override
            public Program parse(Deque<String> args) {
                return new MultiThreadedCachedProgram(parseInputSize(args), parseNumThreads(args));
            }
        },
        ;

        public abstract Program parse(Deque<String> args);

        private static final Map<String, ProgramOption> FROM_STRING = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(Enum::name, po -> po));
    }

    public static void main(String[] argsArr) throws Exception {
        Deque<String> args = new ArrayDeque<>(List.of(argsArr));
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Expected program option");
        }
        String optionString = args.pop();
        ProgramOption programOption = ProgramOption.FROM_STRING.get(optionString);
        if (programOption == null) {
            throw new IllegalArgumentException("Invalid program option");
        }

        Program program = programOption.parse(args);
        program.run();
    }

    private static int parseInputSize(Deque<String> args) {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Expected input size argument");
        }

        return Integer.parseInt(args.pop());
    }

    private static int parseNumThreads(Deque<String> args) {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("Expected thread number argument");
        }

        return Integer.parseInt(args.pop());
    }
}
