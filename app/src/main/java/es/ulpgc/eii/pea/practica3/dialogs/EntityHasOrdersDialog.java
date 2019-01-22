package es.ulpgc.eii.pea.practica3.dialogs;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import es.ulpgc.eii.pea.practica3.R;

public class EntityHasOrdersDialog {

    private final Context context;

    public EntityHasOrdersDialog(Context context) {
        this.context = context;
    }

    public void showDialog() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.couldNotDelete)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setCancelable(false)
                .show();
    }
}
