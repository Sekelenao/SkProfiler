package io.github.sekelenao.skprofiler.http;

import java.io.IOException;

@FunctionalInterface
public interface RequestProcessor {

    CustomHttpResponse response() throws IOException;

}
