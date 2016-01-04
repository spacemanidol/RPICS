package com.androidhive.xmlparsing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;
import com.mapquest.android.maps.RouteManager;
import com.mapquest.android.maps.RouteResponse;
import com.mapquest.android.maps.ServiceResponse.Info;

public class Routewithstopover extends Activity {
	static final String KEY_NAME = "name";
	static final String KEY_LAT = "lat";
	static final String KEY_LNG = "lng";
	protected MapView map; 
	protected MyLocationOverlay myLocationOverlay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routewithstopover);
		// getting intent data
		Intent in = getIntent();

		// Get XML values from previous intent
		String name = in.getStringExtra(KEY_NAME);
		String lat = in.getStringExtra(KEY_LAT);
		String lng = in.getStringExtra(KEY_LNG);
		this.setupMapView(new GeoPoint(38.0,-104.0), 5);
		setupMyLocation();
		GeoPoint gp =returnMyLocationYOCO();
		double latitude=gp.getLatitude();
		double longitude=gp.getLongitude();
		String start=latitude+","+longitude;
		String end= lat+","+lng;

		//createPath(start,end);


	}
	public void createPath(String start, String end)
	{
		final MapView mapView = (MapView)findViewById(R.id.map);
		final WebView itinerary=(WebView)findViewById(R.id.itinerary);		
		final RelativeLayout mapLayout=(RelativeLayout)findViewById(R.id.mapLayout);
		final RelativeLayout itineraryLayout=(RelativeLayout)findViewById(R.id.itineraryLayout);
		final Button showItineraryButton=(Button)findViewById(R.id.showItineraryButton);
		final Button showMapButton=(Button)findViewById(R.id.showMapButton);
		final RouteManager routeManager=new RouteManager(this);
		final RouteResponse routeResponse=new RouteResponse();
		routeManager.setMapView(mapView);
		routeManager.setItineraryView(itinerary);
		routeManager.setDebug(true);
		routeManager.createRoute(start,end);
		routeManager.setRouteCallback(new RouteManager.RouteCallback() 
		{

			/**
			 * if creating the route fails
			 */
			@Override
			public void onError(RouteResponse routeResponse) {
				Info info=routeResponse.info;
				int statusCode=info.statusCode;

				StringBuilder message =new StringBuilder();
				message.append("Unable to create route.\n")
				.append("Error: ").append(statusCode).append("\n")
				.append("Message: ").append(info.messages);
				Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_LONG).show();
			}

			/**
			 * create route succeds
			 */
			@SuppressWarnings("null")
			@Override
			public void onSuccess(RouteResponse routeResponse) {

				//Change buttons
				if(showItineraryButton.getVisibility()==View.GONE &&
						showMapButton.getVisibility()==View.GONE){
					showItineraryButton.setVisibility(View.VISIBLE);
				}
				//save all the points along route so these points can be used later during search
			}
		});
		/**
		 * to show itinerary
		 */
		//attach the show itinerary listener
		showItineraryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mapLayout.setVisibility(View.GONE);
				itineraryLayout.setVisibility(View.VISIBLE);
				showItineraryButton.setVisibility(View.GONE);
				showMapButton.setVisibility(View.VISIBLE);
			}
		});
		/**
		 * shows map
		 */
		//attach the show map listener
		showMapButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mapLayout.setVisibility(View.VISIBLE);
				itineraryLayout.setVisibility(View.GONE);
				showMapButton.setVisibility(View.GONE);
				showItineraryButton.setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * This will set up a basic MapQuest map with zoom controls
	 */
	protected void setupMapView(GeoPoint pt, int zoom) {
		this.map = (MapView) findViewById(R.id.map);

		// set the zoom level
		map.getController().setZoom(zoom);

		// set the center point
		map.getController().setCenter(pt);

		// enable the zoom controls
		map.setBuiltInZoomControls(true);

	}
	public GeoPoint returnMyLocationYOCO()
	{
		return  myLocationOverlay.getMyLocation();
	}
	/**
	 * This will turn on the GPS and move the map to wherever the user is and create a geopoint at said point
	 */
	protected void setupMyLocation() {
		this.myLocationOverlay = new MyLocationOverlay(this, map);

		myLocationOverlay.enableMyLocation();
		myLocationOverlay.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				GeoPoint currentLocation = myLocationOverlay.getMyLocation(); 
				map.getController().animateTo(currentLocation);
				map.getController().setZoom(14);
				map.getOverlays().add(myLocationOverlay);
				myLocationOverlay.setFollowing(true);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.routewithstopover, menu);
		return true;
	}

}
