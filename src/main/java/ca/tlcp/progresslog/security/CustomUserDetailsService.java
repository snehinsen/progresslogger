package ca.tlcp.progresslog.security;

import ca.tlcp.progresslog.entities.users.UserAccount;
import ca.tlcp.progresslog.entities.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Email: " + email);
        Optional<UserAccount> optionalUserAccount = repository.findByEmail(email);
        if (optionalUserAccount.isPresent()) {
            UserAccount userAccount = optionalUserAccount.get();
            System.out.println(String.format("User %s found in database.", userAccount.getFullName()));
            return User.builder()
                    .username(userAccount.getEmail())
                    .password(userAccount.getPassword())
                    .roles(getRoles(userAccount))
                    .build();
        } else {
            System.out.println("Error finding user.");
            throw new UsernameNotFoundException(email);
        }
    }

    private String[] getRoles(UserAccount user) {
        if (user.getRole() == null) {
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
    }
}