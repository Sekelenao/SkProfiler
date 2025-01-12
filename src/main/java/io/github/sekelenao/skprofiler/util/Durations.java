package io.github.sekelenao.skprofiler.util;

import java.time.Duration;
import java.util.Objects;

public final class Durations {

    private Durations() {
        throw new AssertionError("You cannot instantiate this class");
    }

    public static String asHumanReadable(Duration duration) {
        Objects.requireNonNull(duration);
        var printOther = false;
        var sb = new StringBuilder();
        var days = duration.toDaysPart();
        var hours = duration.toHoursPart();
        var minutes = duration.toMinutesPart();
        var seconds = duration.toSecondsPart();
        var millis = duration.toMillisPart();
        if(days > 0) {
            sb.append(days).append("d ");
            printOther = true;
        }
        if(hours > 0 || printOther) {
            sb.append(hours).append("h ");
            printOther = true;
        }
        if(minutes > 0 || printOther) {
            sb.append(minutes).append("m ");
            printOther = true;
        }
        if(seconds > 0 || printOther){
            sb.append(seconds).append("s ");
        }
        return sb.append(millis).append("ms").toString();
    }

}
