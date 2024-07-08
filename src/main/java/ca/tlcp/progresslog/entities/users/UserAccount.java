package ca.tlcp.progresslog.entities.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uuid")
    private long UUID;
    @Column(nullable = false, name = "password")
    private String password;
    @Column(nullable = false, name = "fullname")
    private String fullName;
    @Column(unique = true, nullable = false, name = "email")
    private String email;
    @Column(nullable = false, name = "role")
    private String role;
    @Column(name = "pfp")
    private String PFPLocation;
}
