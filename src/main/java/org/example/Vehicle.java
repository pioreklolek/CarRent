    package org.example;

    import com.google.gson.annotations.SerializedName;
    import jakarta.persistence.*;
    import lombok.*;

    import java.util.Map;
    import java.util.Objects;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Entity
    @Inheritance(strategy = InheritanceType.JOINED)
    @DiscriminatorColumn(name = "type")


    public abstract class Vehicle {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String category;
        private  String brand, model;
        private int year;
        private  int price;

        @SerializedName("rented")
        private boolean rented;


        private String plate;

        @ElementCollection
        @CollectionTable(name = "vehicle_attributes", joinColumns = @JoinColumn(name = "vehicle_id"))
        @MapKeyColumn(name = "attribute_key")
        @Column(name = "attribute_value")
        private Map<String, String> attributes;

        public Vehicle(int id,String category, String brand, String model, int year, int price) {
            this.brand = brand;
            this.category = category;
            this.model = model;
            this.year = year;
            this.price = price;
            this.id = id;
        }

       /* public String toCsv() {
            return id + ";" + category + ";" + brand + ";" + model + ";" + year + ";" + price + ";" + rented;
        }*/

      /*  @Override
        public String toString() {
            return id + " " + category + " " + brand + " " + model + " " + year + " " + price + " " + (rented ? "Wypożyczony" : "Dostępny");
        }*/

        public int getId() {
            return id;
        }

        public String getCategory() {
            return category;
        }

        public String getBrand() {
            return brand;
        }

        public boolean isRented() {
            return rented;
        }

        public int getPrice() {
            return price;
        }

        public int getYear() {
            return year;
        }

        public String getModel() {
            return model;
        }

        void rentVehicle() {
            this.rented = true;
        }

        void returnVehicle() {
            this.rented = false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vehicle vehicle = (Vehicle) o;
            return id == vehicle.id &&
                    year == vehicle.year &&
                    price == vehicle.price &&
                    rented == vehicle.rented &&
                    Objects.equals(brand, vehicle.brand) &&
                    Objects.equals(model, vehicle.model) &&
                    Objects.equals(category, vehicle.category);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id,category,brand,model,year,price,rented);
        }
    }
