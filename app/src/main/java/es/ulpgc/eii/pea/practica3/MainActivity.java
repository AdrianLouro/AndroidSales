package es.ulpgc.eii.pea.practica3;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import es.ulpgc.eii.pea.practica3.dialogs.DeleteEntityConfirmDialog;
import es.ulpgc.eii.pea.practica3.dialogs.EntityHasOrdersDialog;
import es.ulpgc.eii.pea.practica3.dialogs.HttpErrorDialog;
import es.ulpgc.eii.pea.practica3.entities.Customer;
import es.ulpgc.eii.pea.practica3.entities.Entity;
import es.ulpgc.eii.pea.practica3.entities.Order;
import es.ulpgc.eii.pea.practica3.entities.Product;
import es.ulpgc.eii.pea.practica3.http.json.JsonEntityDeleter;
import es.ulpgc.eii.pea.practica3.interfaces.EntityDeleter;
import es.ulpgc.eii.pea.practica3.interfaces.EntityListFragment;
import es.ulpgc.eii.pea.practica3.interfaces.NetworkChecksReceiver;
import es.ulpgc.eii.pea.practica3.interfaces.OnListFragmentInteractionListener;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static android.support.v4.view.GravityCompat.START;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnListFragmentInteractionListener, EntityDeleter, NetworkChecksReceiver {

    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private DrawerLayout drawer;
    private String[] tabs = new String[3];
    private Class[] fragments = new Class[]{CustomersFragment.class, ProductsFragment.class, OrdersFragment.class};
    private Class[] activities = new Class[]{CustomerActivity.class, ProductActivity.class, OrderActivity.class};
    private Class[] entities = new Class[]{Customer.class, Product.class, Order.class};
    private EntityListFragment[] entityListFragments = new EntityListFragment[tabs.length];
    private EntityListFragment currentEntityListFragment;
    private NetworkChecker networkChecker = new NetworkChecker();
    Map<Integer, Integer> menuItems = new HashMap<>();

    {
        menuItems.put(R.id.customers_tab, 0);
        menuItems.put(R.id.products_tab, 1);
        menuItems.put(R.id.orders_tab, 2);
    }


    private static int currentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.container);
        drawer = findViewById(R.id.drawer_layout);

        viewPager.setAdapter(getFragmentPagerAdapter());
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.tab_customers);
        getTabsFromResources();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(getTabLayoutOnTabSelectedListener());
        viewPager.setCurrentItem(currentTab);
        ((NavigationView) findViewById(R.id.navigation_view)).setNavigationItemSelectedListener(this);
        setFloatingButtonListener();
        setDrawer();
        this.registerReceiver(networkChecker, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (networkChecker != null) this.unregisterReceiver(networkChecker);
    }

    private void setDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(START)) drawer.closeDrawer(START);
        else if (tabLayout.getSelectedTabPosition() > 0) viewPager.setCurrentItem(0);
        else super.onBackPressed();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        viewPager.setCurrentItem(menuItems.get(item.getItemId()));
        drawer.closeDrawer(START);
        return true;
    }

    private void getTabsFromResources() {
        tabs[0] = getString(R.string.tab_customers);
        tabs[1] = getString(R.string.tab_products);
        tabs[2] = getString(R.string.tab_orders);
    }

    private void setFloatingButtonListener() {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, activities[tabLayout.getSelectedTabPosition()]));
            }
        });
    }

    private TabLayout.OnTabSelectedListener getTabLayoutOnTabSelectedListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                toolbar.setTitle(tabs[currentTab = tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    private FragmentPagerAdapter getFragmentPagerAdapter() {
        return new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                try {
                    return (Fragment) fragments[position].newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public int getCount() {
                return tabs.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabs[position];
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                currentEntityListFragment = (EntityListFragment) object;
                entityListFragments[position] = (EntityListFragment) object;
                super.setPrimaryItem(container, position, object);
            }
        };
    }

    @Override
    public void onListFragmentClick(Entity entity) {
        startActivity(new Intent(this, activities[tabLayout.getSelectedTabPosition()]).putExtra("entity", entity));
    }

    @Override
    public void onListFragmentLongClick(Entity entity) {
        new DeleteEntityConfirmDialog(this, this, entity).showDialog();
    }

    @Override
    public void confirmDeletion(Entity entity) {
        new JsonEntityDeleter<Entity>(getApplicationContext(), entities[tabLayout.getSelectedTabPosition()], this, entity).execute();
    }

    @Override
    public void deleteEntityCallback(Entity entity, boolean hasAssociatedOrders) {
        if (entity == null) {
            if (hasAssociatedOrders) new EntityHasOrdersDialog(this).showDialog();
            else new HttpErrorDialog(this).showDialog();
            return;
        }

        makeText(this, R.string.entityDeleted, Toast.LENGTH_SHORT).show();
        currentEntityListFragment.removeEntity(entity);
        currentEntityListFragment.notifyDataSetChanged();
    }

    @Override
    public void networkRecovered() {
        for (EntityListFragment entityListFragment : entityListFragments)
            if (entityListFragment != null) entityListFragment.loadEntities();
    }
}
