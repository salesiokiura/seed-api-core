package com.seed.api.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.ext.web.Router;

/**
 * The user service class.
 */
public class UserService extends ServicesService {

    /**
     * The logger utils class.
     */
    private Logger logger = LoggerFactory.getLogger(
      UserService.class.getName());

    /**
     * Sets routes for the http server.
     * @param router The router used to set paths.
     */
    protected void setUserRoutes(final Router router) {

      // Set the routes for location.
      this.setServicesRoutes(router);
    }

}
