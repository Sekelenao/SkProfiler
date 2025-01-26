package io.github.sekelenao.skprofiler.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

final class TestUnits {

    @Test
    @DisplayName("Duration as human readable assertions")
    void durationDurationAsHumanReadableAssertions() {
        assertThrows(NullPointerException.class, () -> Units.durationAsHumanReadable(null));
    }

    @Test
    @DisplayName("Duration as human readable is working")
    void durationDurationAsHumanReadableIsWorking(){
        var complete = Duration.ofMillis(454542561);
        var oneHour = Duration.ofMillis(3600000);
        var oneMinute = Duration.ofMillis(60000);
        var oneSecond = Duration.ofMillis(1000);
        var oneMillisecond = Duration.ofMillis(1);
        assertAll(
                () -> assertEquals(
                        "5d 6h 15m 42s 561ms",
                        Units.durationAsHumanReadable(complete)
                ),
                () -> assertEquals(
                        "0d 1h 0m 0s 0ms",
                        Units.durationAsHumanReadable(oneHour)
                ),
                () -> assertEquals(
                        "0d 0h 1m 0s 0ms",
                        Units.durationAsHumanReadable(oneMinute)
                ),
                () -> assertEquals(
                        "0d 0h 0m 1s 0ms",
                        Units.durationAsHumanReadable(oneSecond)
                ),
                () -> assertEquals(
                        "0d 0h 0m 0s 1ms",
                        Units.durationAsHumanReadable(oneMillisecond)
                )
        );
    }

    @Test
    @DisplayName("FormatBytes handles invalid input")
    void bytesAsHumanReadableHandlesInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> Units.bytesAsHumanReadable(-1));
    }

    @Test
    @DisplayName("FormatBytes works with exact units up to GiB")
    void bytesAsHumanReadableWorksWithExactUnitsUpToGiB() {
        assertAll(
                () -> assertEquals("1 bytes", Units.bytesAsHumanReadable(1)),
                () -> assertEquals("1024 bytes (~1.00 KiB)", Units.bytesAsHumanReadable(1024)),
                () -> assertEquals("1048576 bytes (~1.00 MiB)", Units.bytesAsHumanReadable(1024 * 1024)),
                () -> assertEquals("1073741824 bytes (~1.00 GiB)", Units.bytesAsHumanReadable(1024L * 1024 * 1024)),
                () -> assertEquals(
                    "1099511627776 bytes (~1024.00 GiB)",
                    Units.bytesAsHumanReadable(1024L * 1024 * 1024 * 1024)
                )
        );
    }

    @Test
    @DisplayName("FormatBytes handles non-round values up to GiB")
    void bytesAsHumanReadableWorksWithNonRoundValuesUpToGiB() {
        assertAll(
                () -> assertEquals("1260 bytes (~1.23 KiB)", Units.bytesAsHumanReadable(1260)),
                () -> assertEquals("12634 bytes (~12.34 KiB)", Units.bytesAsHumanReadable(12634)),
                () -> assertEquals("3612672 bytes (~3.45 MiB)", Units.bytesAsHumanReadable(3612672)),
                () -> assertEquals("8472498176 bytes (~7.89 GiB)", Units.bytesAsHumanReadable(8472498176L)),
                () -> assertEquals("131941395333 bytes (~122.88 GiB)", Units.bytesAsHumanReadable(131941395333L)),
                () -> assertEquals(
                    "6355206855698432 bytes (~5918747.61 GiB)",
                    Units.bytesAsHumanReadable(6355206855698432L)
                ),
                () -> assertEquals(
                    "1383505805528216371 bytes (~1288490188.80 GiB)",
                    Units.bytesAsHumanReadable(1383505805528216371L)
                ),
                () -> assertEquals(
                    "9223372036854775807 bytes (~8589934592.00 GiB)",
                    Units.bytesAsHumanReadable(Long.MAX_VALUE)
                )
        );
    }


}
