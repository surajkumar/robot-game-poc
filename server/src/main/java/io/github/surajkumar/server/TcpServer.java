package io.github.surajkumar.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.net.NetServerOptions;

public class TcpServer extends AbstractVerticle {
    private final int port;

    public TcpServer(int port) {
        this.port = port;
    }

    @Override
    public void start(Promise<Void> promise) {
        var server = vertx.createNetServer(new NetServerOptions().setPort(port));
        server.connectHandler(new TcpHandler());
        server.listen(res -> {
            if (res.succeeded()) {
                promise.complete();
            } else {
                promise.fail(res.cause());
            }
        });
    }
}
