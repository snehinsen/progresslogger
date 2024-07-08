package ca.tlcp.progresslog.entities.roles;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> getRoleByRoleName(String roleName);

}
