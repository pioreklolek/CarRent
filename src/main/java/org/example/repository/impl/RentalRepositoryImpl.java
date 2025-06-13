package org.example.repository.impl;

import org.example.model.Rental;
import org.example.repository.RentalRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

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

    public void deleteById(Long id) {
        Rental rental = findById(id);
        if (rental != null) {
            delete(rental);
        }
    }

    public Rental findById(Long id) {
        return session.get(Rental.class, id);
    }

    @Override
    public List<Rental> findAll() {
        return session.createQuery("FROM Rental", Rental.class).list();
    }

    @Override
    public List<Rental> findByUserId(Long userId) {
        return session.createQuery("FROM Rental WHERE userId = :userId", Rental.class)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public List<Rental> findByVehicleId(Long vehicleId) {
        return session.createQuery("FROM Rental WHERE vehicleId = :vehicleId", Rental.class)
                .setParameter("vehicleId", vehicleId)
                .list();
    }
    @Override
    public Optional<Rental> findActiveRentalByVehicleId(Long vehicleId) {
        return Optional.ofNullable(session.createQuery("FROM Rental WHERE vehicleId = :vehicleId AND returned = false", Rental.class)
                .setParameter("vehicleId", vehicleId)
                .uniqueResult());
    }

    @Override
    public List<Rental> findActiveRentalByUserId(Long userId) {
        return session.createQuery(
                        "FROM Rental WHERE userId = :userId AND returned = false", Rental.class)
                .setParameter("userId", userId)
                .getResultList();
    }
    @Override
    public List<Rental> findAllActiveRentals() {
        return session.createQuery("FROM Rental WHERE returned IS FALSE", Rental.class).list();
    }
    @Override
    public List<Rental> findAllRentalsHistory() {
        return session.createQuery("FROM Rental WHERE returned IS TRUE", Rental.class).list();
    }
    @Override
    public List<Rental> historyByUserId(Long userId){
        return session.createQuery(
                        "FROM Rental WHERE userId = :userId AND returned = true",
                        Rental.class)
                .setParameter("userId", userId)
                .list();
    }
}