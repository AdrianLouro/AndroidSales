package es.ulpgc.eii.pea.practica3.http.json.deserializers;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.entities.Entity;

import static java.util.Collections.sort;

/**
 * Created by alour on 19/04/2018.
 */

public class JsonCustomerListDeserializer implements JsonEntityListDeserializer {

    public List<Entity> deserialize(JSONArray jsonArray) {
        List<Entity> customers = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                customers.add(new Customer(
                        jsonArray.getJSONObject(i).getInt("IDCustomer"),
                        jsonArray.getJSONObject(i).getString("name"),
                        addressIsMissing(jsonArray, i) ?
                                "" :
                                jsonArray.getJSONObject(i).getString("address")
                ));
            } catch (JSONException e) {
                return new ArrayList<>();
            }
        }

        sort(customers);
        return customers;
    }

    private boolean addressIsMissing(JSONArray jsonArray, int i) throws JSONException {
        return jsonArray.getJSONObject(i).isNull("address");
    }
}
