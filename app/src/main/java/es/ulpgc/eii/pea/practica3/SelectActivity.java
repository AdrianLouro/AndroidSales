package es.ulpgc.eii.pea.practica3;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.interfaces.NetworkChecksReceiver;
import es.ulpgc.eii.pea.practica3.interfaces.OnListFragmentInteractionListener;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class SelectActivity extends AppCompatActivity implements OnListFragmentInteractionListener, NetworkChecksReceiver {

    private SelectablesFragment selectablesFragment;
    private NetworkChecker networkChecker = new NetworkChecker();
    private Class entityClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        this.entityClass = (Class) getIntent().getSerializableExtra("entityClass");
        this.selectablesFragment = (SelectablesFragment) getSupportFragmentManager().findFragmentById(R.id.selectables_fragment);
        selectablesFragment.entityClass(entityClass);
        selectablesFragment.loadEntities();
        this.registerReceiver(networkChecker, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (networkChecker != null) this.unregisterReceiver(networkChecker);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onListFragmentClick(Entity entity) {
        setResult(RESULT_OK, new Intent().putExtra("customer", entity));
        finish();
    }

    @Override
    public void onListFragmentLongClick(Entity entity) {

    }


    @Override
    public void networkRecovered() {
        if (selectablesFragment != null) selectablesFragment.loadEntities();
    }
}
