package ca.tlcp.progresslog.entities.messages;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "msgBody")
    private String messageBody;
    @Column(name = "msgThreadID")
    private long threadID;
    @Column(name = "fromUser")
    private String fromUser;
    private LocalDateTime timestamp;
}
