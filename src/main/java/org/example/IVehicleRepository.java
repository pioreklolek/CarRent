    package org.example;

    import java.util.List;

    public interface IVehicleRepository {
        void returnVehicle(int id);
        List<Vehicle> getVehicles();
        void save();
        void addVehicle(Vehicle vehicle);
        void removeVehicle(int id);
        List<Vehicle> getAvailableVehicles();
        List<Vehicle> getAdminVehicles();

        Vehicle getVehicleById(int id);

        Vehicle getVehicle(Object id);}
