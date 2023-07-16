package com.seed.api.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.ext.web.Router;

/**
 * The payments service class.
 */
public class PaymentsService  extends BaseService {

    /**
     * The logger utils class.
     */
    private Logger logger = LoggerFactory.getLogger(
      PaymentsService.class.getName());

    /**
     * Sets routes for the http server.
     * @param router The router used to set paths.
     */
    protected void setPaymentsRoutes(final Router router) {

      // Set the routes for location.
      this.setBaseRoutes(router);
    }

}
