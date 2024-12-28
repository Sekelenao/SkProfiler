package io.github.sekelenao.skprofiler.http;

public enum HttpStatus {

    SUCCESS(200);

    private final int code;

    HttpStatus(int code) {
        if(code < 100 || code > 599){
            throw new IllegalArgumentException("Invalid http code");
        }
        this.code = code;
    }

    public int code() {
        return code;
    }

}
