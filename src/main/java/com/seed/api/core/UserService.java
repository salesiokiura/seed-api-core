package com.seed.api.core;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService extends ServicesService {

    /**
     * The logger utils class.
     */
    private Logger logger = LoggerFactory.getLogger(UserService.class.getName());

    /**
     * Sets routes for the http server related to user management (login and registration).
     * @param router The router used to set paths.
     */
    protected void setUserRoutes(final Router router) {
        // Enable parsing of request body for POST requests
        router.route().handler(BodyHandler.create());

        // CORS configuration to allow requests from any origin
        router.route().handler(CorsHandler.create("*").allowedMethods(HttpMethod.POST));

        // Route for user registration
        router.post("/register").handler(this::handleUserRegistration);

        // Route for user login
        router.post("/login").handler(this::handleUserLogin);

        // Set the routes for location (if there are any additional routes for user management)
        this.setServicesRoutes(router);
    }

    /**
     * Handler for user registration.
     * @param routingContext The routing context.
     */
    private void handleUserRegistration(final RoutingContext routingContext) {
        // Extract user registration data from the request body
        JsonObject registrationData = routingContext.getBodyAsJson();

        // Extract individual fields from the registration data
        String fullName = registrationData.getString("full_name");
        String phoneNumber = registrationData.getString("phone_number");
        String email = registrationData.getString("email");
        String password = registrationData.getString("password");
        String confirmPassword = registrationData.getString("confirm_password");

        // Validate the registration data (e.g., check for required fields, email format, password match, etc.)
        if (fullName == null || fullName.isEmpty() ||
            phoneNumber == null || phoneNumber.isEmpty() ||
            email == null || email.isEmpty() ||
            password == null || password.isEmpty() ||
            confirmPassword == null || confirmPassword.isEmpty()) {

            // If any of the required fields are missing, return an error response
            JsonObject errorResponse = new JsonObject()
                .put("message", "Please fill in all the required fields.");
            routingContext.response()
                .setStatusCode(400)
                .putHeader("Content-Type", "application/json")
                .end(errorResponse.encode());
        } else if (!password.equals(confirmPassword)) {
            // If the passwords do not match, return an error response
            JsonObject errorResponse = new JsonObject()
                .put("message", "Password and Confirm Password do not match.");
            routingContext.response()
                .setStatusCode(400)
                .putHeader("Content-Type", "application/json")
                .end(errorResponse.encode());
        } else {
            // For now, assume registration is successful
            JsonObject response = new JsonObject()
                .put("message", "User registration successful")
                .put("full_name", fullName)
                .put("phone_number", phoneNumber)
                .put("email", email);

            routingContext.response()
                .setStatusCode(200)
                .putHeader("Content-Type", "application/json")
                .end(response.encode());
        }
    }

    /**
     * Handler for user login.
     * @param routingContext The routing context.
     */
    private void handleUserLogin(final RoutingContext routingContext) {
        // Extract user login credentials from the request body
        JsonObject loginCredentials = routingContext.getBodyAsJson();

        // Validate login credentials (e.g., check against database, verify password, etc.)
        // Your authentication logic goes here...

        // For now, assume login is successful
        JsonObject response = new JsonObject()
            .put("message", "User login successful")
            .put("username", loginCredentials.getString("username"));

        routingContext.response()
            .setStatusCode(200)
            .putHeader("Content-Type", "application/json")
            .end(response.encode());
    }

   
}
