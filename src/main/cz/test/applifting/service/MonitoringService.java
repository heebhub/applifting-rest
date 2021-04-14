package cz.test.applifting.service;

import cz.test.applifting.jpa.entity.Endpoint;
import cz.test.applifting.jpa.entity.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Interface for monitoring service.
 */
public interface MonitoringService {

     /**
      * Gets and endpoint by its id and user's token.
      *
      * @param id        endpoint's id
      * @param token     user's token
      *
      * @return selected endpoint. If the endpoint is not found or the user validation fails, {@link ResponseStatusException} is thrown.
      */
     ResponseEntity<Endpoint> getEndpointById(final Long id, final String token);

     /**
      * Gets all endpoints.
      *
      * @return list of endpoints as a response {@link ResponseEntity}
      */
     ResponseEntity<List<Endpoint>> getAllEndpoints();

     /**
      * Deletes an endpoint by its id and user's token
      *
      * @param id        endpoint's id
      * @param token     user's token
      *
      * @return {@link HttpStatus}. If the endpoint is not found or the user validation fails, {@link ResponseStatusException} is thrown.
      */
     ResponseEntity<HttpStatus> deleteEndpoint(final Long id, final String token);

     /**
      * Saves an endpoint and attaches it to the user by its token.
      *
      * @param endpoint        inserted endpoint
      * @param token           user's token
      *
      * @return {@link HttpStatus} If user validation fails, {@link ResponseStatusException} is thrown.
      */
     ResponseEntity<HttpStatus> saveEndpoint(final Endpoint endpoint, final String token);

     /**
      * Gets recent 10 last results.
      *
      * @param id        endpoint's id
      * @param token     user's token
      *
      * @return {@link HttpStatus} If the endpoint is not found or the user validation fails, {@link ResponseStatusException} is thrown.
      */
     ResponseEntity<List<Result>> getLastResults(final Long id, final String token);

     /**
      * Updates the selected endpoint.
      *
      * @param id                       endpoint's id
      * @param updatedEndpoint          new endpoint data
      * @param token                    user's token
      *
      * @return {@link HttpStatus} If the endpoint is not found or the user validation fails, {@link ResponseStatusException} is thrown.
      */
     ResponseEntity<HttpStatus> updateEndpoint(Long id, Endpoint updatedEndpoint, String token);
}
