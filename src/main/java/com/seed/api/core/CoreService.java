package com.seed.api.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

/**
 * The core service class.
 */
public class CoreService extends UserService {

  /**
   * The logger utils class.
   */
  private Logger logger = LoggerFactory.getLogger(CoreService.class.getName());

  /**
   * The start callback.
   */
  @Override
  public void start() {
    this.logger.info("start() ->");
    this.startHttpServer(WS_PORT, WEB_SOCKET_PORT, h -> {
      if (h.failed()) {
        this.logger.error(h.cause().getMessage(), h.cause());
      }
    }, wsh -> {
      if (wsh.failed()) {
        this.logger.error(wsh.cause().getMessage(), wsh.cause());
      }
    });
  }

  /**
   * Starts http server.
   * @param customport The http server port.
   * @param custowsmport The web socket port.
   * @param handler The http server result handler.
   * @param wshandler The web socket result handler.
   */
  public void startHttpServer(final int customport,
        final int custowsmport,
        final Handler<AsyncResult<HttpServer>> handler,
        final Handler<AsyncResult<HttpServer>> wshandler) {
            this.logger.info("startHttpServer() ->");
            Router router = Router.router(this.vertx);
            this.setRoutes(router);

            // Health check
            this.setHealthCheck(router);

            this.vertx.createHttpServer()
                .requestHandler(router)
                .listen(customport, handler);

            // Start websocket.
            this.createWebSocket(custowsmport, null, wshandler);
  }

  /**
     * Sets routes for the http server.
     * @param router The router used to set paths.
     */
  private void setRoutes(final Router router) {
      router.route().handler(CorsHandler.create()
          .allowedMethod(HttpMethod.POST)
          .allowedMethod(HttpMethod.GET)
          .allowedMethod(HttpMethod.OPTIONS)
          .allowedMethod(HttpMethod.PUT)
          .allowedMethod(HttpMethod.DELETE)
          .allowedHeader("token")
          .allowedHeader("apiKey")
          .allowedHeader("Authorization")
          .allowedHeader("Access-Control-Allow-Method")
          .allowedHeader("Access-Control-Allow-Origin")
          .allowedHeader("Access-Control-Allow-Credentials")
          .allowedHeader("Content-Type"));

      // Enable multipart form data parsing for all POST API requests.
      router.route().handler(BodyHandler.create());
      router.get("/").handler(this::ping);

      // Set the routes for location.
      this.setUserRoutes(router);
  }

    /**
     * Sets the system health check.
     * @param router The router used to set paths.
     */
    private void setHealthCheck(final Router router) {
        HealthCheckHandler health = HealthCheckHandler.create(this.vertx);
        health.register("ws", WAIT_TIME, f -> f.complete(Status.OK()));

        router.get("/health").handler(health);

        HealthCheckHandler healthz = HealthCheckHandler.create(this.vertx);
        healthz.register("ws", WAIT_TIME,
            f -> f.complete(Status.OK()));

        router.get("/healthz").handler(healthz);
    }

    /**
     * Creates a web socket server.
     * @param custowsmport The custom port.
     * @param opts The web server options.
     * @param handler The web socket result handler.
     */
    private void createWebSocket(final int custowsmport,
        final HttpServerOptions opts,
        final Handler<AsyncResult<HttpServer>> handler) {
            if (opts == null) {
                this.vertx.createHttpServer()
                    .webSocketHandler(this::makeWsRequest)
                        .listen(custowsmport, handler);
            } else {
                this.vertx.createHttpServer(opts)
                    .webSocketHandler(this::makeWsRequest)
                        .listen(custowsmport, handler);
            }
    }

    /**
     * Makes web socket requests.
     * @param ws The web socket utils.
     */
    private void makeWsRequest(final ServerWebSocket ws) {
        this.logger.info("makeWsRequest(uri = {"
            + ws.uri()
            + "}, path = {" + ws.path() + "}) ->");
        String path = ws.path() == null
            ? ""
            : ws.path();
        if (path.endsWith("")) {
            ws.writeTextMessage(this.getResponse(
                ERR_500, "Not implemented endpoint!").encode());
            ws.close();
        } else {
            ws.writeTextMessage(this.getResponse(
                ERR_500, "Unsupported endpoint!").encode());
            ws.close();
        }
    }

    /**
     * Closes all the resources that were opened.
     * @param completionHandler The complention handler.
     */
    public void close(final Handler<AsyncResult<Void>> completionHandler) {
        this.vertx.close(completionHandler);
    }

    /**
     * Pings the server.
     * @param rc The routing context.
     */
    private void ping(final RoutingContext rc) {
      this.logger.info("ping() ->");
      rc.response().end(getResponse("alive").encode());
    }

}
