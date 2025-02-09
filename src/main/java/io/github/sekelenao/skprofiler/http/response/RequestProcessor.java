package io.github.sekelenao.skprofiler.http.response;

import java.io.IOException;

@FunctionalInterface
public interface RequestProcessor {

    CustomHttpResponse response() throws IOException;

}
