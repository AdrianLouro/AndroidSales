package es.ulpgc.eii.pea.practica3.entities;

import org.json.JSONObject;

import java.io.Serializable;

public interface Entity extends Serializable, Cloneable, Comparable {
    void id(int id);

    int id();

    JSONObject toCreateJsonObject();

    JSONObject toEditJsonObject();

    JSONObject toDeleteJsonObject();
}
