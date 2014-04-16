package com.bikeridenetwork;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DisplayMap extends FragmentActivity {
	private GoogleMap map = null;
	private LocationListener locationListener;
	private String uploadApiUrl;
	private Handler handler;
	
	private ArrayList<Marker> markers = new ArrayList(); 
	
	private static final String TAG = "DisplayMap";
	
	private static GoogleApiClient mGoogleApiClient;
	
	private double lat;
	private double lng;
	
	//Dummy locations, delete when we get friends integrated
	private LatLng friend1;
	private LatLng friend2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);
        
        handler = new Handler(Looper.getMainLooper());
        
        
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
        				 lat = location.getLatitude();
        				 lng = location.getLongitude();
        				 
        				 //Dummy locations, delete after integrating friends
        				 friend1 = new LatLng(lat+5, lng+5);
        				 friend2 = new LatLng(lat-5, lng-5);
        				 
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
        		 lat = location.getLatitude();
        		 lng = location.getLongitude();
        		 
        		 //Dummy locations, delete after integrating friends
        		 friend1 = new LatLng(lat+5, lng+5);
				 friend2 = new LatLng(lat-5, lng-5);
        		 
        		 LatLng latLng = new LatLng(lat, lng);
        		 map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        	 }
        	 
        	 mGoogleApiClient = MainActivity.getGoogleApiClient();
        	 
        	 //Creating dummy positions for now
        	 markers.add(map.addMarker(new MarkerOptions().position(friend1)));
        	 markers.add(map.addMarker(new MarkerOptions().position(friend2)));
        	 
        	 
        	//Creating timer which executes once after 30 seconds
             Timer timer = new Timer();
             timer.scheduleAtFixedRate(new updateLocation(), 0, 30000);
        }
       
    }
    
    private class updateLocation extends TimerTask {
    	
    	public void run() {
    		System.out.println("Timer task executing every 30 seconds");
    		
    		/*HttpClient httpClient = new DefaultHttpClient();

    		uploadApiUrl = "http://apt-connexus.appspot.com/UploadServletAPI?latitude=" + String.valueOf(lat) + "&longitude=" + String.valueOf(lng);
    		HttpPost postRequest = new HttpPost(uploadApiUrl);


    		//Send the data to the web service for upload
    		try {
    			httpClient.execute(postRequest);
    		} catch (ClientProtocolException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}*/

    		handler.post(new Runnable() {
				@Override
				public void run() {
					for (Marker m : markers) {
	    				m.setPosition(new LatLng(m.getPosition().latitude+1, m.getPosition().longitude+1));
	        		}
				}
    		});
    		
    	}

    }



}
