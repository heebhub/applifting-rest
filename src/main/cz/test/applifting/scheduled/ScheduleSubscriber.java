package cz.test.applifting.scheduled;

import cz.test.applifting.jpa.entity.Endpoint;
import cz.test.applifting.service.SchedulingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Flow;

/**
 * Subscriber for endpoints for following monitoring.
 */
@Slf4j
@Component
public class ScheduleSubscriber implements Flow.Subscriber<Endpoint> {

     @Autowired
     private final SchedulingService service;

     public ScheduleSubscriber(SchedulingService service) {
          this.service = service;
     }

     @Override
     public void onSubscribe(Flow.Subscription subscription) {
          subscription.request(1);
     }

     @Override
     public void onNext(Endpoint item) {
          service.addMonitor(item);
     }

     @Override
     public void onError(Throwable throwable) {
          log.error("Couldn't execute the subscription.", throwable);
     }

     @Override
     public void onComplete() {
          log.info("Done");
     }
}
