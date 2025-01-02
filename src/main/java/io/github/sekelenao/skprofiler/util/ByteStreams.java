package io.github.sekelenao.skprofiler.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.function.Supplier;

public final class ByteStreams {

    private ByteStreams() {
        throw new AssertionError("You cannot instantiate this class");
    }

    public static String readFromInputStream(Supplier<InputStream> inputStreamSupplier) throws IOException {
        Objects.requireNonNull(inputStreamSupplier);
        try(var inputStream = inputStreamSupplier.get()){
            var bytes = inputStream.readAllBytes();
            return new String(bytes);
        }
    }

    public static void writeOnOutputStream(Supplier<OutputStream> outputStreamSupplier, byte[] bytes) throws IOException {
        Objects.requireNonNull(outputStreamSupplier);
        Objects.requireNonNull(bytes);
        try(var outputStream = outputStreamSupplier.get()){
            outputStream.write(bytes);
        }
    }

}
