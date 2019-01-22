package es.ulpgc.eii.pea.practica3.http.json.deserializers;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.entities.Order;
import es.ulpgc.eii.pea.practica3.entities.Product;

import static java.util.Collections.sort;

/**
 * Created by alour on 19/04/2018.
 */

public class JsonOrderListDeserializer implements JsonEntityListDeserializer {

    public List<Entity> deserialize(JSONArray jsonArray) {
        List<Entity> orders = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                if (customerOrProductIsMissing(jsonArray, i)) continue;
                orders.add(new Order(
                        jsonArray.getJSONObject(i).getInt("IDOrder"),
                        jsonArray.getJSONObject(i).getString("code"),
                        dateIsMissing(jsonArray, i) ?
                                new Date() :
                                date(jsonArray.getJSONObject(i).getString("date")),
                        jsonArray.getJSONObject(i).getInt("quantity"),
                        new Customer(
                                jsonArray.getJSONObject(i).getInt("IDCustomer"),
                                jsonArray.getJSONObject(i).getString("customerName"),
                                ""
                        ),
                        new Product(
                                jsonArray.getJSONObject(i).getInt("IDProduct"),
                                jsonArray.getJSONObject(i).getString("productName"),
                                "",
                                jsonArray.getJSONObject(i).getDouble("price")
                        )
                ));
            } catch (JSONException | ParseException e) {
                return new ArrayList<>();
            }

        }

        sort(orders);
        return orders;

    }

    private boolean dateIsMissing(JSONArray jsonArray, int i) throws JSONException {
        return jsonArray.getJSONObject(i).isNull("date");
    }

    private boolean customerOrProductIsMissing(JSONArray jsonArray, int i) throws JSONException {
        return jsonArray.getJSONObject(i).isNull("IDCustomer") ||
                jsonArray.getJSONObject(i).isNull("IDProduct");
    }

    private Date date(String date) throws JSONException, ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return date.equals("null") ?
                new Date() :
                formatter.parse(date);
    }
}
