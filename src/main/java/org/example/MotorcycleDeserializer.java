package org.example;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MotorcycleDeserializer implements JsonDeserializer<Motorcycle> {
    @Override
    public Motorcycle deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        int id = Integer.parseInt(obj.get("id").getAsString());
        String brand = obj.get("brand").getAsString();
        String model = obj.get("model").getAsString();
        int year = obj.get("year").getAsInt();
        String plate = obj.has("plate") ? obj.get("plate").getAsString() : "";
        int price = obj.has("price") ? obj.get("price").getAsInt() : 0;

        String licenceCategory = "";
        Map<String, String> attributes = new HashMap<>();
        if (obj.has("attributes")) {
            JsonObject attributesObj = obj.getAsJsonObject("attributes");
            if (attributesObj.has("licence_category")) {
                licenceCategory = attributesObj.get("licence_category").getAsString();
            }
            attributesObj.entrySet().forEach(entry ->
                    attributes.put(entry.getKey(), entry.getValue().getAsString())
            );
        }


      /*  System.out.println("Deserializacja Motorcycle: id=" + id + ", brand=" + brand +
                ", licenceCategory=" + licenceCategory + ", plate=" + plate);
        */

        return new Motorcycle(id, brand, model, year, price, licenceCategory, plate, attributes);
    }
}