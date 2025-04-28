    package org.example;

    import java.util.UUID;

    public class Rental {
        private String id = UUID.randomUUID().toString();
        private String vehicleId;
        private String userId;
        private String rentDate;
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
    }