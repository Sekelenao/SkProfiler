package io.github.sekelenao.skprofiler.util;

import java.time.Duration;
import java.util.Locale;
import java.util.Objects;

public final class Units {

    private static final String[] MEMORY_UNITS = {"B", "KiB", "MiB", "GiB"};

    private Units() {
        throw new AssertionError("You cannot instantiate this class");
    }

    public static String durationAsHumanReadable(Duration duration) {
        Objects.requireNonNull(duration);
        return duration.toDaysPart() + "d " +
                duration.toHoursPart() + "h " +
                duration.toMinutesPart() + "m " +
                duration.toSecondsPart() + "s " +
                duration.toMillisPart() + "ms";
    }

    public static String bytesAsHumanReadable(long bytes) {
        Assertions.isPositive(bytes);
        int unitIndex = (int) (Math.log(bytes) / Math.log(1024));
        unitIndex = Math.min(unitIndex, MEMORY_UNITS.length - 1);
        if(unitIndex == 0){
            return String.format(Locale.ROOT, "%d bytes", bytes);
        }
        double readableValue = bytes / Math.pow(1024, unitIndex);
        return String.format(Locale.ROOT, "%d bytes (~%.2f %s)", bytes, readableValue, MEMORY_UNITS[unitIndex]);
    }

}
