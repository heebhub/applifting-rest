package cz.test.applifting.service;

import cz.test.applifting.jpa.entity.Endpoint;

public interface SchedulingService {

     void addMonitor(final Endpoint endpoint);
     void requestEndpoint(final Endpoint endpoint);
}
