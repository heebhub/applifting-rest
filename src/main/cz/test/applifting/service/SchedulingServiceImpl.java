package cz.test.applifting.service;

import cz.test.applifting.jpa.EndpointRepository;
import cz.test.applifting.jpa.ResultRepository;
import cz.test.applifting.jpa.entity.Endpoint;
import cz.test.applifting.jpa.entity.Result;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Setter
@Slf4j
public class SchedulingServiceImpl implements SchedulingService {

     private ScheduledExecutorService taskScheduler = Executors.newScheduledThreadPool(1);
     private HashMap<Long, ScheduledFuture<?>> monitoringTasks = new HashMap<>();

     @Autowired
     private ResultRepository resultRepository;

     @Autowired
     private EndpointRepository endpointRepository;

     public SchedulingServiceImpl(ResultRepository resultRepository, EndpointRepository endpointRepository) {
          this.resultRepository = resultRepository;
          this.endpointRepository = endpointRepository;

          endpointRepository.findAll().forEach(
               this::addMonitor
          );
     }

     public void addMonitor(Endpoint endpoint) {
          final var endpointId = endpoint.getId();
          final Runnable runnable = () -> requestEndpoint(endpoint);
          final var interval = endpoint.getTimeInterval();

          log.info("Started monitoring an endpoint with id {} with time interval {}.", endpoint.getId(), interval);
          final var newTask = taskScheduler.scheduleAtFixedRate(runnable, 0, interval, TimeUnit.SECONDS);

          if (monitoringTasks.containsKey(endpoint.getId())) {
               monitoringTasks.get(endpointId).cancel(true);
          }
          monitoringTasks.put(endpointId, newTask);
     }

     public void requestEndpoint(Endpoint endpoint) {
          final var restTemplate = new RestTemplate();
          final ResponseEntity<String> response = restTemplate.exchange(
               endpoint.getUrl(), HttpMethod.GET, null, String.class
          );
          final HttpStatus responseBody = response.getStatusCode();

          log.info("Response {} for endpoint with id: {}.", responseBody.value(), endpoint.getId());
          resultRepository.save(
               new Result(responseBody.value(), response.getBody(), endpoint)
          );
          endpointRepository.updateTimestamp(endpoint.getId());
     }
}
