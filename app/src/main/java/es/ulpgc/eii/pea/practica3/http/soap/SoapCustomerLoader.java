package es.ulpgc.eii.pea.practica3.http.soap;

import android.content.Context;
import android.os.AsyncTask;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.interfaces.EntityLoader;

import static java.lang.Integer.parseInt;
import static java.util.Collections.sort;
import static org.ksoap2.SoapEnvelope.VER12;

public class SoapCustomerLoader extends AsyncTask<Customer, Void, List<Customer>> {
    private final String method = "QueryCustomers";
    private EntityLoader entityLoader;
    private SoapHelper soapHelper = new SoapHelper(method);

    public SoapCustomerLoader(Context context, EntityLoader entityLoader) {
        this.entityLoader = entityLoader;
    }

    @Override
    protected List<Customer> doInBackground(Customer... customers) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(VER12);
        envelope.setOutputSoapObject(new SoapObject(soapHelper.namespace(), method));

        try {
            new HttpTransportSE(soapHelper.url()).call(soapHelper.action(), envelope);
            return deserializeCustomers((Vector<Object>) envelope.getResponse());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Customer> deserializeCustomers(Vector<Object> response) {
        List<Customer> customers = new ArrayList<>();

        for (SoapObject soapCustomer : (Vector<SoapObject>) response.get(1))
            customers.add(
                    new Customer(
                            parseInt(((SoapObject) soapCustomer.getProperty(0)).getPropertyAsString("value")),
                            ((SoapObject) soapCustomer.getProperty(1)).getPropertyAsString("value"),
                            addressIsMissing(soapCustomer) ?
                                    "" :
                                    ((SoapObject) soapCustomer.getProperty(2)).getPropertyAsString("value")
                    )
            );


        return customers;
    }

    private boolean addressIsMissing(SoapObject soapCustomer) {
        return ((SoapObject) soapCustomer.getProperty(2)).getPropertyAsString("value") == null;
    }

    @Override
    protected void onPostExecute(List<Customer> customers) {
        if (customers != null) sort(customers);
        entityLoader.loadEntitiesCallback((List<Entity>) (List<? extends Entity>) customers);
    }

}
