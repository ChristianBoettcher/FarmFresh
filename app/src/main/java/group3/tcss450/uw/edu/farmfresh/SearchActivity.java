package group3.tcss450.uw.edu.farmfresh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import group3.tcss450.uw.edu.farmfresh.handler.GetAPIAsync;
import group3.tcss450.uw.edu.farmfresh.handler.GetAPIDetailsAsync;
import group3.tcss450.uw.edu.farmfresh.sqlite.ListDB;
import group3.tcss450.uw.edu.farmfresh.sqlite.ListEntry;

/**
 * Activity for search by zip codes (main page of app).
 * Activity called after user successfully logs in.
 */
public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchFragment.OnFragmentInteractionListener{

    private ListDB mMarketDB;
    private ArrayList<String> marketList;
    private HashMap<String, Integer> marketMap;

    /**
     *Initializes this activity with SearchFragment.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (mMarketDB == null) {
            mMarketDB = new ListDB(this);
        }
        SearchFragment sf = new SearchFragment();
        List<ListEntry> marketEntries = mMarketDB.getList();
        marketList = new ArrayList<String>();
        marketMap = new HashMap<String, Integer>();
        for (ListEntry market : marketEntries) {
            marketList.add(market.getMarketName());
            marketMap.put(market.getMarketName(), market.getMarketId());
            Log.d("MARKET LIST TEST", market.getMarketName());
        }

        Bundle args = new Bundle();
        args.putStringArrayList(getString(R.string.MARKET_LIST), marketList);
        args.putSerializable(getString(R.string.MARKET_MAP), marketMap);
        sf.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_container, sf)
                .commit();
    }

    /**
     * Back Button handler.
     * Closes drawer if it was open,
     * otherwise acts like normal back button.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Initializes Action bar if options.
     * @param menu Selected menu.
     * @return true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    /**
     * Handles clicks on one of the options of Action Bar.
     * @param item Selected item.
     * @return true.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles selection of one of the buttons
     * in drawer menu.
     * @param item Selected MenuItem.
     * @return true.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            /*Bundle args = new Bundle();
            args.putSerializable(getString(R.string.DB_NAME),
                    (Serializable) userDB.getUser());
            args.putSerializable(getString(R.string.LOGGED_OUT), true);

            LoginFragment lf = new LoginFragment();
            lf.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, lf)
                    .commit();*/


            //startActivity(new Intent(this, LoginActivity.class));

            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("SQLITE", 1);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Searches for markets near user typed zip code.
     */
    @Override
    public void searchZip() {
        final ListView list = (ListView) findViewById(R.id.search_list);
        EditText zipcode = (EditText) findViewById(R.id.search_text);

        GetAPIAsync apiTask = new GetAPIAsync(this, marketList, marketMap);
        apiTask.execute(zipcode.getText().toString());

    }

    public void saveMarketList(ArrayList<String> marketList, Map<String, Integer> map) {
        if (mMarketDB == null) {
            mMarketDB = new ListDB(this);
        }
        this.marketList = marketList;
        this.marketMap = marketMap;
        mMarketDB.clearList();
        for (String market : marketList) {
            mMarketDB.insertMarket(market, map.get(market));
        }
    }

    /*public void farmDetails(String market) {
        ListView detailsList = (ListView) findViewById(R.id.farm_details_list);
        ArrayList<String> detailsItemList = new ArrayList<>();
        ArrayAdapter<String> detailsAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.list_view_layout, R.id.custom_text_view, detailsItemList);
        detailsList.setAdapter(detailsAdapter);
        GetAPIDetailsAsync detailsApiTask = new GetAPIDetailsAsync(this, detailsAdapter, detailsItemList);
        detailsApiTask.execute(market);
    }*/
}
