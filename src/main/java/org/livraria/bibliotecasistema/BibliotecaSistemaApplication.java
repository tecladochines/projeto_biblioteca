package org.livraria.bibliotecasistema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BibliotecaSistemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaSistemaApplication.class, args);
    }

}
