package es.ulpgc.eii.pea.practica3.entities;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Order implements Entity {

    private int id;
    private String code;
    private Date date;
    private int quantity;
    private Customer customer;
    private Product product;

    public Order(String code, Date date, int quantity, Customer customer, Product product) {
        this.code = code;
        this.date = date;
        this.quantity = quantity;
        this.customer = customer;
        this.product = product;
    }

    public Order(int id, String code, Date date, int quantity, Customer customer, Product product) {
        this.id = id;
        this.code = code;
        this.date = date;
        this.quantity = quantity;
        this.customer = customer;
        this.product = product;
    }

    @Override
    public JSONObject toCreateJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", code);
            jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(date));
            jsonObject.put("IDCustomer", customer.id());
            jsonObject.put("IDProduct", product.id());
            jsonObject.put("quantity", quantity);
        } catch (JSONException e) {
            return null;
        }

        return jsonObject;
    }

    @Override
    public JSONObject toEditJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("IDOrder", id);
            jsonObject.put("code", code);
            jsonObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(date));
            jsonObject.put("IDCustomer", customer.id());
            jsonObject.put("IDProduct", product.id());
            jsonObject.put("quantity", quantity);
        } catch (JSONException e) {
            return null;
        }

        return jsonObject;
    }

    @Override
    public JSONObject toDeleteJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("IDOrder", id);
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

    public String code() {
        return code;
    }

    public void code(String code) {
        this.code = code;
    }

    public Date date() {
        return date;
    }

    public void date(Date date) {
        this.date = date;
    }

    public int quantity() {
        return quantity;
    }

    public void quantity(int quantity) {
        this.quantity = quantity;
    }

    public Customer customer() {
        return customer;
    }

    public void customer(Customer customer) {
        this.customer = customer;
    }

    public Product product() {
        return product;
    }

    public void product(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Order &&
                ((Order) obj).id() == id() &&
                ((Order) obj).code().equals(code()) &&
                ((Order) obj).date().equals(date()) &&
                ((Order) obj).customer().id() == customer().id() &&
                ((Order) obj).product().id() == product().id() &&
                ((Order) obj).quantity() == quantity();
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
        Order order = (Order) o;
        return customer().compareTo(order.customer()) != 0 ?
                customer().compareTo(order.customer()) :
                product().compareTo(order.product()) != 0 ?
                        product().compareTo(order.product()) :
                        code().compareTo(order.code());
    }
}
