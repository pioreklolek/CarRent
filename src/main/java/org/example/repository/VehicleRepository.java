package org.example.repository;
import org.example.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findById(int id);
    List<Vehicle> findByBrand(String brand);
    List<Vehicle> findByModel(String model);
    Vehicle findByYear(int year);
    Vehicle findByPrice(int price);
    Vehicle findByCategory(String category);
}
