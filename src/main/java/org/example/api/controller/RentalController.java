package org.example.api.controller;

import org.springframework.security.core.Authentication;import org.example.model.Rental;
import org.example.model.User;
import org.example.service.RentalService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;
    private final UserService userService;

    public RentalController(RentalService rentalService, UserService userService) {
        this.rentalService = rentalService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(rentalService.allActiveRentlas());
    }

    @GetMapping("/{vehicleId}/status")
    @PreAuthorize("hasAuthority('admin')")

    public ResponseEntity<Boolean> isVehicleRented(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(rentalService.isVehicleRented(vehicleId));     // do poprawy
    }

    @GetMapping("/history/user/{userId}")
    @PreAuthorize("hasAuthority('admin') or @userServiceImpl.findById(#userId).orElse(new org.example.model.User()).login == authentication.name")

    public ResponseEntity<List<Rental>> getRentalHistoryByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(rentalService.historyByUserId(userId));
    }

    @GetMapping("/history/vehicle/{vehicleId}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<Rental>> getRentalHistoryByVehicleId(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(rentalService.historyByVehicleId(vehicleId));
    }

    @GetMapping("/history")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<Rental>> getAllRentalHistory() {
        return ResponseEntity.ok(rentalService.allRentalHistory());
    }
    @GetMapping("/history/my")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<List<Rental>> getMyRentalHistory(Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(rentalService.historyByUserId(user.getId()));
    }
    @GetMapping("active/my")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<?> getMyActiveRentals(Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        if (user == null) {
            return ResponseEntity.badRequest().body("Nieprawidłowy użytkownik");
        }

        List<Rental> activeRentals = rentalService.findActiveRentalByUserId(user.getId());

        if (activeRentals.isEmpty()) {
            return ResponseEntity.status(204).body("Brak aktywnych wypożyczeń.");
        }
        return ResponseEntity.ok(activeRentals);
    }

    @PostMapping("/rent/{vehicleId}")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Rental> rentVehicle(@PathVariable Long vehicleId,Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return Optional.ofNullable(rentalService.rent(vehicleId, user.getId())).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }
    @PostMapping("/return/{vehicleId}")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<Rental> returnVehicle(@PathVariable Long vehicleId, Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        if(user == null) {
            return ResponseEntity.badRequest().build();
        }
        return Optional.ofNullable((rentalService.returnRental(vehicleId, user.getId()))).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/rent/{vehicleId}/{userId}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Rental> rentVehicleForUser(
            @PathVariable Long vehicleId,
            @PathVariable Long userId) {
        return Optional.ofNullable(rentalService.rent(vehicleId, userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
    @PostMapping("/return/{vehicleId}/{userId}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Rental> returnVehicleForUser(
            @PathVariable Long vehicleId,
            @PathVariable Long userId) {
        return Optional.ofNullable(rentalService.returnRental(vehicleId, userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
