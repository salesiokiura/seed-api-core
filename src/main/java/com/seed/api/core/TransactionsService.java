package com.seed.api.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.ext.web.Router;

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
     * Sets routes for the http server.
     * @param router The router used to set paths.
     */
    protected void setTransactionsRoutes(final Router router) {

      // Set the routes for location.
      this.setPaymentsRoutes(router);
    }

}
