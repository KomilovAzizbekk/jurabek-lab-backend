package uz.mediasolutions.jurabeklabbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JurabekLabBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(JurabekLabBackendApplication.class, args);
    }

}
