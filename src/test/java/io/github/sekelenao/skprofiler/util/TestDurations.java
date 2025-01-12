package io.github.sekelenao.skprofiler.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

final class TestDurations {

    @Test
    @DisplayName("Duration as human readable assertions")
    void durationAsHumanReadableAssertions() {
        assertThrows(NullPointerException.class, () -> Durations.asHumanReadable(null));
    }

    @Test
    @DisplayName("Duration as human readable is working")
    void durationAsHumanReadableIsWorking(){
        var complete = Duration.ofMillis(454542561);
        var oneHour = Duration.ofMillis(3600000);
        var oneMinute = Duration.ofMillis(60000);
        var oneSecond = Duration.ofMillis(1000);
        var oneMillisecond = Duration.ofMillis(1);
        assertAll(
                () -> assertEquals(
                        "5d 6h 15m 42s 561ms",
                        Durations.asHumanReadable(complete)
                ),
                () -> assertEquals(
                        "0d 1h 0m 0s 0ms",
                        Durations.asHumanReadable(oneHour)
                ),
                () -> assertEquals(
                        "0d 0h 1m 0s 0ms",
                        Durations.asHumanReadable(oneMinute)
                ),
                () -> assertEquals(
                        "0d 0h 0m 1s 0ms",
                        Durations.asHumanReadable(oneSecond)
                ),
                () -> assertEquals(
                        "0d 0h 0m 0s 1ms",
                        Durations.asHumanReadable(oneMillisecond)
                )
        );
    }

}
