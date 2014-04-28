package com.bikeridenetwork;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

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
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;

public class DisplayMap extends FragmentActivity {
	private GoogleMap map = null;
	private LocationListener locationListener;
	private String uploadApiUrl;
	private Handler handler;
	
	private ArrayList<Marker> markers = new ArrayList(); 
	
	private static final String TAG = "DisplayMap";
	private static final String WEB_APP_URL = "http://mluc.pythonanywhere.com/";
	private static final String WEB_APP_GET_TEST = "http://mluc.pythonanywhere.com/test";
	private static final String WEB_APP_POST_TEST = "http://mluc.pythonanywhere.com/start";
	
	private static GoogleApiClient mGoogleApiClient;
	private static friendData myData;
	
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
        	 
        	  if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
        		    String currentEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
        		    myData = new friendData(currentEmail, lat, lng); 
        	 }
        	 
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
    		
    		//HttpPost postRequest = new HttpPost(WEB_APP_POST_TEST);
    		HttpPost postRequest = new HttpPost(WEB_APP_URL);
    		Gson gson = new Gson();
    		String json = gson.toJson(myData);
    		System.out.println(json);
    		try {
				StringEntity se = new StringEntity(json);
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				se.writeTo(System.out);
				postRequest.setEntity(se);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
    		HttpClient httpClient = new DefaultHttpClient();
    		
    		//Send the data to the web service for upload
    		try {
    			HttpResponse httpResponse = httpClient.execute(postRequest);
    			HttpEntity entity = httpResponse.getEntity();
    			System.out.println(EntityUtils.toString(entity));
    		} catch (ClientProtocolException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

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

    private class friendData {
    	private String email;
    	private String latitude;
    	private String longitude;
    	
    	public friendData(String myEmail, double myLat, double myLng) {
    		this.email = myEmail;
    		this.latitude = String.valueOf(myLat);
    		this.longitude = String.valueOf(myLng);
    	}
    	
    	public void setMyLocation(double myLat, double myLng) {
    		this.latitude = String.valueOf(myLat);
    		this.longitude = String.valueOf(myLng);
    	}
    }

}
