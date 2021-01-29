package pl.morytko.moviemax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class SpringApplicationRun {
    public static void main(String[] args) {
        SpringApplication.run(SpringApplicationRun.class, args);
    }

}


