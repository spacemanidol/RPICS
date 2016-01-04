package com.androidhive.xmlparsing;

import android.content.Intent;
import android.os.Bundle;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;
import com.mapquest.android.maps.RouteManager;

public class SingleMenuItemActivity  extends MapActivity {
	protected MapView map;
	public MyLocationOverlay myLocationOverlay;
	// XML node keys
	static final String KEY_NAME = "name";
	static final String KEY_LAT="lat";
	static final String KEY_LNG = "lng";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_list_item);

		// getting intent data
		Intent in = getIntent();

		// Get XML values from previous intent
		String name = in.getStringExtra(KEY_NAME);
		String lat = in.getStringExtra(KEY_LAT);
		String lng = in.getStringExtra(KEY_LNG);
		String end=lat+","+lng;
		GeoPoint startpoint=myLocationOverlay.getMyLocation();
		double lat_start=startpoint.getLatitude();
		double lng_start=startpoint.getLongitude();
		String start=lat_start+","+lng_start;
		init();
		//displayRoute(start,end);

	}
	protected void init() {
		this.setupMapView(new GeoPoint(38.0,-104.0), 5);
		setupMyLocation();
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

	// create a route and display on the map 
	private void displayRoute(String start, String end) {
		RouteManager routeManager = new RouteManager(this);
		routeManager.setMapView(map);
		routeManager.createRoute(start, end);
	}
	
	@Override
	public boolean isRouteDisplayed() {
		return true;
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
	/**
	 * returns the users location in the form of a geopoint
	 * @return
	 */
	public GeoPoint returnMyLocationYOCO()
	{
		return  myLocationOverlay.getMyLocation();
	}
}
