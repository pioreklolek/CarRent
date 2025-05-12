package org.example.repository;

import org.example.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, String> {
    List<Rental> findByUserId(String userId);
    List<Rental> findByVehicleId(String vehicleId);
}