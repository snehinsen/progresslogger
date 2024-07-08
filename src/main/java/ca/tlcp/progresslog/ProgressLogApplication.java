package ca.tlcp.progresslog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Files;

@SpringBootApplication
public class ProgressLogApplication {

    public static void main(String[] args) {
        File storage = new File(ServerUtils.PFP_SAVE_DIRECTORY);
        if (!storage.exists()) {
            storage.mkdir();
        }
        SpringApplication.run(ProgressLogApplication.class, args);
    }

}
