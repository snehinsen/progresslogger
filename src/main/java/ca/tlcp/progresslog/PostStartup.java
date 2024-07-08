package ca.tlcp.progresslog;


import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class PostStartup {

    @PostConstruct
    public void init() {
    }

}
