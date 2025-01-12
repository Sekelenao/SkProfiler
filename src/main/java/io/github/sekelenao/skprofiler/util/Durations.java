package io.github.sekelenao.skprofiler.util;

import java.time.Duration;
import java.util.Objects;

public final class Durations {

    private Durations() {
        throw new AssertionError("You cannot instantiate this class");
    }

    public static String asHumanReadable(Duration duration) {
        Objects.requireNonNull(duration);
        return duration.toDaysPart() + "d " +
                duration.toHoursPart() + "h " +
                duration.toMinutesPart() + "m " +
                duration.toSecondsPart() + "s " +
                duration.toMillisPart() + "ms";
    }

}
