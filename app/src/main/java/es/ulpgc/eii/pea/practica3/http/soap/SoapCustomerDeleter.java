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
import es.ulpgc.eii.pea.practica3.interfaces.EntityDeleter;

import static java.lang.Boolean.TRUE;
import static java.lang.Integer.parseInt;
import static org.ksoap2.SoapEnvelope.VER12;

public class SoapCustomerDeleter extends AsyncTask<Customer, Void, SoapCustomerDeleter.CustomerWithHasAssociatedOrders> {
    private final String method = "DeleteCustomer";
    private final EntityDeleter entityDeleter;
    private SoapHelper soapHelper = new SoapHelper(method);

    public SoapCustomerDeleter(Context context, EntityDeleter entityDeleter) {
        this.entityDeleter = entityDeleter;
    }

    @Override
    protected CustomerWithHasAssociatedOrders doInBackground(Customer... customers) {
        if (customerHasOrders(customers[0])) return new CustomerWithHasAssociatedOrders(null, true);

        SoapObject request = new SoapObject(soapHelper.namespace(), method);
        request.addProperty("IDCustomer", customers[0].id());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(VER12);
        envelope.setOutputSoapObject(request);

        try {
            new HttpTransportSE(soapHelper.url()).call(soapHelper.action(), envelope);
            return new CustomerWithHasAssociatedOrders(
                    customerWasDeleted((Vector<Object>) envelope.getResponse()) ? customers[0] : null,
                    false
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private boolean customerWasDeleted(Vector<Object> response) {
        return response.get(1).equals(TRUE);
    }

    @Override
    protected void onPostExecute(CustomerWithHasAssociatedOrders customerWithHasAssociatedOrders) {
        entityDeleter.deleteEntityCallback(
                customerWithHasAssociatedOrders.customer,
                customerWithHasAssociatedOrders.hasAssociatedOrders
        );
    }

    private boolean customerHasOrders(Customer customer) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(VER12);
        envelope.setOutputSoapObject(new SoapObject(soapHelper.namespace(), "QueryOrders"));

        try {
            new HttpTransportSE(soapHelper.url()).call(soapHelper.action(), envelope);
            return checkCustomerHasOrders(customer, deserializeCustomersIds((Vector<Object>) envelope.getResponse()));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkCustomerHasOrders(Customer customer, List<Integer> ids) {
        for (int id : ids)
            if (id == customer.id()) return true;

        return false;
    }

    private List<Integer> deserializeCustomersIds(Vector<Object> response) {
        List<Integer> ids = new ArrayList<>();
        for (SoapObject soapCustomer : (Vector<SoapObject>) response.get(1)) {
            if (customerOrProductIsMissing(soapCustomer)) continue;
            ids.add(parseInt(((SoapObject) soapCustomer.getProperty(1)).getPropertyAsString("value")));
        }

        return ids;
    }

    private boolean customerOrProductIsMissing(SoapObject soapCustomer) {
        return ((SoapObject) soapCustomer.getProperty(1)).getProperty("value") == null;
    }

    class CustomerWithHasAssociatedOrders {
        private Customer customer;
        private boolean hasAssociatedOrders;

        private CustomerWithHasAssociatedOrders(Customer customer, boolean hasAssociatedOrders) {
            this.customer = customer;
            this.hasAssociatedOrders = hasAssociatedOrders;
        }
    }
}
