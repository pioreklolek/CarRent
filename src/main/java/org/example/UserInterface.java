package org.example;

import org.example.repository.RentalRepository;

import java.util.*;

public class UserInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final IVehicleRepository repository = new VehiclesRepository();
    private final IUserRepository userRepository = new UserRepository();
    private final RentalRepository rentalRepository = new RentalRepository();

    private final AuthService authService;
    private User currentUser;

    public UserInterface() {
        this.authService = new AuthService(userRepository);
    }

    public void start() {
        if (authenticateUser()) {
            while (true) {
                if (currentUser.getRole().equals("admin")) {
                    adminMenu();
                } else {
                    userMenu();
                }
            }
        } else {
            System.out.println("Niepoprawne dane logowania!");
        }
    }

    /*public void start() {
        if (authenticateUser()) {
            while (true) {
                System.out.println("\n===== MENU WYPOŻYCZALNI =====");
                System.out.println(" Wybierz jedna z opcji:\n");

                System.out.println("1. Lista pojazdow\n");
                System.out.println("2. Wypożycz pojazd\n");
                System.out.println("3. Zwróć pojazd\n");
                if (currentUser.getRole().equals("admin")) {
                    System.out.println("4. Wyswietl liste uzytkownikow\n");
                    System.out.println("5. Wypożycz pojazd dla uzytkownika\n");
                    System.out.println("6. Dodaj pojazd\n");
                    System.out.println("7. Usun pojazd\n");
                    System.out.println("8. Dodaj uzytkownika\n");
                    System.out.println("9. Usun uzytkownika\n");
                }
                System.out.println("0. Wyjdź\n");

                if (!scanner.hasNextInt()) {
                    System.out.println("Błąd: Podaj liczbę!");
                    scanner.next();
                    continue;
                }

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> showVehicles();
                    case 2 -> rentVehicle();
                    case 3 -> returnVehicle();
                    case 4 -> showUsers();
                    case 5 -> rentVehicleForUser();
                    case 6 -> addVehicle();
                    case 7 -> removeVehicle();
                    case 8 -> {
                        System.out.println("Podaj login użytkownika: ");
                        String login = scanner.nextLine();
                        System.out.println("Podaj hasło użytkownika: ");
                        String password = scanner.nextLine();
                        System.out.println("Podaj rolę użytkownika (admin/user): ");
                        String role = scanner.nextLine();
                        User user = new User(login, password, role);
                        userRepository.addUser(user);
                    }
                    case 9 -> {
                        System.out.println("Podaj login użytkownika do usunięcia: ");
                        String login = scanner.nextLine();
                        userRepository.removeUser(login);
                    }
                    case 0 -> {
                        System.out.println("Koniec programu");
                        return;
                    }
                }
            }
        } else {
            System.out.println("Niepoprawne dane logowania!");
        }
    }*/
    private void adminMenu() {
        System.out.println("\n===== MENU ADMINA =====");
        System.out.println("Wybierz jedną z opcji:");
        System.out.println("1. Lista wszystkich pojazdów");
        System.out.println("2. Wyswietl liste użytkowników");
        System.out.println("3. Wyswietl liste wypożyczeń");
        System.out.println("4. Wypożycz pojazd dla użytkownika");
        System.out.println("5. Dodaj pojazd");
        System.out.println("6. Usun pojazd");
        System.out.println("7. Dodaj użytkownika");
        System.out.println("8. Usun użytkownika");
        System.out.println("0. Wyjdź");

        int choice = getValidChoice();
        switch (choice) {
            case 1 -> showAdminVehicles();
            case 2 -> showUsers();
            case 3 -> showRentalsAdmin();
            case 4 -> rentVehicleForUser();
            case 5 -> addVehicle();
            case 6 -> removeVehicle();
            case 7 -> addUser();
            case 8 -> removeUser();
            case 0 -> {
                System.out.println("Koniec programu");
                System.exit(0);
            }
        }
    }

    private void userMenu() {
        System.out.println("\n===== MENU UŻYTKOWNIKA =====");
        System.out.println("Wybierz jedną z opcji:");
        System.out.println("1. Lista dostepnych pojazdów");
        System.out.println("2. Wypożycz pojazd");
        System.out.println("3. Wypożyczone pojazdy");
        System.out.println("4. Zwróć pojazd");
        System.out.println("0. Wyjdź");

        int choice = getValidChoice();
        switch (choice) {
            case 1 -> showVehicles();
            case 2 -> rentVehicle();
            case 3 -> showRentals();
            case 4 -> returnVehicle();
            case 0 -> {
                System.out.println("Koniec programu");
                System.exit(0);
            }
        }
    }

    private int getValidChoice() {
        int choice = -1;
        while (choice < 0 || choice > 9) {
            if (!scanner.hasNextInt()) {
                System.out.println("Błąd: Podaj liczbę!");
                scanner.next();
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();
        }
        return choice;
    }

    private void showVehicles() {
        List<Vehicle> vehicles = repository.getAvailableVehicles();
        if (vehicles.isEmpty()) {
            System.out.println("Brak dostępnych pojazdów!\n");
        } else {
            System.out.println("LISTA POJAZDÓW:\n");
            vehicles.forEach(System.out::println);
        }
    }
    private void showAdminVehicles(){
        List<Vehicle> vehicles = repository.getAdminVehicles();
        if (vehicles.isEmpty()) {
            System.out.println("Brak dostępnych pojazdów!\n");
        } else {
            System.out.println("LISTA POJAZDÓW:\n");
            vehicles.forEach(System.out::println);
        }
    }
    private void rentVehicle() {
        System.out.println("=====DOSTĘPNE POJAZDY=====");
        showVehicles();
        System.out.print("Podaj ID pojazdu do wypożyczenia: ");
        String vehicleId = scanner.nextLine();

        Vehicle vehicle = repository.getVehicle(vehicleId);
        if (vehicle == null) {
            System.out.println("Pojazd o podanym ID nie istnieje.");
            return;
        }

        if (rentalRepository.isVehicleRented(vehicleId) || vehicle.isRented()) {
            System.out.println("Pojazd jest już wypożyczony.");
            return;
        }

        System.out.println("Podaj liczbę tygodni na jakie chcesz wypożyczyć pojazd: ");
        int weeks = scanner.nextInt();
        scanner.nextLine();

        Calendar rentCalendar = Calendar.getInstance();
        Calendar returnCalendar = Calendar.getInstance();
        returnCalendar.add(Calendar.WEEK_OF_YEAR, weeks);

        String rentDate = String.format("%tF", rentCalendar);
        String returnDate = String.format("%tF", returnCalendar);

        Rental rental = new Rental(vehicleId, currentUser.getId(), rentDate, returnDate);

        rentalRepository.addRental(rental);
        vehicle.rentVehicle();

        System.out.println("Pojazd został wypożyczony!");
        System.out.println("Data wypożyczenia: " + rentDate);
        System.out.println("Data zwrotu: " + returnDate);
    }

    private void returnVehicle() {
        System.out.println("=====TWOJE WYPOŻYCZENIA=====");
        showRentals();
        System.out.println("Podaj ID pojazdu do zwrotu: ");
        String vehicleId = scanner.nextLine();

        Rental rental = rentalRepository.getRentals().stream()
                .filter(r -> r.getVehicleId().equals(vehicleId) && r.getUserId().equals(currentUser.getId()))
                .findFirst()
                .orElse(null);

        if (rental == null) {
            System.out.println("Nie znaleziono wypożyczenia dla tego pojazdu.");
            return;
        }

        rentalRepository.removeRental(rental.getId());
        Vehicle vehicle = repository.getVehicle(vehicleId);
        if (vehicle != null) {
            vehicle.returnVehicle();
        }

        System.out.println("Zwrócono pojazd o ID: " + vehicleId);
    }

    private void showUsers() {
        if (!currentUser.getRole().equals("admin")) {
            System.out.println("Brak uprawnień do wyświetlenia listy użytkowników!");
            return;
        }
        List<User> users = userRepository.getUsers();
        if (users.isEmpty()) {
            System.out.println("Brak użytkowników!\n");
        } else {
            System.out.println("LISTA UŻYTKOWNIKÓW:\n");
            users.forEach(System.out::println);
        }
    }

    private void rentVehicleForUser() {
        if (!currentUser.isAdmin()) {
            System.out.println("Brak uprawnień do wypożyczania pojazdów dla innych użytkowników!");
            return;
        }

        System.out.println("=====DOSTĘPNE POJAZDY=====");
        showVehicles();

        System.out.print("Podaj ID pojazdu: ");
        String vehicleId = scanner.nextLine();

        Vehicle vehicle = repository.getVehicle(vehicleId);
        if (vehicle == null || vehicle.isRented()) {
            System.out.println("Pojazd o podanym ID nie istnieje lub jest już wypożyczony.");
            return;
        }

        System.out.println("=====DOSTĘPNI UŻYTKOWNICY=====");
        showUsers();

        System.out.print("Podaj login użytkownika: ");
        String login = scanner.nextLine();

        User user = userRepository.getUser(login);
        if (user == null) {
            System.out.println("Użytkownik o podanym loginie nie istnieje!");
            return;
        }

        System.out.print("Podaj liczbę tygodni na jakie chcesz wypożyczyć pojazd: ");
        int weeks = scanner.nextInt();
        scanner.nextLine();

        Calendar rentCalendar = Calendar.getInstance();
        Calendar returnCalendar = Calendar.getInstance();
        returnCalendar.add(Calendar.WEEK_OF_YEAR, weeks);

        String rentDate = String.format("%tF", rentCalendar);
        String returnDate = String.format("%tF", returnCalendar);

        Rental rental = new Rental(vehicleId, user.getId(), rentDate, returnDate);
        rentalRepository.addRental(rental);

        vehicle.rentVehicle();

        System.out.println("Pojazd został wypożyczony dla użytkownika: " + login);
        System.out.println("Data wypożyczenia: " + rentDate);
        System.out.println("Data zwrotu: " + returnDate);
    }

    private boolean authenticateUser() {
        System.out.println("Podaj login: ");
        String login = scanner.nextLine();
        System.out.println("Podaj hasło: ");
        String password = scanner.nextLine();

        User user = authService.login(login, password);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    private void removeVehicle() {
        System.out.println("=====POJAZDY W SYSTEMIE=====");
        showVehicles();
        System.out.print("Podaj ID pojazdu do usunięcia: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Vehicle vehicle = repository.getVehicle(id);
        if (vehicle == null) {
            System.out.println("Pojazd o podanym ID nie istnieje!");
            return;
        }

        repository.removeVehicle(id);
        System.out.println("Pojazd został usunięty.");
    }

    private void addVehicle() {
        if (!currentUser.isAdmin()) {
            System.out.println("Brak uprawnień do dodawania pojazdów!");
            return;
        }

        System.out.println("Podaj markę pojazdu: ");
        String brand = scanner.nextLine();

        System.out.println("Podaj model pojazdu: ");
        String model = scanner.nextLine();

        System.out.println("Podaj rok produkcji pojazdu: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Podaj typ pojazdu (Car/Motorcycle): ");
        String type = scanner.nextLine();

        System.out.println("Podaj cenę pojazdu: ");
        int price = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Podaj numer rejestracyjny pojazdu: ");
        String plate = scanner.nextLine();

        Map<String, String> attributes = new HashMap<>();

        Vehicle vehicle = null;

        if (type.equalsIgnoreCase("Car")) {
            vehicle = new Car(0, brand, model, year, price, plate, attributes);
        } else if (type.equalsIgnoreCase("Motorcycle")) {
            System.out.println("Podaj kategorię motocykla (np. Sport, Cruiser): ");
            String category = scanner.nextLine();

            attributes.put("category", category);

            vehicle = new Motorcycle(0, brand, model, year, price, category, plate, attributes);
        } else {
            System.out.println("Niepoprawny typ pojazdu!");
            return;
        }

        repository.addVehicle(vehicle);
        System.out.println("Pojazd został dodany.");
        showVehicles();
    }

    private void showRentalsAdmin() {
        List<Rental> rentals = rentalRepository.getRentals();
        if (rentals.isEmpty()) {
            System.out.println("Brak wypożyczeń!");
        } else {
            System.out.println("=====LISTA WYPOŻYCZEŃ=====\n");
            rentals.forEach(rental -> {
                User user = userRepository.getUserById(rental.getUserId());
                String login = (user != null) ? user.getLogin() : "Nieznany użytkownik";

                System.out.println("Wypożyczenie ID: " + rental.getId());
                System.out.println("Pojazd ID: " + rental.getVehicleId());
                System.out.println("Użytkownik: " + login);
                System.out.println("Data wypożyczenia: " + rental.getRentDate());
                System.out.println("Data zwrotu: " + rental.getReturnDate());
                System.out.println("----------------------------");
            });
        }
    }
    private void showRentals(){
        List<Rental> userRentals = rentalRepository.getRentals().stream()
                .filter(rental -> rental.getUserId().equals(currentUser.getId()))
                .toList();

        if (userRentals.isEmpty()) {
            System.out.println("Nie masz aktualnie wypożyczonych pojazdów.");
            return;
        }

        System.out.println("==TWOJE WYNAJĘTE POJAZDY==");
        for (Rental rental : userRentals) {
            Vehicle vehicle = repository.getVehicle(rental.getVehicleId());
            if (vehicle != null) {
                System.out.println(vehicle);
                System.out.println("Data wypożyczenia: " + rental.getRentDate());
                System.out.println("Data zwrotu: " + rental.getReturnDate());
                System.out.println("----------------------------");
            }
        }
    }

    private void addUser() {
        if (!currentUser.isAdmin()) {
            System.out.println("Brak uprawnień do dodawania użytkowników!");
            return;
        }

        System.out.println("Podaj login użytkownika: ");
        String login = scanner.nextLine();

        if (userRepository.getUser(login) != null) {
            System.out.println("Użytkownik o takim loginie już istnieje!");
            return;
        }

        System.out.println("Podaj hasło użytkownika: ");
        String password = scanner.nextLine();

        System.out.println("Podaj rolę użytkownika (admin/user): ");
        String role = scanner.nextLine();
        if (!role.equalsIgnoreCase("admin") && !role.equalsIgnoreCase("user")) {
            System.out.println("Niepoprawna rola użytkownika!");
            return;
        }

        User user = new User(login, password, role);
        userRepository.addUser(user);
        System.out.println("Użytkownik został dodany.");
    }
    private void removeUser(){
        if (!currentUser.isAdmin()) {
            System.out.println("Brak uprawnień do usuwania użytkowników!");
            return;
        }
        System.out.println("=====LISTA UŻYTKOWNIKÓW=====");
        showUsers();
        System.out.print("Podaj login użytkownika do usunięcia: ");
        String login = scanner.nextLine();

        User user = userRepository.getUser(login);
        if (user == null) {
            System.out.println("Użytkownik o podanym loginie nie istnieje!");
            return;
        }

        userRepository.removeUser(login);
        System.out.println("Użytkownik został usunięty.");
    }
}