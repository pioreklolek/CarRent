package org.example.service;

import org.example.Vehicle;
import org.example.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository repo;

    public VehicleService(VehicleRepository repo) {
        this.repo = repo;
    }

    public List<Vehicle> getAll() {
        return repo.findAll();
    }

    public List<Vehicle> getAvailable() {
        return repo.findByRentedFalse();
    }

    public Vehicle get(String id) {
        return repo.findById(id).orElse(null);
    }

    public void add(Vehicle v) {
        repo.save(v);
    }

    public void remove(String id) {
        repo.deleteById(id);
    }
}