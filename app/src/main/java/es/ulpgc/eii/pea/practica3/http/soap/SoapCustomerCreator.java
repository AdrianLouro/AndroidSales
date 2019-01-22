package es.ulpgc.eii.pea.practica3.http.soap;

import android.content.Context;
import android.os.AsyncTask;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Vector;

import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.interfaces.EntityCreator;

import static java.lang.Integer.parseInt;
import static org.ksoap2.SoapEnvelope.VER12;

public class SoapCustomerCreator extends AsyncTask<Customer, Void, Customer> {
    private final String method = "InsertCustomer";
    private final EntityCreator entityCreator;
    private SoapHelper soapHelper = new SoapHelper(method);

    public SoapCustomerCreator(Context context, EntityCreator entityCreator) {
        this.entityCreator = entityCreator;
    }

    @Override
    protected Customer doInBackground(Customer... customers) {
        SoapObject request = new SoapObject(soapHelper.namespace(), method);
        request.addProperty("name", customers[0].name());
        request.addProperty("address", customers[0].address());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(VER12);
        envelope.setOutputSoapObject(request);

        try {
            new HttpTransportSE(soapHelper.url()).call(soapHelper.action(), envelope);
            customers[0].id(deserializeId((Vector<Object>) envelope.getResponse()));
            return customers[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int deserializeId(Vector<Object> response) {
        return parseInt(response.get(1).toString());
    }

    @Override
    protected void onPostExecute(Customer customer) {
        entityCreator.createEntityCallback(customer);
    }
}
