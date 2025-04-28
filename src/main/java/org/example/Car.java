package org.example;

import lombok.*;

import java.util.Map;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class Car extends Vehicle {

    public Car(int id, String brand, String model, int year, int price, String plate, Map<String, String> attributes) {
        super(id, "Car", brand, model, year, price);
        setPlate(plate);
        setAttributes(attributes);
    }

    @Override
    public String toString() {
        return getId() + " " + getCategory() + " " + getBrand() + " " + getModel() + " " +
                getYear() + " " + getPlate() + " " +
                getPrice() + " " +
                (isRented() ? "Wypożyczony" : "Dostępny");
    }
}
