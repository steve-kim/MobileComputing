package com.bikeridenetwork;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class DisplayMap extends FragmentActivity {
	private GoogleMap map = null;
	private LocationListener locationListener;
	
	private static final String TAG = "DisplayMap";
	
	private static GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);
        
        if (map == null) {
        	 map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        	 map.setMyLocationEnabled(true);
        	 LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        	 
        	 Criteria criteria = new Criteria();
        	 
        	 String provider = locationManager.getBestProvider(criteria, true);
        	 
        	 Location location = locationManager.getLastKnownLocation(provider);
        	 
        	 if (location == null) {
        		 locationListener = new LocationListener() {
        			 public void onLocationChanged(Location location) {
        				 double lat = location.getLatitude();
        				 double lng = location.getLongitude();
        				 LatLng latLng = new LatLng(lat, lng);
        				 map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        			 }

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onProviderEnabled(String provider) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onProviderDisabled(String provider) {
						// TODO Auto-generated method stub
						
					}
        		 };
        		 
        		 locationManager.requestLocationUpdates(provider, 20000, 0, locationListener);
        	 }
        	 else {
        		 double lat = location.getLatitude();
        		 double lng = location.getLongitude();
        		 LatLng latLng = new LatLng(lat, lng);
        		 map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        	 }
        	 
        	 mGoogleApiClient = MainActivity.getGoogleApiClient();
        	 Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        	 Log.d(TAG, "Display name: " + currentPerson.getDisplayName());
        	 Log.d(TAG, "Current location: " + currentPerson.getCurrentLocation());
        	 
        }
       
    }



}
