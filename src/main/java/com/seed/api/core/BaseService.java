package com.seed.api.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;

/**
 * The Base service class.
 */
public class BaseService extends AbstractVerticle {

    /**
     * The logger utils class.
     */
    private Logger logger = LoggerFactory.getLogger(
        BaseService.class.getName());

    /**
     * The event bus default part.
     */
    protected static final int WEB_SOCKET_PORT = 7000;

    /**
     * The web server port.
     */
    protected static final int WS_PORT = 8080;

    /**
     * The call defaukt wait time before timeout.
     */
    protected static final int WAIT_TIME = 2000;

    /**
     * The error code 500.
     */
    protected static final int ERR_500 = 500;

    /**
     * The success code.
     */
    protected static final int SUCCESS_CODE = 200;

    /**
     * Bad Client Request.
     */
    protected static final int ERR_BAD_REQUEST = 400;

    /**
     * The constructor.
     */
    public BaseService() {
        this.vertx = Vertx.vertx(new VertxOptions()
            .setMetricsOptions(new MicrometerMetricsOptions()
                .setPrometheusOptions(new VertxPrometheusOptions()
                    .setEnabled(true))
                .setEnabled(true)));
    }

    /**
     * Sets routes for the http server.
     * @param router The router used to set paths.
     */
    protected void setBaseRoutes(final Router router) { }

    /**
     * Gets the response as a json object.
     * @param payload The payload.
     * @return The json object.
     */
    protected JsonObject getResponse(final String payload) {
        return new JsonObject()
            .put("status", SUCCESS_CODE)
            .put("message", "Success")
            .put("payload", payload);
    }

    /**
     * Gets the response as a json object.
     * @param status The status code.
     * @param payload The payload.
     * @return The json object.
     */
    protected JsonObject getResponse(final int status, final String payload) {
        return new JsonObject()
            .put("status", status)
            .put("message", "Success")
            .put("payload", payload);
    }
}
