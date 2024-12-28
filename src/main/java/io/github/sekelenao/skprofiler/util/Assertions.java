package io.github.sekelenao.skprofiler.util;

public final class Assertions {

    private Assertions() {
        throw new AssertionError("You cannot instantiate this class");
    }

    public static void checkPort(int port){
        if(port < 0 || port > 65535) {
            throw new NumberFormatException("Wrong port were provided: " + port);
        }
    }

}
