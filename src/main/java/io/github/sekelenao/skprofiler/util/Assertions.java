package io.github.sekelenao.skprofiler.util;

import java.util.Arrays;
import java.util.Objects;

public final class Assertions {

    private Assertions() {
        throw new AssertionError("You cannot instantiate this class");
    }

    public static void checkPort(int port){
        if(port < 0 || port > 65535) {
            throw new IllegalArgumentException("Wrong port was provided: " + port);
        }
    }

    public static void requireNonNulls(Object... objects){
        Arrays.stream(Objects.requireNonNull(objects)).forEach(Objects::requireNonNull);
    }

}
