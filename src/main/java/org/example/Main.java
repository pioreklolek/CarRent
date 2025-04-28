package org.example;


public class Main {
    public static void main(String[] args) {
         new UserInterface().start();


        VehiclesRepository vehiclesRepository = new VehiclesRepository();


        System.out.println("Lista pojazdów w repozytorium:");
        for (Vehicle v : vehiclesRepository.getVehicles()) {
            System.out.println(v);
        }

    }
}
