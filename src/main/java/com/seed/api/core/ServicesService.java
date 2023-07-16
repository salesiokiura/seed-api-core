package com.seed.api.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.ext.web.Router;

/**
 * The services service class.
 */
public class ServicesService extends TransactionsService {

    /**
     * The logger utils class.
     */
    private Logger logger = LoggerFactory.getLogger(
      ServicesService.class.getName());

    /**
     * Sets routes for the http server.
     * @param router The router used to set paths.
     */
    protected void setServicesRoutes(final Router router) {

      // Set the routes for location.
      this.setTransactionsRoutes(router);
    }

}
