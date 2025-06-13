package org.example;

import org.example.hibernate.HibernateUtil;
import org.example.repository.RentalRepository;
import org.example.repository.UserRepository;
import org.example.repository.VehicleRepository;
import org.example.repository.impl.RentalRepositoryImpl;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.repository.impl.VehicleRepositoryImpl;
import org.example.service.AuthService;
import org.example.service.RentalService;
import org.example.service.UserService;
import org.example.service.VehicleService;
import org.example.service.impl.AuthServiceImpl;
import org.example.service.impl.RentalServiceImpl;
import org.example.service.impl.UserServiceImpl;
import org.example.service.impl.VehicleServiceImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class VehicleRentalCliApp {

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        UserRepository userRepository = new UserRepositoryImpl(session);
        VehicleRepository vehicleRepository = new VehicleRepositoryImpl(session);
        RentalRepository rentalRepository = new RentalRepositoryImpl(session);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        AuthService authService = new AuthServiceImpl(userRepository);
        UserService userService = new UserServiceImpl(userRepository,passwordEncoder);
        VehicleService vehicleService = new VehicleServiceImpl(vehicleRepository);
        RentalService rentalService = new RentalServiceImpl(rentalRepository, vehicleRepository);

        UserInterface ui = new UserInterface(
                vehicleRepository,
                userRepository,
                rentalRepository,
                authService,
                userService,
                rentalService
        );

        ui.start();

        session.close();
        sessionFactory.close();
    }
}