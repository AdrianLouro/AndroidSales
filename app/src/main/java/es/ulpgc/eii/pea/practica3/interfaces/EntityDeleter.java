package es.ulpgc.eii.pea.practica3.interfaces;

import es.ulpgc.eii.pea.practica3.entities.Entity;

public interface EntityDeleter {
    void deleteEntityCallback(Entity entity, boolean hasAssociatedOrders);

    void confirmDeletion(Entity entity);
}
