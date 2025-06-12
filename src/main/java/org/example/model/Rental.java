    package org.example.model;
    import jakarta.persistence.*;
    import lombok.*;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "rental")

    public class Rental {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "vehicle_id", nullable = false)
        private Long vehicleId;

        @Column(name = "user_id", nullable = false)
        private Long userId;

        @Column(name = "rent_date", nullable = false)
        private String rentDate;

        @Column(name = "return_date", nullable = false)
        private String returnDate;

        @Column(name = "returned", nullable = false)
        private boolean returned = false;

        public Rental(Long vehicleId, Long userId, String rentDate, String returnDate) {
            this.vehicleId = vehicleId;
            this.userId = userId;
            this.rentDate = rentDate;
            this.returnDate = returnDate;
        }


        public Long getId() {
            return id;
        }

        public Long getVehicleId() {
            return vehicleId;
        }

        public Long getUserId() {
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

        public boolean isReturned() {
            return returned;
        }
        public void setReturned(boolean returned) {
            this.returned = returned;
        }
    }