package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VehicleRentalApp implements CommandLineRunner {

    private final UserInterface userInterface;

    public VehicleRentalApp(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public static void main(String[] args) {
        SpringApplication.run(VehicleRentalApp.class, args);
    }

    @Override
    public void run(String... args) {
        userInterface.start();
    }
}