package org.example.service.impl;

import org.example.model.Rental;
import org.example.model.Vehicle;
import org.example.repository.RentalRepository;
import org.example.repository.VehicleRepository;
import org.example.service.RentalService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepo;
    private final VehicleRepository vehicleRepo;

    public RentalServiceImpl(RentalRepository rentalRepo,VehicleRepository vehicleRepo) {
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
    }

    @Override
    public boolean isVehicleRented(Long vehicleId) {
        Optional<Rental> rental =  rentalRepo.findActiveRentalByVehicleId(vehicleId);
        return rental.isPresent();
    }
    @Override
    public Optional<Rental> findActiveRentalByVehicleId(Long vehicleId){
        return rentalRepo.findActiveRentalByVehicleId(vehicleId);
    }
    @Override
    public List<Rental> findActiveRentalByUserId(Long userId){
        return rentalRepo.findActiveRentalByUserId(userId);
    }
    @Override
    public Rental rent(Long vehicleId, Long userId) {
        Vehicle vehicle = vehicleRepo.findById(vehicleId);
        if (vehicle == null || vehicle.isRented() || vehicle.isDeleted()){
            return null;
        }
        Rental rental = new Rental();
        rental.setVehicleId(vehicleId);
        rental.setUserId(userId);
        rental.setStartDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        rental.setEndDate(null);
        rentalRepo.save(rental);
        vehicle.setRented(true);
        vehicleRepo.save(vehicle);
        return rental;
    }
    @Override
    public Rental   returnRental(Long vehicleId, Long userId) {
        Optional<Rental> rental = rentalRepo.findActiveRentalByVehicleId(vehicleId);
        if (rental.isPresent()) {
            Rental activeRental = rental.get();
            activeRental.setEndDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            activeRental.setReturned(true);
            rentalRepo.save(activeRental);
            Vehicle vehicle = vehicleRepo.findById(vehicleId);
            if (vehicle != null) {
                vehicle.setRented(false);
                vehicleRepo.save(vehicle);
            }
            return activeRental;
        }
        return null;
    }
    @Override
    public List<Rental> findAll() {
        return rentalRepo.findAll();

    }
    @Override
    public List<Rental> allActiveRentlas() {
        return rentalRepo.findAllActiveRentals();
    }
    @Override
    public List<Rental> allRentalHistory() {
        return rentalRepo.findAllRentalsHistory();
    }
    @Override
    public List<Rental> historyByUserId(Long userId) {
        return rentalRepo.historyByUserId(userId);
    }
    @Override
    public List<Rental> historyByVehicleId(Long vehicleId) {
    return rentalRepo.findByVehicleId(vehicleId);
    }

}
