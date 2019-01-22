package es.ulpgc.eii.pea.practica3.dialogs;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import es.ulpgc.eii.pea.practica3.R;
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.interfaces.EntityDeleter;

public class DeleteEntityConfirmDialog {

    private final EntityDeleter entityDeleter;
    private final Context context;
    private final Entity entity;

    public DeleteEntityConfirmDialog(EntityDeleter entityDeleter, Context context, Entity entity) {
        this.entityDeleter = entityDeleter;
        this.context = context;
        this.entity = entity;
    }

    public void showDialog() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.confirmDeletionTitle)
                .setMessage(R.string.confirmDeletionMessage)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        entityDeleter.confirmDeletion(entity);
                    }
                })
                .setNegativeButton(R.string.no, null)
                .setCancelable(false)
                .show();
    }
}
