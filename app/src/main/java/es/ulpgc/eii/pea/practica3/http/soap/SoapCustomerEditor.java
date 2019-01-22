package es.ulpgc.eii.pea.practica3.http.soap;

import android.content.Context;
import android.os.AsyncTask;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Vector;

import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.interfaces.EntityEditor;

import static java.lang.Boolean.TRUE;
import static org.ksoap2.SoapEnvelope.VER12;

public class SoapCustomerEditor extends AsyncTask<Customer, Void, Entity> {
    private final String method = "UpdateCustomer";
    private final EntityEditor entityEditor;
    private SoapHelper soapHelper = new SoapHelper(method);

    public SoapCustomerEditor(Context context, EntityEditor entityEditor) {
        this.entityEditor = entityEditor;
    }

    @Override
    protected Entity doInBackground(Customer... customers) {
        SoapObject request = new SoapObject(soapHelper.namespace(), method);
        request.addProperty("IDCustomer", customers[0].id());
        request.addProperty("name", customers[0].name());
        request.addProperty("address", customers[0].address());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(VER12);
        envelope.setOutputSoapObject(request);

        try {
            new HttpTransportSE(soapHelper.url()).call(soapHelper.action(), envelope);
            return customerWasEdited((Vector<Object>) envelope.getResponse()) ?
                    customers[0] :
                    null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean customerWasEdited(Vector<Object> response) {
        return response.get(1).equals(TRUE);
    }

    @Override
    protected void onPostExecute(Entity entity) {
        entityEditor.editEntityCallback(entity);
    }
}
