package es.ulpgc.eii.pea.practica3.http.json.deserializers;

import org.json.JSONArray;

import java.util.List;

import es.ulpgc.eii.pea.practica3.entities.Entity;

/**
 * Created by alour on 19/04/2018.
 */

public interface JsonEntityListDeserializer {
    List<Entity> deserialize(JSONArray jsonArray);
}
