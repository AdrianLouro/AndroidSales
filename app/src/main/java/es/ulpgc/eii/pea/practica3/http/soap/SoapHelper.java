package es.ulpgc.eii.pea.practica3.http.soap;

import static es.ulpgc.eii.pea.practica3.http.HttpConstants.URL;

/**
 * Created by alour on 26/04/2018.
 */

public class SoapHelper {
    private final String method;
    private final String namespace = "urn://ulpgc.masterii.moviles";

    public SoapHelper(String method) {
        this.method = method;
    }

    public String action() {
        return namespace + method;
    }

    public String url() {
        return URL + "?" + method;
    }

    public String namespace() {
        return namespace;
    }
}
