package org.example.api.controller;

import org.example.model.Rental;
import org.example.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(rentalService.allActiveRentlas());
    }
    @GetMapping("/{vehicleId}/status")
    public ResponseEntity<Boolean> isVehicleRented(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(rentalService.isVehicleRented(vehicleId));     // do poprawy
    }
    @GetMapping("/history/user/{userId}")
    public ResponseEntity<List<Rental>> getRentalHistoryByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(rentalService.historyByUserId(userId));
    }
    @GetMapping("/history/vehicle/{vehicleId}")
    public ResponseEntity<List<Rental>> getRentalHistoryByVehicleId(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(rentalService.historyByVehicleId(vehicleId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<Rental>> getAllRentalHistory() {
        return ResponseEntity.ok(rentalService.allRentalHistory());
    }
    @PostMapping("/rent/{vehicleId}/{userId}")
    public ResponseEntity<Rental> rentVehicle(
            @PathVariable Long vehicleId,
            @PathVariable Long userId) {
        return Optional.ofNullable(rentalService.rent(vehicleId, userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
    @PostMapping("/return/{vehicleId}/{userId}")
    public ResponseEntity<Rental> returnVehicle(
            @PathVariable Long vehicleId,
            @PathVariable Long userId) {
        return Optional.ofNullable(rentalService.returnRental(vehicleId, userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
