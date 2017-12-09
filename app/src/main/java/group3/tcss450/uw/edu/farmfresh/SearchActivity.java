package group3.tcss450.uw.edu.farmfresh;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
    private SharedPreferences mPrefs;

    private String[] myFilters = {""};

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

        mPrefs = getSharedPreferences(getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);

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
            //super.onBackPressed();
            Log.d("POP", "POPOPO");

            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() == 0) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            } else {
                super.onBackPressed();
            }

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

            Intent intent = new Intent(this, LoginActivity.class);
            //intent.putExtra("SQLITE", 1);
            String username = mPrefs.getString(getString(R.string.SAVEDNAME), "");
            String password = mPrefs.getString(getString(R.string.SAVEDPASS), "");
            saveToSharedPrefs(username, password, 0);
            startActivity(intent);
        } else if (id == R.id.filters_item) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            //alert.setTitle("Filters");


            LinearLayout alertLayout = new LinearLayout(this);
            alertLayout.setOrientation(LinearLayout.VERTICAL);

            final TextView tv1 = new TextView(this);
            tv1.setText("Enter Product");
            tv1.setTextSize(20);
            final EditText productFilter = new EditText(this);
            productFilter.setText(myFilters[0]);

            alertLayout.addView(tv1);
            alertLayout.addView(productFilter);

            alert.setView(alertLayout);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myFilters[0] = productFilter.getText().toString();
                }
            });
            alert.show();
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

        GetAPIAsync apiTask = new GetAPIAsync(this, marketList, marketMap, myFilters);
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

    public void saveToSharedPrefs(String name, String pass, Integer auto) {
        mPrefs.edit().putString(getString(R.string.SAVEDNAME), name).apply();
        mPrefs.edit().putString(getString(R.string.SAVEDPASS), pass).apply();
        mPrefs.edit().putInt(getString(R.string.SAVEDAUTO), auto).apply();
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
