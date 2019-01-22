package es.ulpgc.eii.pea.practica3.http.json.deserializers;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.entities.Product;

import static java.util.Collections.sort;

/**
 * Created by alour on 19/04/2018.
 */

public class JsonProductListDeserializer implements JsonEntityListDeserializer {

    public List<Entity> deserialize(JSONArray jsonArray) {
        List<Entity> products = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                products.add(new Product(
                        jsonArray.getJSONObject(i).getInt("IDProduct"),
                        jsonArray.getJSONObject(i).getString("name"),
                        descriptionIsMissing(jsonArray, i) ?
                                "" :
                                jsonArray.getJSONObject(i).getString("description"),
                        jsonArray.getJSONObject(i).getDouble("price")
                ));
            } catch (JSONException e) {
                return new ArrayList<>();
            }
        }

        sort(products);
        return products;
    }

    private boolean descriptionIsMissing(JSONArray jsonArray, int i) throws JSONException {
        return jsonArray.getJSONObject(i).isNull("description");
    }
}
