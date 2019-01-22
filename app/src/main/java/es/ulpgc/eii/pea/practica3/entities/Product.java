package es.ulpgc.eii.pea.practica3.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Product implements Cloneable, Entity {

    private int id;
    private String name;
    private String description;
    private double price;

    public Product(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product(int id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @Override
    public JSONObject toCreateJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("description", description);
            jsonObject.put("price", price);
        } catch (JSONException e) {
            return null;
        }

        return jsonObject;
    }

    @Override
    public JSONObject toEditJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("IDProduct", id);
            jsonObject.put("name", name);
            jsonObject.put("description", description);
            jsonObject.put("price", price);
        } catch (JSONException e) {
            return null;
        }

        return jsonObject;
    }

    @Override
    public JSONObject toDeleteJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("IDProduct", id);
        } catch (JSONException e) {
            return null;
        }

        return jsonObject;
    }

    public int id() {
        return id;
    }

    public void id(int id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    public String description() {
        return description;
    }

    public void description(String address) {
        this.description = address;
    }

    public double price() {
        return price;
    }

    public void price(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Product &&
                ((Product) obj).id() == id() &&
                ((Product) obj).name().equals(name()) &&
                ((Product) obj).description().equals(description()) &&
                ((Product) obj).price() == price();
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int compareTo(Object o) {
        return name().compareTo(((Product) o).name());
    }
}
