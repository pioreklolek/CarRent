package org.example.repository;

import org.example.model.Vehicle;

import java.util.List;

public interface VehicleRepository {
    void save(Vehicle vehicle);
    void delete(Vehicle vehicle);
    Vehicle findById(String id);
    List<Vehicle> findAll();
    List<Vehicle> findByRentedFalse();
    void deleteById(String id);

    }