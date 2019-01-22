package es.ulpgc.eii.pea.practica3.interfaces;

import es.ulpgc.eii.pea.practica3.entities.Entity;

public interface EntityListFragment {
    void loadEntities();

    void notifyDataSetChanged();

    void removeEntity(Entity entity);
}
