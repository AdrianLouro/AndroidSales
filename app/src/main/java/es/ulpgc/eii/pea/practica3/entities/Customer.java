package es.ulpgc.eii.pea.practica3.entities;


import org.json.JSONException;
import org.json.JSONObject;

public class Customer implements Entity {

    private int id;
    private String name;
    private String address;

    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Customer(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    @Override
    public JSONObject toCreateJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("address", address);
        } catch (JSONException e) {
            return null;
        }

        return jsonObject;
    }

    @Override
    public JSONObject toEditJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("IDCustomer", id);
            jsonObject.put("name", name);
            jsonObject.put("address", address);
        } catch (JSONException e) {
            return null;
        }

        return jsonObject;
    }

    @Override
    public JSONObject toDeleteJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("IDCustomer", id);
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

    public String address() {
        return address;
    }

    public void address(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Customer &&
                ((Customer) obj).id() == id() &&
                ((Customer) obj).name().equals(name()) &&
                ((Customer) obj).address().equals(address());
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
        return name().compareTo(((Customer) o).name());
    }
}
