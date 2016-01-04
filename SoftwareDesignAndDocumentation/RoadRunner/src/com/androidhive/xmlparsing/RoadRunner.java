package com.androidhive.xmlparsing;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class RoadRunner extends Activity {

		//Initialize our variables
		private SeekBar bar;
		String start=null;
		String end=null;
		String string=null;
		int points=0;
		public static int ror_value;
		public static int ros_value;
		public static boolean is_eat_and_drink;
		public static boolean is_see_and_do;
		public static boolean is_fuel_up;
		public static boolean is_rest_and_empty;
		public static boolean is_sleep;
		public static int seekbar_value;
		Bundle geoPoints=new Bundle();
		Bundle searchTerms=new Bundle();
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_road_runner);
			Bundle bundle=getIntent().getExtras();
			string=bundle.getString("stuff");
			start=bundle.getString("start");
			end=bundle.getString("end");
			points=bundle.getInt("points");
			geoPoints=bundle;
			RadioButton default_1 = (RadioButton) findViewById(R.id.radio_5);
			ror_value=5;
			RadioButton default_2 = (RadioButton) findViewById(R.id.radio_50);
			ros_value=50;
			default_1.setChecked(true);
			default_2.setChecked(true);
		}


		//so the state of each object is saved and displayed
		protected void onStart() {
			super.onStart();  // Always call the superclass method first

			CheckBox eat_and_drink = (CheckBox) findViewById(R.id.checkbox1);
			if(is_eat_and_drink) {
				eat_and_drink.setChecked(true);
			}
			CheckBox see_and_do = (CheckBox) findViewById(R.id.checkbox2);
			if(is_see_and_do) {
				see_and_do.setChecked(true);
			}
			CheckBox fuel_up = (CheckBox) findViewById(R.id.checkbox3);
			if(is_fuel_up) {
				fuel_up.setChecked(true);
			}
			CheckBox rest_and_empty = (CheckBox) findViewById(R.id.checkbox4);
			if(is_rest_and_empty) {
				rest_and_empty.setChecked(true);
			}
			CheckBox sleep = (CheckBox) findViewById(R.id.checkbox5);
			if(is_sleep) {
				sleep.setChecked(true);
			}
			RadioButton ror_1 = (RadioButton)findViewById(R.id.radio_1);
			RadioButton ror_5 = (RadioButton)findViewById(R.id.radio_5);
			RadioButton ror_10 = (RadioButton)findViewById(R.id.radio_10);
			RadioButton ros_10 = (RadioButton)findViewById(R.id.radio_10_2);
			RadioButton ros_50 = (RadioButton)findViewById(R.id.radio_50);
			RadioButton ros_100 = (RadioButton)findViewById(R.id.radio_100);
			if(ror_value==1) {
				ror_1.setChecked(true);
			}
			if(ror_value==5) {
				ror_5.setChecked(true);
			}
			if(ror_value==10) {
				ror_10.setChecked(true);
			}
			if(ros_value==10) {
				ros_10.setChecked(true);
			}
			if(ros_value==50) {
				ros_50.setChecked(true);
			}
			if(ros_value==100) {
				ros_100.setChecked(true);
			}

		}
		//for saving the specific search
		public void getSearchTerm (View view) {
			//Get specific search term
			EditText text = (EditText)findViewById(R.id.search_text);
			String value = text.getText().toString();
			//save value for when calling search

		}
		//goes to map
		public void Map(View view){
			Intent myIntent = new Intent(this, Route.class);
			startActivity(myIntent);
		}
		//saves the chosen options in a string so it can be passed to the next window
		public String createResultsString(){
			EditText text = (EditText)findViewById(R.id.search_text);
			String value = text.getText().toString();
			int hastext=0;//to see if user put text
			if(value.length()!=0)
			{
				hastext=1;
			}
			String string="";
			string=ror_value+","+ros_value+","+is_eat_and_drink+","+is_see_and_do+","+is_fuel_up+","+is_rest_and_empty+","+is_sleep+","+hastext+","+value;
			return string;
		}
		//move on to the results and search string
		public void displaySearchListView(View view){
			Intent myIntent=new Intent(this,AndroidXMLParsingActivity.class);
			String string2=createResultsString();
			//get the amount of points we are dending within the range of search
			Bundle bundle2=new Bundle();
			bundle2.putString("start",start);
			bundle2.putString("end", end);
			bundle2.putInt("points",points);
			bundle2.putString("search",string2);
			bundle2.putString("geopoints", string);
			myIntent.putExtras(bundle2);
			startActivity(myIntent);
		}
		//for the options to be displayed
		public void onRoRClicked(View view) {
			// Is the button now checked?
			boolean checked = ((RadioButton) view).isChecked();

			// Check which radio button was clicked
			switch(view.getId()) {
			case R.id.radio_1:
				if (checked)
					// one mile is chosen
					ror_value=1;
				break;
			case R.id.radio_5:
				if (checked)
					// 5 miles is chosen
					ror_value=5;
				break;
			case R.id.radio_10:
				if(checked)
					// 10 miles is chosen
					ror_value=10;
				break;

			}
		}

		public void onRoSClicked(View view) {
			// Is the button now checked?
			boolean checked = ((RadioButton) view).isChecked();

			// Check which radio button was clicked
			switch(view.getId()) {
			case R.id.radio_10_2:
				if (checked)
					// 10 miles is chosen
					ros_value=10;
				break;
			case R.id.radio_50:
				if (checked)
					// 50 miles is chosen
					ros_value=50;
				break;
			case R.id.radio_100:
				if (checked)
					// 100 miles is chosen
					ros_value=100;
				break;
			}
		}
		//resets everything
		public void clearSettings(View view) {
			//Set all 5 checkboxes to unchecked if they're not already
			CheckBox cb1 = (CheckBox) findViewById(R.id.checkbox1);
			if (cb1.isChecked()) {
				cb1.setChecked(false);
			}
			CheckBox cb2 = (CheckBox) findViewById(R.id.checkbox2);
			if (cb2.isChecked()) {
				cb2.setChecked(false);
			}
			CheckBox cb3 = (CheckBox) findViewById(R.id.checkbox3);
			if (cb3.isChecked()) {
				cb3.setChecked(false);
			}
			CheckBox cb4 = (CheckBox) findViewById(R.id.checkbox4);
			if (cb4.isChecked()) {
				cb4.setChecked(false);
			}
			CheckBox cb5 = (CheckBox) findViewById(R.id.checkbox5);
			if (cb5.isChecked()) {
				cb5.setChecked(false);
			}
			//set default radio button values
			RadioButton ror_1 = (RadioButton)findViewById(R.id.radio_1);
			ror_1.setChecked(false);
			RadioButton ror_5 = (RadioButton)findViewById(R.id.radio_5);
			ror_5.setChecked(true);
			RadioButton ror_10 = (RadioButton)findViewById(R.id.radio_10);
			ror_10.setChecked(false);
			RadioButton ros_10 = (RadioButton)findViewById(R.id.radio_10_2);
			ros_10.setChecked(false);
			RadioButton ros_50 = (RadioButton)findViewById(R.id.radio_50);
			ros_50.setChecked(true);
			RadioButton ros_100 = (RadioButton)findViewById(R.id.radio_100);
			ros_100.setChecked(false);
		}
		//for the checkboxes
		public void onCheckboxClicked(View view) {
			// Is the view now checked?
			boolean checked = ((CheckBox) view).isChecked();

			// Check which checkbox was clicked
			switch(view.getId()) {
			case R.id.checkbox1:
				if (checked)
					is_eat_and_drink=true;
				// eat and drink
				break;
			case R.id.checkbox2:
				if (checked)
					is_see_and_do=true;
				// see and do
				break;
			case R.id.checkbox3:
				if (checked)
					is_fuel_up=true;
				// fuel up
				break;
			case R.id.checkbox4:
				if (checked)
					is_rest_and_empty=true;
				// rest and empty
				break;
			case R.id.checkbox5:
				if (checked)
					is_sleep=true;
				// sleep
				break;
			}
		}
		public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
		    double earthRadius = 3958.75;
		    double dLat = Math.toRadians(lat2-lat1);
		    double dLng = Math.toRadians(lng2-lng1);
		    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		               Math.sin(dLng/2) * Math.sin(dLng/2);
		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		    double dist = earthRadius * c;

		    int meterConversion = 1609;

		    return new Float(dist * meterConversion).floatValue();
		    }

	}