package org.example;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CarDeserializer implements JsonDeserializer<Car> {
    @Override
    public Car deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        int id = Integer.parseInt(obj.get("id").getAsString());
        String brand = obj.get("brand").getAsString();
        String model = obj.get("model").getAsString();
        int year = obj.get("year").getAsInt();
        String plate = obj.has("plate") ? obj.get("plate").getAsString() : "";
        int price = obj.has("price") ? obj.get("price").getAsInt() : 0;

        Map<String, String> attributes = new HashMap<>();
        if (obj.has("attributes")) {
            JsonObject attributesObj = obj.getAsJsonObject("attributes");
            attributesObj.entrySet().forEach(entry ->
                    attributes.put(entry.getKey(), entry.getValue().getAsString())
            );
        }
        return new Car(id, brand, model, year, price, plate, attributes);
    }
}