package com.seed.api.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

@ExtendWith(VertxExtension.class)
public class PingTest {
    
    private CoreService service;

    private int port;

    @BeforeEach
    public void before(final VertxTestContext tc) {
        try {
            this.port = TestUtils.getFreePort();
            int wsport = TestUtils.getFreePort();
            this.service = new CoreService();

            this.service.startHttpServer(this.port, wsport, h -> {
                if (h.succeeded()) {
                    tc.completeNow();
                } else {
                    tc.completeNow();
                }
            }, wsh -> { });   
        } catch (final Exception e) {
            assertTrue(false, "Error -> " + TestUtils.errToStr(e));
        }
    }

    @AfterEach
    public void after(final VertxTestContext tc) {
        if (this.service != null) {
            this.service.close(tc.succeedingThenComplete());
        }
    }

    @Test
    public void ping(final Vertx vertx, final VertxTestContext tc) {
        HttpClient client = vertx.createHttpClient();
        client.request(HttpMethod.GET, this.port, "localhost", "/")
            .compose(req -> req.send().compose(HttpClientResponse::body))
            .onComplete(tc.succeeding( buffer -> tc.verify(() -> {
                String res = buffer.toString();
                assertNotNull(res, "Result is null!");
                JsonObject body = new JsonObject(res);
                assertEquals(BaseService.SUCCESS_CODE,
                    body.getInteger("status"), "Status is invalid");
                tc.completeNow();
            })));
    }

}
