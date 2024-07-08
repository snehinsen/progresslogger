package ca.tlcp.progresslog.entities.roles;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {
    @Id
    private String roleName;
    private boolean canCreateThreads;
    private boolean canRespondToThreads;
    private boolean canDeleteThreads;
    private boolean canManageUsers;
    private boolean canManageThreads;
}
