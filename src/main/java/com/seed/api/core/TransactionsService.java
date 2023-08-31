package com.seed.api.core;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
     * Handler for pushing battery data.
     * @param routingContext The routing context.
     */
    private void handlePushBatteryData(final RoutingContext routingContext) {
        // Extract battery data from the request body
        JsonObject batteryData = routingContext.getBodyAsJson();

        // Validate and process battery data
        // (store in the database, etc.)
        try {
            double current = batteryData.getDouble("current");
            double voltage = batteryData.getDouble("voltage");

            if (current < 0 || voltage < 0) {
                JsonObject errorResponse = new JsonObject()
                    .put("message", "Invalid battery data");
                routingContext.response()
                    .setStatusCode(ERR_BAD_REQUEST)
                    .putHeader("Content-Type", "application/json")
                    .end(errorResponse.encode());
                return;
            }

            // Store battery data in the database
            boolean success = storeBatteryDataInDatabase(current, voltage);

            if (success) {
                JsonObject response = new JsonObject()
                    .put("message", "Battery data stored successfully");
                routingContext.response()
                    .setStatusCode(SUCCESS_CODE)
                    .putHeader("Content-Type", "application/json")
                    .end(response.encode());
            } else {
                JsonObject errorResponse = new JsonObject()
                    .put("message", "Failed to store battery data");
                routingContext.response()
                    .setStatusCode(ERR_500)
                    .putHeader("Content-Type", "application/json")
                    .end(errorResponse.encode());
            }
        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject()
                .put("message", "Error processing battery data");
            routingContext.response()
                .setStatusCode(ERR_500)
                .putHeader("Content-Type", "application/json")
                .end(errorResponse.encode());
        }
    }

    /**
     * Handler for retrieving battery data.
     * @param routingContext The routing context.
     */
    // Handler for retrieving battery data.
    private void handleRetrieveBatteryData(
      final RoutingContext routingContext
      ) {
      // Retrieve battery data from the database
      // (replace with your actual retrieval logic)
      double batteryCurrent = BATTERY_CURRENT;
      double batteryVoltage = BATTERY_VOLTAGE;
      // Store battery data in the database
      boolean storedSuccessfully =
      storeBatteryDataInDatabase(batteryCurrent, batteryVoltage);
      if (storedSuccessfully) {
          JsonObject response = new JsonObject()
              .put("message", "Battery data stored successfully")
              .put("current", batteryCurrent)
              .put("voltage", batteryVoltage);
          routingContext.response()
              .setStatusCode(SUCCESS_CODE)
              .putHeader("Content-Type", "application/json")
              .end(response.encode());
      } else {
          JsonObject errorResponse = new JsonObject()
              .put("message", "Failed to store battery data");
          routingContext.response()
              .setStatusCode(ERR_500)
              .putHeader("Content-Type", "application/json")
              .end(errorResponse.encode());
      }
  }



  private boolean storeBatteryDataInDatabase(
    final double current, final double voltage) {
    try {
        // Load the Oracle JDBC driver
        //(replace with the actual class name of the Oracle driver)
        Class.forName("oracle.jdbc.driver.OracleDriver");

        // Establish a database connection
        //(replace with your actual database URL, username, and password)
        Connection connection = DriverManager.getConnection(
          "jdbc:oracle:thin:@//your.oracle.server:1521/yourdb",
           "username", "password");

        // Prepare a SQL statement to insert battery data
        String sql =
        "INSERT INTO battery_data(current, voltage) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setDouble(1, current);
        statement.setDouble(2, voltage);

        // Execute the insert
        int rowsInserted = statement.executeUpdate();

        // Close the database resources
        statement.close();
        connection.close();

        // Return true if the insert was successful, false otherwise
        return rowsInserted > 0;
    } catch (ClassNotFoundException | SQLException e) {
        // Handle any database errors here
        e.printStackTrace();
        return false;
    }
}


}
