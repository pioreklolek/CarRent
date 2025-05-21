package org.example.repository.impl;

import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.Vehicle;
import org.example.repository.VehicleRepository;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class VehicleRepositoryImpl implements VehicleRepository {
    private final Session session;

    public VehicleRepositoryImpl(Session session) {
        this.session = session;
    }

    @Override
    public void save(Vehicle vehicle) {
        session.beginTransaction();
        session.saveOrUpdate(vehicle);
        session.getTransaction().commit();
    }


    @Override
    public void delete(Vehicle vehicle) {
        session.beginTransaction();
        session.delete(vehicle);
        session.getTransaction().commit();
    }
    public void deleteById(String id) {
        Vehicle vehicle = findById(id);
        if (vehicle != null) {
            delete(vehicle);
        }
    }

    @Override
    public Vehicle findById(String id) {
        return session.get(Vehicle.class, id);
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> result = session.createQuery("FROM Vehicle", Vehicle.class).getResultList();
        return result;
    }

    @Override
    public List<Vehicle> findByRentedFalse() {
        List<Vehicle> result = new ArrayList<>();
        result.addAll(session.createQuery("FROM Vehicle WHERE rented = false", Vehicle.class).getResultList());
        return result;
    }
    public List<Car> findAllCars() {
        return session.createQuery("FROM Car", Car.class).getResultList();
    }
    public List<Motorcycle> findAllMotorcycles() {
        return session.createQuery("FROM Motorcycle", Motorcycle.class).getResultList();
    }
}