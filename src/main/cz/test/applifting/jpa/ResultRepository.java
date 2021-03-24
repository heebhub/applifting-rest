package cz.test.applifting.jpa;

import cz.test.applifting.jpa.entity.Endpoint;
import cz.test.applifting.jpa.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository for {@link Result}
 */
public interface ResultRepository extends JpaRepository<Result, Long> {
     List<Result> findTop10ByEndpointOrderByUpdatedOnDesc(Endpoint endpoint);
}
