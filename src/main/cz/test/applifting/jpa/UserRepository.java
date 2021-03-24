package cz.test.applifting.jpa;

import cz.test.applifting.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA repository for {@link User}
 */
public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findByToken(final String token);
}
