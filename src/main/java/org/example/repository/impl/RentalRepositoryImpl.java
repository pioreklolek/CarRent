package org.example.repository.impl;

import org.example.model.Rental;
import org.example.repository.RentalRepository;
import org.hibernate.Session;

import java.util.List;

public class RentalRepositoryImpl implements RentalRepository {
    private final Session session;

    public RentalRepositoryImpl(Session session) {
        this.session = session;
    }

    @Override
    public void save(Rental rental) {
        session.beginTransaction();
        session.saveOrUpdate(rental);
        session.getTransaction().commit();
    }

    @Override
    public void delete(Rental rental) {
        session.beginTransaction();
        session.delete(rental);
        session.getTransaction().commit();
    }

    public void deleteById(String id) {
        Rental rental = findById(id);
        if (rental != null) {
            delete(rental);
        }
    }

    public Rental findById(String id) {
        return session.get(Rental.class, id);
    }

    @Override
    public List<Rental> findAll() {
        return session.createQuery("FROM Rental", Rental.class).list();
    }

    @Override
    public List<Rental> findByUserId(String userId) {
        return session.createQuery("FROM Rental WHERE userId = :userId", Rental.class)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public List<Rental> findByVehicleId(String vehicleId) {
        return session.createQuery("FROM Rental WHERE vehicleId = :vehicleId", Rental.class)
                .setParameter("vehicleId", vehicleId)
                .list();
    }
}