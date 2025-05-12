    package org.example;
    import jakarta.persistence.*;
    import lombok.*;


    import java.util.UUID;
    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "rental")

    public class Rental {
        @Id
        private String id;

        @Column(name = "vehicle_id", nullable = false)
        private String vehicleId;

        @Column(name = "user_id", nullable = false)
        private String userId;

        @Column(name = "rent_date", nullable = false)
        private String rentDate;

        @Column(name = "return_date", nullable = false)
        private String returnDate;

        public Rental(String vehicleId, String userId, String rentDate, String returnDate) {
            this.vehicleId = vehicleId;
            this.userId = userId;
            this.rentDate = rentDate;
            this.returnDate = returnDate;
        }


        public String getId() {
            return id;
        }

        public String getVehicleId() {
            return vehicleId;
        }

        public String getUserId() {
            return userId;
        }

        public String getRentDate() {
            return rentDate;
        }

        public String getReturnDate() {
            return returnDate;
        }

        public void setUser(User currentUser) {
            this.userId = currentUser.getId();
        }

        public void setVehicle(Vehicle vehicle) {
            this.vehicleId = vehicle.getId();
        }

        public void setStartDate(String l) {
            this.rentDate = String.valueOf(l);
        }

        public void setEndDate(String l) {
            this.returnDate = String.valueOf(l);
        }
    }