package dk.krakow.dev.securityexample.repo;

import dk.krakow.dev.securityexample.model.Privilege;
import dk.krakow.dev.securityexample.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
	Privilege findByName(String name);
}
