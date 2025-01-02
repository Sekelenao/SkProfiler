package io.github.sekelenao.skprofiler.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class TestByteStreams {

    @Test
    @DisplayName("Read from InputStream assertions")
    void readFromInputStreamAssertions() {
        assertThrows(NullPointerException.class, () -> ByteStreams.readFromInputStream(null));
    }

    @Test
    @DisplayName("Write on OutputStream assertions")
    void writeOnOutputStreamAssertions() throws IOException {
        try(var outputStream = OutputStream.nullOutputStream()){
            assertAll(
                    () -> assertThrows(
                            NullPointerException.class,
                            () -> ByteStreams.writeOnOutputStream(null, new byte[0])
                    ),
                    () -> assertThrows(
                            NullPointerException.class,
                            () -> ByteStreams.writeOnOutputStream(() -> outputStream, null)
                    )
            );
        }
    }

    @Test
    @DisplayName("Read from InputStream is working")
    void readFromInputStream() throws IOException {
        var content = "Hello World!";
        var inputStream = new ByteArrayInputStream(content.getBytes());
        assertEquals(content, ByteStreams.readFromInputStream(() -> inputStream));
    }

    @Test
    @SuppressWarnings("resource")
    @DisplayName("Read is closing the InputStream")
    void readIsClosingTheInputStream() {
        var inputStream = InputStream.nullInputStream();
        assertAll(
                () -> assertDoesNotThrow(() -> ByteStreams.readFromInputStream(() -> inputStream)),
                () -> assertThrows(IOException.class, inputStream::read)
        );
    }

    @Test
    @DisplayName("Write to OutputStream is working")
    void writeToOutputStream() throws IOException {
        var content = "Hello World!";
        var outputStream = new ByteArrayOutputStream();
        ByteStreams.writeOnOutputStream(() -> outputStream, content.getBytes());
        assertEquals(
                new String(content.getBytes()),
                outputStream.toString()
        );
    }

    @Test
    @SuppressWarnings("resource")
    @DisplayName("Read is closing the InputStream")
    void writeIsClosingTheOutputStream() {
        var outputStream = OutputStream.nullOutputStream();
        assertAll(
                () -> assertDoesNotThrow(() -> ByteStreams.writeOnOutputStream(() -> outputStream, new byte[0])),
                () -> assertThrows(IOException.class, () -> outputStream.write(new byte[0]))
        );
    }

}
