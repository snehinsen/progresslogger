package ca.tlcp.progresslog.entities.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {


    @Query(nativeQuery = true)
    Optional<UserAccount> findByEmail(String email);


}