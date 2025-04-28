package org.example.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.Rental;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RentalRepository {

    private static final String RENTALS_JSON_PATH = "src/main/resources/JSONS/rentals.json";

    private List<Rental> rentals = new ArrayList<>();

    public RentalRepository() {
        loadFromFile();
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void addRental(Rental rental) {
        rentals.add(rental);
        saveToFile();
    }

    public void removeRental(String rentalId) {
        rentals.removeIf(rental -> rental.getId().equals(rentalId));
        saveToFile();
    }

    private void loadFromFile() {
        File file = new File(RENTALS_JSON_PATH);
        if (!file.exists()) return;
        try {
            String json = Files.readString(file.toPath());
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Rental>>() {}.getType();
            rentals = gson.fromJson(json, listType);
            if (rentals == null) {
                rentals = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            rentals = new ArrayList<>();
        }
    }

    public void saveToFile() {
        try (Writer writer = new FileWriter(RENTALS_JSON_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(rentals, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isVehicleRented(String vehicleId) {
        Calendar currentDate = Calendar.getInstance();
        for (Rental rental : rentals) {
            if (rental.getVehicleId().equals(vehicleId)) {
                try {
                    Date returnDate = new SimpleDateFormat("yyyy-MM-dd").parse(rental.getReturnDate());
                    if (currentDate.getTime().before(returnDate)) {
                        return true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}