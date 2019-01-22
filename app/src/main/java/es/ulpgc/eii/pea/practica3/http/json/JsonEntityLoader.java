package es.ulpgc.eii.pea.practica3.http.json;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.entities.Order;
import es.ulpgc.eii.pea.practica3.entities.Product;
import es.ulpgc.eii.pea.practica3.http.json.deserializers.JsonCustomerListDeserializer;
import es.ulpgc.eii.pea.practica3.http.json.deserializers.JsonEntityListDeserializer;
import es.ulpgc.eii.pea.practica3.http.json.deserializers.JsonOrderListDeserializer;
import es.ulpgc.eii.pea.practica3.http.json.deserializers.JsonProductListDeserializer;
import es.ulpgc.eii.pea.practica3.interfaces.EntityLoader;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.toolbox.Volley.newRequestQueue;
import static es.ulpgc.eii.pea.practica3.http.HttpConstants.URL;

public class JsonEntityLoader<T extends Entity> {

    private Map<Class, String> methods = new HashMap<>();

    {
        methods.put(Customer.class, "QueryCustomers");
        methods.put(Product.class, "QueryProducts");
        methods.put(Order.class, "QueryOrders");
    }

    private Map<Class, Class> deserializers = new HashMap<>();

    {
        deserializers.put(Customer.class, JsonCustomerListDeserializer.class);
        deserializers.put(Product.class, JsonProductListDeserializer.class);
        deserializers.put(Order.class, JsonOrderListDeserializer.class);
    }

    private final Context context;
    private final EntityLoader entityLoader;
    private final Class<T> type;

    public JsonEntityLoader(Context context, Class<T> type, EntityLoader entityLoader) {
        this.context = context;
        this.type = type;
        this.entityLoader = entityLoader;
    }

    public void execute() {
        newRequestQueue(context).add(
                new JsonObjectRequest(
                        GET,
                        URL + "?" + methods.get(type),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    entityLoader.loadEntitiesCallback(
                                            ((JsonEntityListDeserializer) deserializers.get(type).newInstance())
                                                    .deserialize(response.getJSONArray("data"))
                                    );
                                } catch (InstantiationException | IllegalAccessException | JSONException e) {
                                    entityLoader.loadEntitiesCallback(null);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                entityLoader.loadEntitiesCallback(null);
                            }
                        }
                )
        );
    }

}
