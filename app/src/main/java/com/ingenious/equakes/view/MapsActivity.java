package com.ingenious.equakes.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ingenious.equakes.R;
import com.ingenious.equakes.model.Earthquake;
import com.ingenious.equakes.util.QueryUtils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(QueryUtils.getMapType() == 1){
            LatLng Location = new LatLng(QueryUtils.getSelectedEarthQuake().getmLatitude(),QueryUtils.getSelectedEarthQuake().getmLongitude());
            mMap.addMarker(new MarkerOptions().position(Location).title(QueryUtils.getSelectedEarthQuake().getmLocation()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Location));

         }else if(QueryUtils.getMapType() == 2) {
            for (Earthquake record : QueryUtils.getEarthquakeList()) {
                LatLng loc = new LatLng(record.getmLatitude(), record.getmLongitude());
                mMap.addMarker(new MarkerOptions().position(loc).title(record.getmLocation()));
            }
        }
    }
}
