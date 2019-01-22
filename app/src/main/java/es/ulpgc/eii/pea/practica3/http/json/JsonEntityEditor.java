package es.ulpgc.eii.pea.practica3.http.json;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.entities.Order;
import es.ulpgc.eii.pea.practica3.entities.Product;
import es.ulpgc.eii.pea.practica3.http.HttpConstants;
import es.ulpgc.eii.pea.practica3.interfaces.EntityEditor;

import static com.android.volley.Request.Method.POST;
import static com.android.volley.toolbox.Volley.newRequestQueue;
import static es.ulpgc.eii.pea.practica3.http.HttpConstants.URL;

public class JsonEntityEditor<T extends Entity> {

    private Map<Class, String> methods = new HashMap<>();
    {
        methods.put(Customer.class, "UpdateCustomer");
        methods.put(Product.class, "UpdateProduct");
        methods.put(Order.class, "UpdateOrder");
    }

    private final Context context;
    private final EntityEditor entityEditor;
    private final Class<T> type;
    private Entity entity;

    public JsonEntityEditor(Context context, Class<T> type, EntityEditor entityEditor, Entity entity) {
        this.context = context;
        this.type = type;
        this.entityEditor = entityEditor;
        this.entity = entity;
    }

    public void execute() {
        newRequestQueue(context).add(
                new JsonObjectRequest(
                        POST,
                        URL + "?" + methods.get(type),
                        entity.toEditJsonObject(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                entityEditor.editEntityCallback(entity);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                entityEditor.editEntityCallback(null);
                            }
                        }
                )
        );
    }

}
