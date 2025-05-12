package org.example.service;

import org.example.Rental;
import org.example.User;
import org.example.Vehicle;
import org.example.repository.RentalRepository;
import org.example.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class RentalService {
    private final RentalRepository repo;
    private final VehicleRepository vehicleRepo;

    public RentalService(RentalRepository repo, VehicleRepository vehicleRepo) {
        this.repo = repo;
        this.vehicleRepo = vehicleRepo;
    }

    public List<Rental> getAll() {
        return repo.findAll();
    }

    public void add(Rental r) {
        repo.save(r);
    }

    public void remove(String id) {
        repo.deleteById(id);
    }

    public List<Rental> getByVehicle(String vehicleId) {
        return repo.findByVehicleId(vehicleId);
    }

    public List<Rental> getByUser(String userId) {
        return repo.findByUserId(userId);
    }

    public void rentVehicle(User currentUser, Vehicle vehicle) {
        Rental rental = new Rental();

        rental.setId(UUID.randomUUID().toString());
        rental.setUser(currentUser);
        rental.setVehicle(vehicle);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusWeeks(2);


        rental.setStartDate(today.format(formatter));
        rental.setEndDate(endDate.format(formatter));


        add(rental);

        vehicle.setRented(true);
        repo.save(rental);
        vehicleRepo.save(vehicle);
    }

    public void returnVehicle(String userId, String vehicleId) {
        List<Rental> rentals = repo.findByUserId(userId).stream()
                .filter(r -> r.getVehicleId().equals(vehicleId))
                .toList();

        if (rentals.isEmpty()) {
            throw new RuntimeException("Nie znaleziono wypożyczenia dla tego pojazdu.");
        }

        Rental rental = rentals.get(rentals.size() - 1);

        repo.delete(rental);

        Vehicle vehicle = vehicleRepo.findById(vehicleId).orElseThrow(() -> new RuntimeException("Pojazd nie znaleziony"));
        vehicle.setRented(false);

        vehicleRepo.save(vehicle);
    }
}