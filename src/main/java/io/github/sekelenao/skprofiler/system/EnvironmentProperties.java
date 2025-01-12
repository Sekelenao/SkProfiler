package io.github.sekelenao.skprofiler.system;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.Optional;

public final class EnvironmentProperties {

    private EnvironmentProperties() {
        throw new AssertionError("You cannot instantiate this class");
    }

    public static Optional<String> command(){
        return Optional.ofNullable(System.getProperty("sun.java.command"));
    }

    public static Optional<String> javaVersion(){
        return Optional.ofNullable(System.getProperty("java.version"));
    }

    public static Optional<String> vmName(){
        return Optional.ofNullable(System.getProperty("java.vm.name"));
    }

    public static Optional<String> vmVersion(){
        return Optional.ofNullable(System.getProperty("java.vm.version"));
    }

    public static Optional<String> vmVendor(){
        return Optional.ofNullable(System.getProperty("java.vm.vendor"));
    }

    public static Optional<String> javaHome(){
        return Optional.ofNullable(System.getProperty("java.home"));
    }

    public static Duration vmUptime(){
        return Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());
    }

}
