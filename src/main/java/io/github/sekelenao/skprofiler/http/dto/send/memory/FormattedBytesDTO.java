package io.github.sekelenao.skprofiler.http.dto.send.memory;

import io.github.sekelenao.skprofiler.util.Assertions;
import io.github.sekelenao.skprofiler.util.Optionals;
import io.github.sekelenao.skprofiler.util.Units;

import java.util.Objects;

public record FormattedBytesDTO(long bytes, String formatted) {

    public FormattedBytesDTO {
        Assertions.isGreaterThan(bytes, -2);
        Objects.requireNonNull(formatted);
    }

    public static FormattedBytesDTO of(long bytes) {
        Assertions.isGreaterThan(bytes, -2);
        if (bytes < 0) {
            return new FormattedBytesDTO(-1, Optionals.missingValueDescriptor());
        }
        return new FormattedBytesDTO(
            bytes,
            Units.bytesAsHumanReadable(bytes)
        );
    }

}
