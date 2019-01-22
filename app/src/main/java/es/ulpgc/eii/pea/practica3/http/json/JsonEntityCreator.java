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
import es.ulpgc.eii.pea.practica3.interfaces.EntityCreator;

import static com.android.volley.Request.Method.POST;
import static com.android.volley.toolbox.Volley.newRequestQueue;
import static es.ulpgc.eii.pea.practica3.http.HttpConstants.URL;

public class JsonEntityCreator<T extends Entity> {

    private Map<Class, String> methods = new HashMap<>();
    {
        methods.put(Customer.class, "InsertCustomer");
        methods.put(Product.class, "InsertProduct");
        methods.put(Order.class, "InsertOrder");
    }

    private final Context context;
    private final EntityCreator entityCreator;
    private final Class<T> type;
    private Entity entity;

    public JsonEntityCreator(Context context, Class<T> type, EntityCreator entityCreator, Entity entity) {
        this.context = context;
        this.type = type;
        this.entityCreator = entityCreator;
        this.entity = entity;
    }

    public void execute() {
        newRequestQueue(context).add(
                new JsonObjectRequest(
                        POST,
                        URL + "?" + methods.get(type),
                        entity.toCreateJsonObject(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    entity.id(response.getInt("data"));
                                    entityCreator.createEntityCallback(entity);
                                } catch (JSONException e) {
                                    entityCreator.createEntityCallback(null);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                entityCreator.createEntityCallback(null);
                            }
                        }
                )
        );
    }

}
