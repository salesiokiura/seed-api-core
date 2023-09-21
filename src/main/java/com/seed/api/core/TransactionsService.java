package com.seed.api.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;



/**
 * The Transactions services class.
 */
public class TransactionsService extends PaymentsService {

    /**
     * The logger utils class.
     */
    private Logger logger = LoggerFactory.getLogger(
      TransactionsService.class.getName());

    /**
     * Static sample data collected from the sensor.
     */
    private static final double BATTERY_CURRENT = 3.7;
    /**
     * voltage percentage.
     */
    private static final double BATTERY_VOLTAGE = 10.5;

    /**
     * Sets routes for the http server.
     * @param router The router used to set paths.
     */
    protected void setTransactionsRoutes(final Router router) {
        // Set the routes for location.
        this.setPaymentsRoutes(router);

        // Route for pushing battery data
        router.post("/push-battery-data")
        .handler(this::handlePushBatteryData);

        // Route for retrieving battery data
        router.get("/retrieve-battery-data")
        .handler(this::handleRetrieveBatteryData);
    }
    /**
     * Handles the battery data received in the request and
     * returns a JSON object.
     *
     * @param routingContext The routing context for handling the HTTP request.
     * @return A JSON object representing the response.
     */
    private JsonObject handlePushBatteryData(
    final RoutingContext routingContext) {
        // Extract battery data from the request body
        JsonObject batteryData = routingContext.getBodyAsJson();

        // Validate and process battery data
        try {
            double current = batteryData.getDouble("current");
            double voltage = batteryData.getDouble("voltage");

            if (current < 0 || voltage < 0) {
                return new JsonObject()
                    .put("message", "Invalid battery data");
            }

            // Create a response JSON object
            JsonObject response = new JsonObject()
                .put("message", "Battery data stored successfully");

            return response;
        } catch (Exception e) {
            return new JsonObject()
                .put("message", "Error processing battery data");
        }
    }


    /**
     * Handler for retrieving battery data.
     * @param routingContext The routing context.
     * @return A JSON object containing battery data.
     */
    private JsonObject handleRetrieveBatteryData(
        final RoutingContext routingContext) {
        // Retrieve battery data from wherever you want
        //(replace with your actual retrieval logic)
        double batteryCurrent = BATTERY_CURRENT;
        double batteryVoltage = BATTERY_VOLTAGE;

        // Create a response JSON object with the retrieved data
        JsonObject response = new JsonObject()
            .put("message", "Battery data retrieved successfully")
            .put("current", batteryCurrent)
            .put("voltage", batteryVoltage);

        return response;
    }


    /**
     * Creates a JSON object containing battery data.
     *
     * This method takes the current and voltage values and
     * creates a JSON object
     * with the following structure:
     *
     * {
     *    "current": [current_value],
     *    "voltage": [voltage_value]
     * }
     *
     * @param current The current value to be included in the JSON object.
     * @param voltage The voltage value to be included in the JSON object.
     * @return A JSON object containing battery data.
     */
    public JsonObject storeBatteryDataInJsonObject(
        final double current, final double voltage) {
        JsonObject result = new JsonObject();

        // Instead of connecting to the database, create a JSON object
        result.put("current", current);
        result.put("voltage", voltage);

        // Return the JSON object
        return result;
    }



}
