package cz.test.applifting.jpa;

import cz.test.applifting.jpa.entity.Endpoint;
import cz.test.applifting.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * JPA repository for {@link Endpoint}
 */
public interface EndpointRepository extends JpaRepository<Endpoint, Long> {
     List<Endpoint> findAll();

     Optional<Endpoint> findEndpointByIdAndUser(final Long id, final User user);

     Optional<Endpoint> findEndpointById(final Long id);

     @Modifying
     @Transactional
     @Query("update Endpoint e set e.lastVisited = CURRENT_TIMESTAMP WHERE e.id = :id")
     void updateTimestamp(final @Param("id") Long endpointId);
}
