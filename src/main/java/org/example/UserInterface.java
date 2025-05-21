package org.example;
import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.User;
import org.example.model.Vehicle;
import org.example.repository.*;
import org.example.service.*;

import java.util.*;

public class UserInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final VehicleRepository vehicleRepo;
    private final UserRepository userRepo;
    private final RentalRepository rentalRepo;
    private final AuthService authService;
    private final UserService userService;
    private final RentalService rentalService;

    private User currentUser;

    public UserInterface(VehicleRepository vehicleRepo,
                         UserRepository userRepo,
                         RentalRepository rentalRepo,
                         AuthService authService,
                         UserService userService,
                         RentalService rentalService) {
        this.vehicleRepo = vehicleRepo;
        this.userRepo = userRepo;
        this.rentalRepo = rentalRepo;
        this.authService = authService;
        this.userService = userService;
        this.rentalService = rentalService;
    }

    public void start() {
        if (authenticateUser()) {
            while (true) {
                try {
                    if (currentUser.isAdmin()) adminMenu();
                    else userMenu();
                } catch (Exception e) {
                    System.err.println("Błąd: " + e.getMessage());
                    scanner.nextLine(); // wyczyść bufor
                }
            }
        } else {
            System.out.println("Niepoprawne dane logowania!");
        }
    }

    private boolean authenticateUser() {
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Hasło: ");
        String password = scanner.nextLine();
        User user = authService.login(login, password);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    private void adminMenu() {
        System.out.println("\n===== MENU ADMINA =====");
        System.out.println("""
                1. Lista pojazdów
                2. Lista użytkowników
                3. Lista wypożyczeń
                4. Wypożycz pojazd dla użytkownika
                5. Dodaj pojazd
                6. Usuń pojazd
                7. Dodaj użytkownika
                8. Usuń użytkownika
                0. Wyjdź
                """);
        int choice = getValidChoice();
        switch (choice) {
            case 1 -> showAllVehicles();
            case 2 -> showUsers();
            case 3 -> showRentals();
            case 4 -> rentVehicleForUser();
            case 5 -> addVehicle();
            case 6 -> removeVehicle();
            case 7 -> addUser();
            case 8 -> removeUser();
            case 0 -> System.exit(0);
        }
    }

    private void userMenu() {
        System.out.println("\n===== MENU UŻYTKOWNIKA =====");
        System.out.println("""
                1. Lista dostępnych pojazdów
                2. Wypożycz pojazd
                3. Moje wypożyczenia
                4. Zwróć pojazd
                0. Wyjdź
                """);
        int choice = getValidChoice();
        switch (choice) {
            case 1 -> showAvailableVehicles();
            case 2 -> rentVehicle();
            case 3 -> showUserRentals();
            case 4 -> returnVehicle();
            case 0 -> System.exit(0);
        }
    }

    private int getValidChoice() {
        while (true) {
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Błąd: Podaj liczbę!");
            }
        }
    }

    private void showAvailableVehicles() {
        List<Vehicle> availableVehicles = vehicleRepo.findByRentedFalse();

        if (availableVehicles.isEmpty()) {
            System.out.println("Brak dostępnych pojazdów.");
            return;
        }
        availableVehicles.sort(Comparator.comparingInt((v -> Integer.parseInt(v.getId()))));
        System.out.println("\n===== DOSTĘPNE POJAZDY =====");
        System.out.printf("%-5s %-10s %-15s %-20s %-10s %-15s %-20s %-20s %-5s \n",
                "ID", "Typ", "Marka", "Model", "Rok"," Rejestracja"," Kategoria Licencji"," Atrybuty", "Cena" );

        System.out.println("==============================================================================================================================");

        for (Vehicle v : availableVehicles) {
            String type = (v.getCategory().equals("Car")) ? "Samochód" :
                    (v.getCategory().equals("Motorcycle")) ? "Motocykl" : "Inne";

            String licenseCategory = "B";

            if (v instanceof Motorcycle) {
                Motorcycle motorcycle = (Motorcycle) v;
                licenseCategory = motorcycle.getLicenceCategory();
            }

            String attributes = (v.getAttributes() != null) ? v.getAttributes().toString() : "Brak atrybutów";

            System.out.printf("%-5s %-10s %-15s %-20s %-10s %-15s %-20s %-20s %-5s\n",
                    v.getId(),
                    type,
                    v.getBrand(),
                    v.getModel(),
                    v.getYear(),
                    v.getPlate(),
                    licenseCategory,
                    v.getAttributes(),
                    v.getPrice());
        }
    }
    private void showAllVehicles() {
    List<Vehicle> allVehicles  = vehicleRepo.findAll();
    if (allVehicles.isEmpty()){
        System.out.println("Brak pojazdów w bazie.");
        return;
        }
        allVehicles.sort(Comparator.comparingInt((v -> Integer.parseInt(v.getId()))));

        System.out.println("\n===== WSZYSTKIE POJAZDY =====");
        System.out.printf("%-5s %-10s %-15s %-20s %-10s %-15s %-20s %-20s %-5s \n",
                "ID", "Typ", "Marka", "Model", "Rok"," Rejestracja"," Kategoria Licencji"," Atrybuty", "Cena" );
        System.out.println("==============================================================================================================================");

        for (Vehicle v : allVehicles) {
            String type = (v.getCategory().equals("Car")) ? "Samochód" :
                    (v.getCategory().equals("Motorcycle")) ? "Motocykl" : "Inne";

            String licenseCategory = "B";

            if (v instanceof Motorcycle) {
                Motorcycle motorcycle = (Motorcycle) v;
                licenseCategory = motorcycle.getLicenceCategory();
            }

            String attributes = (v.getAttributes() != null) ? v.getAttributes().toString() : "Brak atrybutów";

            String rented = v.isRented() ? "Wypożyczony" : "Dostępny";

            System.out.printf("%-5s %-10s %-15s %-20s %-10s %-15s %-20s %-20s %-5s %-10s \n",
                    v.getId(),
                    type,
                    v.getBrand(),
                    v.getModel(),
                    v.getYear(),
                    v.getPlate(),
                    licenseCategory,
                    v.getAttributes(),
                    v.getPrice(),
                    rented);
        }
    }

    private void showUsers() {
        List<User> users = userRepo.findAll();
        if (users.isEmpty()) {
            System.out.println("Brak użytkowników w bazie.");
            return;
        }
        System.out.println("\n===== WSZYSCY UŻYTKOWNICY =====");
        System.out.printf("%-5s %-10s %-15s %-20s \n", "ID", "Login", "Hasło", "Rola");
        System.out.println("=========================================================");
        for (User user : users) {
            System.out.printf("%-5s %-10s %-15s %-20s \n",
                    user.getId(),
                    user.getLogin(),
                    user.getPassword(),
                    user.isAdmin() ? "Admin" : "Użytkownik");
        }
    }

    private void showRentals() {
        rentalRepo.findAll().forEach(System.out::println);
    }

    private void showUserRentals() {
        rentalRepo.findAll().stream()
                .filter(r -> r.getUserId().equals(currentUser.getId()))
                .forEach(rental -> {
                    Vehicle vehicle = vehicleRepo.findById(rental.getVehicleId());
                    if (vehicle != null) {
                        System.out.println("ID: " + vehicle.getId() +
                                ", Pojazd: " + vehicle.getBrand() + " " + vehicle.getModel() +
                                ", Data wypożyczenia: " + rental.getRentDate() +
                                ", Data zwrotu: " + rental.getReturnDate());
                    }
                });
    }

    private void rentVehicle() {
        System.out.print("Podaj ID pojazdu: ");
        String vehicleId = scanner.nextLine();
        Vehicle vehicle = vehicleRepo.findById(vehicleId);
        if (vehicle != null) {
            if (vehicle.isRented()) {
                System.out.println("Pojazd już wypożyczony.");
                return;
            }
            rentalService.rentVehicle(currentUser, vehicle);
            System.out.println("Wypożyczono pojazd: " + vehicle.getBrand());
        } else {
            System.out.println("Nie znaleziono pojazdu.");
        }
    }

    private void rentVehicleForUser() {
        System.out.print("Login użytkownika: ");
        String login = scanner.nextLine();
        User user = userRepo.findByLogin(login);
        if (user == null) {
            System.out.println("Użytkownik nie istnieje.");
            return;
        }
        System.out.print("ID pojazdu: ");
        String vehicleId = scanner.nextLine();
        Vehicle vehicle = vehicleRepo.findById(vehicleId);
        if (vehicle != null) {
            if (!vehicle.isRented()) {
                rentalService.rentVehicle(user, vehicle);
                System.out.println("Wypożyczono pojazd dla użytkownika.");
            } else {
                System.out.println("Pojazd już wypożyczony.");
            }
        } else {
            System.out.println("Nie znaleziono pojazdu.");
        }
    }

    private void returnVehicle() {
        System.out.print("ID pojazdu do zwrotu: ");
        String vehicleId = scanner.nextLine();
        rentalService.returnVehicle(currentUser.getId(), vehicleId);
    }

    private void addUser() {
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Hasło: ");
        String password = scanner.nextLine();
        System.out.print("Rola (admin/user): ");
        String role = scanner.nextLine();
        authService.register(login, password, role);
    }

    private void removeUser() {
        System.out.print("Login do usunięcia: ");
        String login = scanner.nextLine();
        userService.deleteUserByLogin(login);
    }

    private void removeVehicle() {
        System.out.print("ID pojazdu do usunięcia: ");
        String vehicleId = scanner.nextLine();
        vehicleRepo.deleteById(vehicleId);
    }

    private void addVehicle() {
        System.out.print("Typ pojazdu (Car/Motorcycle): ");
        String type = scanner.nextLine();

        System.out.print("Marka: ");
        String brand = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Rok: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Cena: ");
        int price = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Tablica: ");
        String plate = scanner.nextLine();

        Vehicle vehicle;
        if (type.equalsIgnoreCase("Motorcycle")) {
            System.out.print("Kategoria prawa jazdy: ");
            String cat = scanner.nextLine();
            vehicle = new Motorcycle(brand, model, year, price, cat, plate, new HashMap<>());
        } else if (type.equalsIgnoreCase("Car")) {
            vehicle = new Car(brand, model, year, price, plate, new HashMap<>());
        } else {
            System.out.println("Nieprawidłowy typ pojazdu.");
            return;
        }

        vehicleRepo.save(vehicle);
        System.out.println("Pojazd dodany do bazy.");
    }
}