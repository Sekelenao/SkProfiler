package io.github.sekelenao.skprofiler.log;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class TestCustomLogger {

    @Test
    @DisplayName("CustomLogger use caching")
    void loggersAreCached(){
        var logger1 = CustomLogger.on(this.getClass());
        var logger2 = CustomLogger.on(this.getClass());
        assertAll(
                () -> assertEquals(logger1, logger2),
                () -> assertSame(logger1, logger2)
        );
    }

}
