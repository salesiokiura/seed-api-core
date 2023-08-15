package com.seed.api.core;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.http.HttpMethod;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The base service class for setting up routes
 * and handlers for various services.
 * Some of the services include
 * 1. Contact
 * 2. Book Slot
 * 3. Book to collect Device
 * 4. Check Slot
 * Extend this class to define routes and handlers for specific services.
 */
public class ServicesService extends TransactionsService {

    /**
     * The logger utils class.
     */
    private Logger logger = LoggerFactory.getLogger(
      ServicesService.class.getName());

    /**
     * Sets routes for the http server, including
     * routes for additional services.
     *
     * @param router The router used to set paths.
     */
    protected void setServicesRoutes(final Router router) {
        // Set the routes for location.
        this.setTransactionsRoutes(router);

        // Add routes for additional services (Contact and Book)
        setContactRoutes(router);
        setBookRoutes(router);
    }

    /**
     * Sets routes for the Contact service.
     *
     * @param router The router used to set paths.
     */
    /*
    |
    |
    | 1. CONTACT US SERVICE
    |
    |
    */
    protected void setContactRoutes(final Router router) {
        router.route("/contact/*").handler(BodyHandler.create());

        CorsHandler corsHandler = CorsHandler.create("*")
            .allowedMethods(Set.of(HttpMethod.POST));

        router.route("/contact/*").handler(corsHandler);

        // Route for submitting contact information
        router.post("/contact/submit").handler(this::handleContactSubmission);
    }

    /**
     * Handles submission of contact information.
     *
     * @param routingContext The routing context.
     */
    protected void handleContactSubmission(
      final RoutingContext routingContext) {
        JsonObject contactData = routingContext.getBodyAsJson();
        // logic to process contact data goes here

        // For now, we assume submission is successful
        JsonObject response = new JsonObject()
            .put("message", "Contact submission successful")
            .put("data", contactData);

        routingContext.response()
            .setStatusCode(SUCCESS_CODE)
            .putHeader("Content-Type", "application/json")
            .end(response.encode());
    }

    /**
     * Sets routes and handlers for the Book service.
     *
     * @param router The router used to set paths.
     */

    /*
    |
    |
    | 2. Book Slot
    |
    |
    */
    protected void setSlotRoutes(final Router router) {
        router.route("/slot/*").handler(BodyHandler.create());

        CorsHandler corsHandler = CorsHandler.create("*")
            .allowedMethods(Set.of(HttpMethod.POST));

        router.route("/slot/*").handler(corsHandler);

        // Route for booking a device
        router.post("/slot/book").handler(this::handleSlotBooking);
    }

    /**
     * Handles booking slot of a device.
     *
     * @param routingContext The routing context.
     */
    protected void handleSlotBooking(final RoutingContext routingContext) {
        JsonObject bookingData = routingContext.getBodyAsJson();
        // logic to process slot booking goes here

        // For now, we assume booking is successful
        JsonObject response = new JsonObject()
            .put("message", "Slot booking successful")
            .put("data", bookingData);

        routingContext.response()
            .setStatusCode(SUCCESS_CODE)
            .putHeader("Content-Type", "application/json")
            .end(response.encode());
    }


     /**
     * Sets routes and handlers for the Book service.
     *
     * @param router The router used to set paths.
     */
    /*
    |
    |
    | 3. Book to collect Device
    |
    |
    */
    protected void setBookRoutes(final Router router) {
        router.route("/book/*").handler(BodyHandler.create());

        CorsHandler corsHandler = CorsHandler.create("*")
            .allowedMethods(Set.of(HttpMethod.POST));

        router.route("/book/*").handler(corsHandler);

        // Route for booking a device
        router.post("/book/device").handler(this::handleDeviceBooking);
    }

    /**
     * Handles booking of a device.
     *
     * @param routingContext The routing context.
     */
    protected void handleDeviceBooking(final RoutingContext routingContext) {
        JsonObject bookingData = routingContext.getBodyAsJson();
        // logic to process device booking goes here

        // For now, we assume booking is successful
        JsonObject response = new JsonObject()
            .put("message", "Device booking successful")
            .put("data", bookingData);

        routingContext.response()
            .setStatusCode(SUCCESS_CODE)
            .putHeader("Content-Type", "application/json")
            .end(response.encode());
    }
}
