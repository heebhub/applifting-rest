package cz.test.applifting.service;

import cz.test.applifting.jpa.entity.Endpoint;
import cz.test.applifting.jpa.entity.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MonitoringService {
     ResponseEntity<Endpoint> getEndpointById(final Long id, final String token);

     ResponseEntity<List<Endpoint>> getAllEndpoints();

     ResponseEntity<HttpStatus> deleteEndpoint(final Long id, final String token);

     ResponseEntity<HttpStatus> saveEndpoint(final Endpoint endpoint, final String token);

     ResponseEntity<List<Result>> getLastResults(final Long id, final String token);

     ResponseEntity<HttpStatus> updateEndpoint(Long id, Endpoint updatedEndpoint, String token);
}
