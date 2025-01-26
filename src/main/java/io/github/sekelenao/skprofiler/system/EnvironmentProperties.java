package io.github.sekelenao.skprofiler.system;

import io.github.sekelenao.skprofiler.util.Optionals;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.Optional;
import java.util.OptionalLong;

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

    public static OptionalLong heapMemoryInitialSize(){
        return Optionals.emptyIfNegative(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getInit());
    }

    public static long heapMemoryUsedSize(){
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
    }

    public static long heapMemoryCommittedSize(){
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getCommitted();
    }

    public static OptionalLong heapMemoryMaxSize(){
        return Optionals.emptyIfNegative(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax());
    }

    public static OptionalLong nonHeapMemoryInitialSize(){
        return Optionals.emptyIfNegative(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getInit());
    }

    public static long nonHeapMemoryUsedSize(){
        return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
    }

    public static long nonHeapMemoryCommittedSize(){
        return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getCommitted();
    }

    public static OptionalLong nonHeapMemoryMaxSize(){
        return Optionals.emptyIfNegative(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getMax());
    }

}
