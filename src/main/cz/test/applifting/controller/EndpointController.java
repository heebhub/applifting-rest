package cz.test.applifting.controller;

import cz.test.applifting.jpa.entity.Endpoint;
import cz.test.applifting.jpa.entity.Result;
import cz.test.applifting.service.MonitoringServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Slf4j
public class EndpointController {

    private final MonitoringServiceImpl monitoringService;

    /**
     * Gets the list of all recorded endpoints.
     *
     * @return response       {@link ResponseEntity}
     */
    @GetMapping("/endpoints")
    public ResponseEntity<List<Endpoint>> getEndpoints() {
        return monitoringService.getAllEndpoints();
    }

     /**
      * Gets an endpoint by its id
      *
      * @param id              selected endpoint's id
      * @param token           user's token
      *
      * @return response       {@link ResponseEntity}
      */
     @GetMapping("/endpoints/{id}")
     public ResponseEntity<Endpoint> getEndpoint(@PathVariable final Long id,
                                                 @RequestHeader(value = "accessToken") final String token) {
          return monitoringService.getEndpointById(id, token);
     }

     /**
      * Gets the endpoint by its id and user's token.
      *
      * @param id              selected endpoint's id
      * @param token           user's token
      *
      * @return response       {@link ResponseEntity}
      */
     @GetMapping("/endpoints/{id}/results")
     public ResponseEntity<List<Result>> getRecentResults(@PathVariable final Long id,
                                          @RequestHeader(value = "accessToken") final String token) {
          log.info("Trying to get recent results for endpoint id {} and token {}", id, token);
          return monitoringService.getLastResults(id, token);
     }

     /**
      * Updates selected endpoint.
      *
      * @param id              selected endpoint's id
      * @param endpoint        endpoint to update {@link Endpoint}
      * @param token           user's token
      *
      * @return response       {@link ResponseEntity}
      */
     @PutMapping("/endpoints/{id}")
     public ResponseEntity<HttpStatus> putEndpoint(@PathVariable final Long id,
                                                   @RequestBody @Valid final Endpoint endpoint,
                                                   @RequestHeader(value = "accessToken") final String token) {
          return monitoringService.updateEndpoint(id, endpoint, token);
     }

     /**
      * Adds a new endpoint.
      *
      * @param endpoint        endpoint to update {@link Endpoint}
      * @param token           user's token
      *
      * @return response       {@link ResponseEntity}
      */
    @PostMapping("/endpoints")
    public ResponseEntity<HttpStatus> postEndpoint(@RequestBody @Valid Endpoint endpoint,
                             @RequestHeader(value="accessToken") final String token) {
        return monitoringService.saveEndpoint(endpoint, token);
    }

     /**
      * Deletes selected endpoint by its id.
      *
      * @param id              selected endpoint's id
      * @param token           user's token
      *
      * @return response       {@link ResponseEntity}
      */
     @DeleteMapping("/endpoints/{id}")
     public ResponseEntity<HttpStatus> deleteEndpoint(@PathVariable final Long id,
                                @RequestHeader(value="accessToken") final String token) {
          return monitoringService.deleteEndpoint(id, token);
     }
}
