package cz.test.applifting.service;

import cz.test.applifting.jpa.entity.Endpoint;

/**
 * Interface for scheduling service.
 */
public interface SchedulingService {

     /**
      * Adds a new monitor to the scheduler.
      *
      * @param endpoint        endpoint to monitor {@link Endpoint}
      */
     void addMonitor(final Endpoint endpoint);

     /**
      * Monitors a selected endpoint
      *
      * @param endpoint        monitored endpoint {@link Endpoint}
      */
     void requestEndpoint(final Endpoint endpoint);
}
