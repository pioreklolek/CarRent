package org.example;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Table(name = "vehicle")
public abstract class Vehicle {

    @Id
    private String id;

    private String category;
    private String brand;
    private String model;
    private int year;
    private int price;

    private boolean rented;

    @Column(nullable = false, unique = true)
    private String plate;

    @ElementCollection
    @CollectionTable(name = "vehicle_attributes", joinColumns = @JoinColumn(name = "vehicle_id"))
    @MapKeyColumn(name = "attribute_key")
    @Column(name = "attribute_value")
    private Map<String, String> attributes;

    public Vehicle(String category, String brand, String model, int year, int price, String plate, Map<String, String> attributes) {
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.plate = plate;
        this.attributes = attributes != null ? attributes : new HashMap<>();
        this.rented = false;
    }

    public void rentVehicle() {
        this.rented = true;
    }

    public void returnVehicle() {
        this.rented = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) o;
        return year == vehicle.year &&
                price == vehicle.price &&
                rented == vehicle.rented &&
                Objects.equals(id, vehicle.id) &&
                Objects.equals(brand, vehicle.brand) &&
                Objects.equals(model, vehicle.model) &&
                Objects.equals(category, vehicle.category) &&
                Objects.equals(plate, vehicle.plate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, brand, model, year, price, rented, plate);
    }
}