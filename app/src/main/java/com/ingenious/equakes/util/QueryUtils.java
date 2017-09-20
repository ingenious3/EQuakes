package com.ingenious.equakes.util;

import android.text.TextUtils;
import android.util.Log;

import com.ingenious.equakes.model.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    private static ArrayList<Earthquake> EarthquakeList = new ArrayList<>();

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static int mapType = 0;   // mapType = 0 -> no map , mapType = 1 -> point map  ,mapType = 2 -> full map

    private static Earthquake selectedEarthQuake = null;

    public QueryUtils() {
    }


    public  static ArrayList<Earthquake> fetchEarthquakeList(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem Closing inputStream", e);
        }
        EarthquakeList.clear();
        EarthquakeList = getEarthquakesDetails(jsonResponse);

        return EarthquakeList;
    }
    private static URL  createUrl(String stringUrl){

        URL url = null;
        try{
            url = new URL(stringUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error Creating Url", e);
        }
        return url;

    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(30000 /*milliseconds*/);
            urlConnection.setConnectTimeout(60000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode()== 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else{
                Log.e(LOG_TAG, "Error Response Code : " + urlConnection.getResponseCode());
            }
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem Retrieving the json Response", e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null){

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){

                output.append(line);
                line = reader.readLine();
            }
        }

      return output.toString();

    }

    public static ArrayList<Earthquake> getEarthquakesDetails(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }
        try{

            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray earthquakesArray = baseJsonResponse.getJSONArray("features");

            for (int i = 0; i < earthquakesArray.length(); i++) {

                JSONObject currentEarthquake = earthquakesArray.getJSONObject(i);

                JSONObject properties = currentEarthquake.getJSONObject("properties");
                JSONObject geometry = currentEarthquake.getJSONObject("geometry");

                double magnitude = properties.getDouble("mag");
                Long time = properties.getLong("time");
                String location = properties.getString("place");

                JSONArray coordinates=geometry.getJSONArray("coordinates");
                double longitude=coordinates.getDouble(0);
                double latitude=coordinates.getDouble(1);

                Earthquake earthquake = new Earthquake(magnitude, location, time,longitude,latitude);

                getEarthquakeList().add(earthquake);
            }

        } catch(JSONException e) {
            Log.e("QueryUtils.java", "Problem Parsing the result.", e);
        }
        return EarthquakeList;

    }

    public static ArrayList<Earthquake> getEarthquakeList() {
        return EarthquakeList;
    }

    public static void setEarthquakeList(ArrayList<Earthquake> earthquakeList) {
        EarthquakeList = earthquakeList;
    }

    public static int getMapType() {
        return mapType;
    }

    public static void setMapType(int mapType) {
        QueryUtils.mapType = mapType;
    }

    public static Earthquake getSelectedEarthQuake() {
        return selectedEarthQuake;
    }

    public static void setSelectedEarthQuake(Earthquake selectedEarthQuake) {
        QueryUtils.selectedEarthQuake = selectedEarthQuake;
    }
}