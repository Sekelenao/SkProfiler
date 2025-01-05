package io.github.sekelenao.skprofiler.system;

import java.util.Optional;

public final class SystemProperties {

    private SystemProperties() {
        throw new AssertionError("You cannot instantiate this class");
    }

    public static String javaVersion(){
        var version = System.getProperty("java.version");
        if(version == null){
            throw new AssertionError("Java version is null");
        }
        return version;
    }

    public static Optional<String> command(){
        return Optional.ofNullable(System.getProperty("sun.java.command"));
    }

}
