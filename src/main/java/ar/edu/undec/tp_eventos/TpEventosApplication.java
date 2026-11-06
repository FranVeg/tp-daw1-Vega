package ar.edu.undec.tp_eventos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TpEventosApplication {

    public static void main(String[] args) {
        SpringApplication.run(TpEventosApplication.class, args);
    }
}