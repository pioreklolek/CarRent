package org.example.repository.impl;

import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.Vehicle;
import org.example.repository.VehicleRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class VehicleRepositoryImpl implements VehicleRepository {
    private final Session session;

    public VehicleRepositoryImpl(Session session) {
        this.session = session;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        session.beginTransaction();
        session.saveOrUpdate(vehicle);
        session.getTransaction().commit();
        return vehicle;
    }


    @Override
    public void delete(Vehicle vehicle) {
        session.beginTransaction();
        vehicle.setDeleted(true);
        session.update(vehicle);
        session.getTransaction().commit();
    }
    @Override
    public void deleteById(Long vehicleId) {
        Vehicle vehicle = findById(vehicleId);
        if (vehicle != null) {
            session.beginTransaction();
            vehicle.setDeleted(true);
            session.update(vehicle);
            session.getTransaction().commit();
        }
    }

    @Override
    public Vehicle findById(Long id) {
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
    @Override
    public List<Vehicle> getAvailabeVehicles() {
        return session.createQuery("FROM Vehicle WHERE rented = false AND deleted = false", Vehicle.class).getResultList();
    }
    @Override
    public List<Vehicle> findByRentedTrue() {
        List<Vehicle> result = new ArrayList<>();
        result.addAll(session.createQuery("FROM Vehicle WHERE rented = true", Vehicle.class).getResultList());
        return result;
    }
    public List<Car> findAllCars() {
        return session.createQuery("FROM Car", Car.class).getResultList();
    }
    public List<Motorcycle> findAllMotorcycles() {
        return session.createQuery("FROM Motorcycle", Motorcycle.class).getResultList();
    }
    @Override
    public List<Vehicle> findAllActive() {
        return session.createQuery("FROM Vehicle WHERE deleted = false", Vehicle.class).getResultList();
    }
    @Override
    public List<Vehicle> findByDeletedTrue() {
        return session.createQuery("FROM Vehicle WHERE deleted = true", Vehicle.class).getResultList();
    }
}