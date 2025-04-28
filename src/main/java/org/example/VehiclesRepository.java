
package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.repository.RentalRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class VehiclesRepository implements IVehicleRepository {
    private static final String pathcsv = "vehicles.csv";
    private static final String path = "src/main/resources/JSONS/vehicle.json";
    UserRepository userRepository = new UserRepository();
    RentalRepository rentalRepository = new RentalRepository();
    List<Vehicle> vehicles = new ArrayList<>();

    public VehiclesRepository() {
        load();
    }



    @Override
    public void returnVehicle(int id) {
        Vehicle vehicle = getVehicleById(id);

        if (vehicle != null && vehicle.isRented()) {
            Rental rental = rentalRepository.getRentals().stream()
                    .filter(r -> r.getVehicleId().equals(String.valueOf(id)))
                    .findFirst()
                    .orElse(null);

            if (rental != null) {
                User user = userRepository.getUserById(rental.getUserId());
                if (user != null) {
                    System.out.println("Pojazd został zwrócony przez użytkownika: " + user.getLogin());
                }
                rentalRepository.removeRental(rental.getId());
            }

            vehicle.setRented(false);
            save();
        } else {
            System.out.println("Pojazd nie jest wypożyczony.");
        }
    }

    @Override
    public List<Vehicle> getVehicles() {
        return getVehicleCopy();
    }

    @Override
    public void save() {
        try (Writer writer = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Vehicle.class, "type")
                            .registerSubtype(Car.class, "Car")
                            .registerSubtype(Motorcycle.class, "Motorcycle"))
                    .create();
            gson.toJson(vehicles, writer);
        } catch (IOException e) {
            System.out.println("Błąd zapisu do pliku JSON");
            e.printStackTrace();
        }
    }


    public void load() {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }

        try {
            String json = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);

            RuntimeTypeAdapterFactory<Vehicle> vehicleAdapterFactory = RuntimeTypeAdapterFactory
                    .of(Vehicle.class, "category")
                    .registerSubtype(Car.class, "Car")
                    .registerSubtype(Motorcycle.class, "Motorcycle");

            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(vehicleAdapterFactory)
                    .registerTypeHierarchyAdapter(Motorcycle.class, new MotorcycleDeserializer())
                    .registerTypeHierarchyAdapter(Car.class, new CarDeserializer())
                    .create();

            Type listType = new TypeToken<List<Vehicle>>() {}.getType();
            List<Vehicle> vehiclesList = gson.fromJson(json, listType);

            vehicles.clear();
            vehicles.addAll(vehiclesList);

            synchronizeRentedStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void synchronizeRentedStatus() {
        for (Vehicle vehicle : vehicles) {
            boolean isRented = rentalRepository.getRentals().stream()
                    .anyMatch(rental -> rental.getVehicleId().equals(String.valueOf(vehicle.getId())));
            vehicle.setRented(isRented);
        }
    }
    /*private void loadFromCsv() {
        File file = new File(pathcsv);
        System.out.println("Próbuję otworzyć plik: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.out.println("plik nie istnieje, tworzenie nowej listy pojazdów.");
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("Wczytano linię: " + line);

                String[] data = line.split(";");
                try {
                    Vehicle vehicle;
                    if (data[1].equals("Motorcycle")) {
                        vehicle = new Motorcycle(
                                Integer.parseInt(data[0]), data[2], data[3],
                                Integer.parseInt(data[4]), Integer.parseInt(data[5]), data[7]
                        );
                    } else if (data[1].equals("Car")) {
                        vehicle = new Car(
                                Integer.parseInt(data[0]), data[2], data[3],
                                Integer.parseInt(data[4]), Integer.parseInt(data[5])
                        );
                    } else {
                        System.out.println("Nieznany typ pojazdu: " + data[1]);
                        continue;
                    }

                    if(Boolean.parseBoolean(data[5])){ //!!!  ???
                        vehicle.rentVehicle();
                    }

                    vehicles.add(vehicle);
                    System.out.println("Dodano pojazd: " + vehicle);

                } catch (NumberFormatException e) {
                    System.out.println("błąd parsowania " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("plik nie znaleziony!");
        }
    }*/
    public List<Vehicle> getVehicleCopy() {
        List<Vehicle> copy = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (v instanceof Car) {
                copy.add(new Car(v.getId(), v.getBrand(), v.getModel(), v.getYear(), v.getPrice(),v.getPlate(),v.getAttributes()));
            } else if (v instanceof Motorcycle) {
                Motorcycle m = (Motorcycle) v;
                copy.add(new Motorcycle(m.getId(), m.getBrand(), m.getModel(), m.getYear(), m.getPrice(), m.getLicenceCategory(), m.getPlate(), m.getAttributes()));
            }
        }
        return copy;
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicle.getId() == 0) {
            vehicle.setId(generateNewId());
        }

        System.out.println("Próba dodania pojazdu: ID " + vehicle.getId() + " " + vehicle.getBrand() + " " + vehicle.getModel());
        if(!vehicles.contains(vehicle)) {
            vehicles.add(vehicle);
            save();
            System.out.println("Dodano pojazd: ID " + vehicle.getId() + " " + vehicle.getBrand() + " " + vehicle.getModel());
        } else {
            System.out.println("Pojazd o podanym ID: " + vehicle.getId() + " już istnieje!");
        }
    }
    @Override
    public void removeVehicle(int id) {
        if (vehicles.removeIf(vehicle -> vehicle.getId() == id)) {
            save();
            System.out.printf("Usunięto pojazd o ID: %d\n", id);
        } else {
            System.out.println("Nie znaleziono pojazdu o podanym ID!");
        }
    }



    public List<Vehicle> getAvailableVehicles(){
        List<Vehicle> availableVehicles = new ArrayList<>();
        for(Vehicle vehicle : vehicles){
            if(!vehicle.isRented()){
                availableVehicles.add(vehicle);
            }
        }
        return availableVehicles;
    }
    public List<Vehicle> getAdminVehicles(){
        List<Vehicle> adminVehicles = new ArrayList<>();
        for(Vehicle vehicle : vehicles){
                adminVehicles.add(vehicle);

        }
        return adminVehicles;
    }


    @Override
    public Vehicle getVehicle(Object id) {
        if (id == null) {
            return null;
        }

        if (id instanceof String) {
            try {
                int vehicleId = Integer.parseInt((String) id);
                return getVehicleById(vehicleId);
            } catch (NumberFormatException e) {
                String plate = (String) id;
                for (Vehicle vehicle : vehicles) {
                    if (plate.equalsIgnoreCase(vehicle.getPlate())) {
                        return vehicle;
                    }
                }
                return null;
            }
        }
        else if (id instanceof Integer) {
            return getVehicleById((Integer) id);
        }

        return null;
    }

    public Vehicle getVehicleById(int id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId() == id) {
                return vehicle;
            }
        }
        return null;
    }
    private int generateNewId() {
        if (vehicles.isEmpty()) {
            return 1;
        }
        return vehicles.stream().mapToInt(Vehicle::getId).max().getAsInt() + 1;
    }
}

