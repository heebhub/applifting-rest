package cz.test.applifting.service;

import cz.test.applifting.jpa.EndpointRepository;
import cz.test.applifting.jpa.ResultRepository;
import cz.test.applifting.jpa.UserRepository;
import cz.test.applifting.jpa.entity.Endpoint;
import cz.test.applifting.jpa.entity.Result;
import cz.test.applifting.jpa.entity.User;
import cz.test.applifting.scheduled.ScheduleSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.concurrent.SubmissionPublisher;

/**
 * {@link MonitoringService} implementation
 */
@Service
@Slf4j
public class MonitoringServiceImpl implements MonitoringService {

     private static final String ACCESS_DENIED = "Access Denied.";

     private final EndpointRepository endpointRepository;
     private final ResultRepository resultRepository;
     private final UserRepository userRepository;
     private final SubmissionPublisher<Endpoint> publisher;

     public MonitoringServiceImpl(final EndpointRepository endpointRepository,
                                  final ResultRepository resultRepository,
                                  final UserRepository userRepository,
                                  final SchedulingService schedulingServiceImpl) {
          this.endpointRepository = endpointRepository;
          this.resultRepository = resultRepository;
          this.userRepository = userRepository;

          this.publisher = new SubmissionPublisher<>();
          publisher.subscribe(new ScheduleSubscriber(schedulingServiceImpl));
     }

     @Override
     public ResponseEntity<List<Endpoint>> getAllEndpoints() {
          return ResponseEntity.ok(endpointRepository.findAll());
     }

     @Override
     public ResponseEntity<HttpStatus> deleteEndpoint(final Long id, final String token) {
          final var endpoint = endpointRepository.findEndpointById(id).orElseThrow(() -> {
               log.info(ACCESS_DENIED);
               throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
          });

          log.info("Deleting an endpoint with id {}", endpoint.getId());
          endpointRepository.delete(endpoint);

          return ResponseEntity.ok(HttpStatus.OK);
     }

     @Override
     public ResponseEntity<HttpStatus> saveEndpoint(final Endpoint endpoint, final String token) {
          final var user = validateUser(token);

          log.info("Found a user with id {} and username {}.", user.getId(), user.getUsername());
          endpoint.setUser(user);

          log.info("Saving and subscribing an endpoint with id {}", endpoint.getId());
          endpointRepository.save(endpoint);
          publisher.submit(endpoint);

          return ResponseEntity.ok(HttpStatus.CREATED);
     }

     @Override
     public ResponseEntity<List<Result>> getLastResults(final Long id, final String token) {
          final var user = validateUser(token);
          var endpoint = endpointRepository.findEndpointByIdAndUser(id, user).orElseThrow(() -> {
               log.info(ACCESS_DENIED);
               throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
          });

          log.info("Getting ten recent results for endpoint with id {}", endpoint.getId());
          return ResponseEntity.ok(
               resultRepository.findTop10ByEndpointOrderByUpdatedOnDesc(endpoint)
          );
     }

     @Override
     public ResponseEntity<Endpoint> getEndpointById(final Long id, final String token) {
          final var user = validateUser(token);

          log.info("Getting an endpoint with id {}", id);
          return ResponseEntity.ok(
               endpointRepository.findEndpointByIdAndUser(id, user).orElseThrow(() -> {
                    log.info(ACCESS_DENIED);
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
               }));
     }

     @Override
     public ResponseEntity<HttpStatus> updateEndpoint(Long id, Endpoint updatedEndpoint, String token) {
          final var user = validateUser(token);

          var endpoint = endpointRepository.findEndpointById(id).orElseThrow(() -> {
               log.info("Couldn't find an endpoint with id {}", id);
               throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Endpoint id was not found");
          });

          endpointRepository.findEndpointByIdAndUser(id, user).orElseThrow(() -> {
               log.info(ACCESS_DENIED);
               throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
          });

          log.info("Found the endpoint, updating...");
          endpoint.setUser(user);
          endpoint.setName(updatedEndpoint.getName());
          endpoint.setUrl(updatedEndpoint.getUrl());
          endpoint.setTimeInterval(updatedEndpoint.getTimeInterval());
          endpoint.setLastVisited(new Date());

          endpointRepository.save(endpoint);
          publisher.submit(endpoint);

          return ResponseEntity.ok(HttpStatus.OK);
     }

     private User validateUser(final String token) {
          return userRepository.findByToken(token).orElseThrow(() -> {
               log.error("Couldn't find a user by token {}", token);
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't find a user by this token");
          });
     }
}
