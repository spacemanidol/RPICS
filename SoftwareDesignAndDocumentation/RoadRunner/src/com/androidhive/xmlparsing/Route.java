package com.androidhive.xmlparsing;

import java.util.ArrayList;
import java.util.List;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.RouteManager;
import com.mapquest.android.maps.RouteResponse;
import com.mapquest.android.maps.RouteResponse.Route.Leg;
import com.mapquest.android.maps.RouteResponse.Route.Leg.Maneuver;
import com.mapquest.android.maps.ServiceResponse.Info;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Route extends SimpleMap {

	String startEnd=null;
	String endEnd=null;
	int amountPoints=0;
	boolean previous_route;
	public final static String STOP_POINTS="com.example.test.GEOPOINTS";
	protected int getLayoutId() {
		return R.layout.activity_route;
	}
	String itinerary;
	String intentinfo;
	String points=null;
	RouteManager routeManager=new RouteManager(this);
	String startentered;
	String endentered;
	List<GeoPoint>routePoints = null;
	List<String>stopPoints=null;
	List<Leg> legs=null;

	/**
	 * This is all the stuff that loads all the elements in the XML, 
	 * populates them, creates the route and makes the map buttons changed based upon the situations
	 * 
	 */
	@Override
	protected void init() {
		super.init();
		//find the objects we need to interact with
			final MapView mapView = (MapView)findViewById(R.id.map);
			final WebView itinerary=(WebView)findViewById(R.id.itinerary);
			final Button MyLocationButton=(Button)findViewById(R.id.MyLocationButton);			
			final RelativeLayout mapLayout=(RelativeLayout)findViewById(R.id.mapLayout);
			final RelativeLayout itineraryLayout=(RelativeLayout)findViewById(R.id.itineraryLayout);
			final Button createRouteButton=(Button)findViewById(R.id.createRouteButton);
			final Button showItineraryButton=(Button)findViewById(R.id.showItineraryButton);
			final Button showMapButton=(Button)findViewById(R.id.showMapButton);
			final Button clearButton=(Button)findViewById(R.id.clearButton);
			final EditText points=(EditText)findViewById(R.id.routepoints);
			final EditText start=(EditText)findViewById(R.id.startTextView);
			final EditText end=(EditText)findViewById(R.id.endTextView);
			final RouteManager routeManager=new RouteManager(this);
			final RouteResponse routeResponse=new RouteResponse();
			routeManager.setMapView(mapView);
			routeManager.setItineraryView(itinerary);
			routeManager.setDebug(true);
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
					createRouteButton.setEnabled(true);
				}

				/**
				 * create route succeds
				 */
				@SuppressWarnings("null")
				@Override
				public void onSuccess(RouteResponse routeResponse) {
					clearButton.setVisibility(View.VISIBLE);
					//Change buttons
					if(showItineraryButton.getVisibility()==View.GONE &&
							showMapButton.getVisibility()==View.GONE){
						showItineraryButton.setVisibility(View.VISIBLE);
					}
					//save all the points along route so these points can be used later during search
					legs=routeResponse.route.legs;
					final List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
					final List<String> geoPointsCoordinates = new ArrayList<String>();

					List<Maneuver>maneuvers=legs.get(0).maneuvers;
					for(int i=0;i<maneuvers.size();i++)
					{
						GeoPoint gp=maneuvers.get(i).startPoint;
						geoPoints.add(gp);
						geoPointsCoordinates.add(gp.getLatitude()+","+gp.getLongitude()+",");
					}
					geoPointsCoordinates.add("end");
					routePoints=geoPoints;
					stopPoints=geoPointsCoordinates;
					amountPoints=routePoints.size();
					StringBuilder message =new StringBuilder();
					message.append("Points added\n")
					.append(routePoints.size()*100);
					Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_LONG).show();
					createRouteButton.setEnabled(true);
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
			/**
			 * clears the route
			 */
			//attach the clear route listener
			clearButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					routeManager.clearRoute();
					clearButton.setVisibility(View.GONE);
					showItineraryButton.setVisibility(View.GONE);
					showMapButton.setVisibility(View.GONE);
					mapLayout.setVisibility(View.VISIBLE);
					itineraryLayout.setVisibility(View.GONE);
				}
			});
			/**
			 * create the route
			 */
			//create an onclick listener for the instructional text
			createRouteButton.setOnClickListener(new View.OnClickListener() {
				@SuppressWarnings("null")
				@Override
				public void onClick(View view) {	
					hideSoftKeyboard(view);
					createRouteButton.setEnabled(false);
					String startAt=getText(start);
					String endAt=getText(end);
					startentered=getText(start);
					endentered=getText(end);
					startEnd=getText(start);
					endEnd=getText(end);
					routeManager.createRoute(startAt, endAt);
				}
			});
			/**
			 * sets the start textbox to the location in latitude and long
			 */
			MyLocationButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					GeoPoint gp =returnMyLocationYOCO();
					double latitude=gp.getLatitude();
					double longitude=gp.getLongitude();
					start.setText(latitude+","+longitude);
				}
			});
		}
		@Override
		public boolean isRouteDisplayed() {
			return true;
		}
		/**
		 * upon click it goes over to the Road Runner window
		 * @param view
		 */
		public void Map(View view) {
			//Perform search
			Intent myIntent = new Intent(this, RoadRunner.class);
			String string="";
			Bundle bundle=new Bundle();
			for(int i=0;i<stopPoints.size();i++)
			{

				string=string+(stopPoints.get(i));
			}
			bundle.putString("stuff", string);
			bundle.putString("start",startEnd);
			bundle.putString("end", endEnd);
			bundle.putInt("points",amountPoints);
			myIntent.putExtras(bundle);
			startActivity(myIntent);
		}
	}