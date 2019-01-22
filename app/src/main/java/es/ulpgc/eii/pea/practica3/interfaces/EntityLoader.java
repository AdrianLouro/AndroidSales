package es.ulpgc.eii.pea.practica3.interfaces;

import java.util.List;

import es.ulpgc.eii.pea.practica3.entities.Entity;

public interface EntityLoader {
    void loadEntitiesCallback(List<Entity> entities);
}
