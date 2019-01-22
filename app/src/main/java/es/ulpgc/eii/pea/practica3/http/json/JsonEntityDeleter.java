package es.ulpgc.eii.pea.practica3.http.json;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.entities.Order;
import es.ulpgc.eii.pea.practica3.entities.Product;
import es.ulpgc.eii.pea.practica3.http.json.deserializers.JsonOrderListDeserializer;
import es.ulpgc.eii.pea.practica3.interfaces.EntityDeleter;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.toolbox.Volley.newRequestQueue;
import static es.ulpgc.eii.pea.practica3.http.HttpConstants.URL;

public class JsonEntityDeleter<T extends Entity> {

    private Map<Class, String> methods = new HashMap<>();

    {
        methods.put(Customer.class, "DeleteCustomer");
        methods.put(Product.class, "DeleteProduct");
        methods.put(Order.class, "DeleteOrder");
    }

    private final Context context;
    private final EntityDeleter entityDeleter;
    private final Class<T> type;
    private Entity entity;

    public JsonEntityDeleter(Context context, Class<T> type, EntityDeleter entityDeleter, Entity entity) {
        this.context = context;
        this.type = type;
        this.entityDeleter = entityDeleter;
        this.entity = entity;
    }

    public void execute() {
        if (type == Customer.class || type == Product.class) checkAssociatedOrders();
        else deleteEntity();
    }

    private void deleteEntity() {
        newRequestQueue(context).add(
                new JsonObjectRequest(
                        POST,
                        URL + "?" + methods.get(type),
                        entity.toDeleteJsonObject(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                entityDeleter.deleteEntityCallback(entity, false);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                entityDeleter.deleteEntityCallback(null, false);
                            }
                        }
                )
        );
    }

    private void checkAssociatedOrders() {
        newRequestQueue(context).add(
                new JsonObjectRequest(
                        GET,
                        URL + "?QueryOrders",
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (checkEntityHasOrders(new JsonOrderListDeserializer()
                                            .deserialize(response.getJSONArray("data"))))
                                        entityDeleter.deleteEntityCallback(null, true);
                                    else deleteEntity();
                                } catch (JSONException e) {
                                    entityDeleter.deleteEntityCallback(null, false);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                entityDeleter.deleteEntityCallback(null, false);
                            }
                        }
                )
        );
    }

    private boolean checkEntityHasOrders(List<Entity> orders) {
        for (Entity order : orders)
            if (type == Customer.class && ((Order) order).customer().id() == entity.id() ||
                    type == Product.class && ((Order) order).product().id() == entity.id())
                return true;

        return false;
    }

}
