package dk.krakow.dev.securityexample.repo;

import dk.krakow.dev.securityexample.model.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

	AuthToken findByTokenAndEnabled(String token, boolean enabled);
}
