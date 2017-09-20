package com.ingenious.equakes.util;

import android.content.AsyncTaskLoader;
import android.content.Context;


import com.ingenious.equakes.model.Earthquake;

import java.util.ArrayList;


public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getSimpleName();

    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {
        if (mUrl == null){
            return null;
        }

        ArrayList<Earthquake> Earthquakes = QueryUtils.fetchEarthquakeList(mUrl);

        return Earthquakes;
    }
}
