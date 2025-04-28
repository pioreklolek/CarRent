package org.example;

import lombok.*;

import java.util.Map;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class Motorcycle extends Vehicle {

    private String licenceCategory;

    public Motorcycle(int id, String brand, String model, int year, int price, String licenceCategory, String plate, Map<String, String> attributes) {
        super(id, "Motorcycle", brand, model, year, price);
        this.licenceCategory = licenceCategory;
        setPlate(plate);
        setAttributes(attributes);
    }

    public Motorcycle(int id, String brand, String model, int year, int price) {
        super(id, "Motorcycle", brand, model, year, price);
    }

    @Override
    public String toString() {
        return getId() + " " + getCategory() + " " + getBrand() + " " + getModel() + " " +
                getYear() + " "  + getPlate() + " " +
                (licenceCategory != null ? licenceCategory + " " : "") +
                getPrice() + " " +
                (isRented() ? "Wypożyczony" : "Dostępny");
    }

  /*  @Override
    public String toCsv() {
        return super.toCsv() + ";" + licenceCategory;
    }*/

    public String getLicenceCategory() {
        return licenceCategory;
    }
}
