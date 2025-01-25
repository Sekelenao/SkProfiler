package io.github.sekelenao.skprofiler.http.endpoint;

public class HomeEndpoint implements Endpoint {

    @Override
    public String route() {
        return "/";
    }

}
