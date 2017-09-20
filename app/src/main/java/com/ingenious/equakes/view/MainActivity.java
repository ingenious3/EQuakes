package com.ingenious.equakes.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ingenious.equakes.R;
import com.ingenious.equakes.adapter.EarthquakeAdapter;
import com.ingenious.equakes.model.Earthquake;
import com.ingenious.equakes.util.EarthquakeLoader;
import com.ingenious.equakes.util.QueryUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<ArrayList<Earthquake>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final String REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    private static final int EARTHQUAKE_LOADER_ID = 1;
    private EarthquakeAdapter adapter;
    private TextView emptyTextView;
    private ProgressBar mProgressBar;
    private Earthquake earthquake = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView earthquakeListView = (ListView) findViewById(R.id.list);

        emptyTextView = (TextView)findViewById(R.id.Empty_txtview);
        earthquakeListView.setEmptyView(emptyTextView);

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QueryUtils.setMapType(1);
                QueryUtils.setSelectedEarthQuake(adapter.getItem(position));
                Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(mapIntent);

            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo !=null && networkInfo.isConnected()){

            android.app.LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

        } else {
            mProgressBar.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet);
        }

    }

    @Override
    public android.content.Loader<ArrayList<Earthquake>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "50");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> earthquakes) {

        mProgressBar.setVisibility(View.GONE);

        emptyTextView.setText(R.string.no_earthquakes);

        adapter.clear();

        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<ArrayList<Earthquake>> loader) {
        adapter.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_Map) {
            QueryUtils.setMapType(2);
            Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(mapIntent);
            return true;
        }

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        QueryUtils.setMapType(0);
    }
}
