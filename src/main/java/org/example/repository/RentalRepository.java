package org.example.repository;

import org.example.model.Rental;

import java.util.List;

public interface RentalRepository{
    void save(Rental rental);
    void delete(Rental rental);
    List<Rental> findByUserId(String userId);
    List<Rental> findByVehicleId(String vehicleId);
    List<Rental> findAll();

    void deleteById(String id);
}