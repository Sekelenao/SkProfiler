package io.github.sekelenao.skprofiler.http;

public enum HttpStatus {

    SUCCESS(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    HttpStatus(int code) {
        if(code < 100 || code > 599){
            throw new IllegalArgumentException("Invalid HTTP code");
        }
        this.code = code;
    }

    public int code() {
        return code;
    }

}
