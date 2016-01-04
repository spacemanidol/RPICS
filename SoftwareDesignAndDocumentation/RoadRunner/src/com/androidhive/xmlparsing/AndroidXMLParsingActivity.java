package com.androidhive.xmlparsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidXMLParsingActivity extends ListActivity {
	// All static variables
	// XML node keys
	static final String KEY_ITEM = "result"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_NAME = "name";
	static final String KEY_LAT = "lat";
	static final String KEY_LNG = "lng";
	static final String eat_and_drink2="food";
	static final String see_and_do2="amusement_park|aquarium|art_gallery|bowling_alley|casino|movie_rental|movie_theater|museum|park|stadium";
	static final String fuel_up2="gas_station";
	static final String sleep2="campground|rv_park|lodging";
	static final String rest_and_empty2="bar|bus_station|gas_station|park";
	int ror;
	List<String> urlToSearch = new ArrayList<String>();

	int ros;
	boolean eat_and_drink;
	boolean see_and_do;
	boolean fuel_up;
	boolean rest_and_empty;
	boolean sleep;
	int is_specific;
	String specific;
	String passto;
	private static final String API_KEY ="AIzaSyBnTClFK-KRkAr-ZoCxJuLGIK5NuSfgj04";
	//converts a string to a boolean because the elements passed from other intents come from strings
	public boolean convertStringToBoolean(String string)
	{
		char[] chars = string.toCharArray();
		String test="t";
		char[] chair=test.toCharArray();
		if(chars[0]==chair[0])
		{
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Bundle bundle=getIntent().getExtras();
		String start=bundle.getString("start");
		String end=bundle.getString("end");
		int stopPoints=bundle.getInt("points");
		String stuff= bundle.getString("geopoints");//string of geopoints
		String search=bundle.getString("search");//string of road runner search parameters
		String [] tokens=search.split(",");
		String [] points=stuff.split(",");
		List<String> list= Arrays.asList(points);
		ror=Integer.parseInt( tokens[0]);
		String temp=null;
		ror=ror*1000;//turn to meters
		ros=Integer.parseInt( tokens[1]);
		eat_and_drink=convertStringToBoolean(tokens[2]);
		see_and_do=convertStringToBoolean(tokens[3]);
		fuel_up=convertStringToBoolean(tokens[4]);
		rest_and_empty=convertStringToBoolean(tokens[5]);
		sleep=convertStringToBoolean(tokens[6]);
		is_specific=Integer.parseInt(tokens[7]);
		if(is_specific!=0)
		{
			specific=tokens[8];
		}
		String initiallat=list.get(0);
		String initiallng=list.get(1);
		int k=0;
		for(int i=0;i<stopPoints;i++)
		{

			String latitude=list.get(k);
			k++;
			String longitude=list.get(k);
			k++;
			temp="https://maps.googleapis.com/maps/api/place/nearbysearch/xml?location="+latitude+","+longitude+"&radius="+ror;
			//specific item so added
			if(is_specific>0)
			{

				temp+="&sensor=false&name="+specific;
				temp+="&key=AIzaSyBnTClFK-KRkAr-ZoCxJuLGIK5NuSfgj04";
			}
			else{
				temp+="&types=";
				boolean is_prev=false;
				if(eat_and_drink)
				{
					if(is_prev==true)//not first option
					{
						temp+="|";
						temp+=eat_and_drink2;
					}
					else{
						temp+=eat_and_drink2;
						is_prev=true;}
				}
				if(see_and_do)
				{
					if(is_prev==true)//not first option
					{
						temp+="|";
						temp+=see_and_do2;
					}
					else{
						temp+=see_and_do2;
						is_prev=true;
					}
				}
				if(fuel_up)
				{
					if(is_prev==true)//not first option
					{
						temp+="|";
						temp+=fuel_up2;
					}
					else{
						temp+=fuel_up2;
						is_prev=true;
					}
				}
				if(rest_and_empty)
				{
					if(is_prev==true)//not first option
					{
						temp+="|";
						temp+=rest_and_empty2;
					}
					else{temp+=rest_and_empty2;
					is_prev=true;
					}
				}
				if(sleep)
				{

					if(is_prev==true)//not first option
					{
						temp+="|";
						temp+=sleep2;
					}
					else{temp+=sleep2;
					is_prev=true;
					}
				}
				temp+="&sensor=false&key=AIzaSyBnTClFK-KRkAr-ZoCxJuLGIK5NuSfgj04";
			}
			urlToSearch.add(temp);
		}
		
		ArrayList<HashMap<String, String>> stops = new ArrayList<HashMap<String, String>>();
		
		for(int h=0;h<urlToSearch.size();h++)
		{
			String URL=urlToSearch.get(h);
			XMLParser parser = new XMLParser();
			String xml = parser.getXmlFromUrl(URL); // getting XML
			Document doc = parser.getDomElement(xml); // getting DOM element

			NodeList nl = doc.getElementsByTagName(KEY_ITEM);
			String name=null;
			String lat=null;
			String lng=null;
			// looping through all item nodes <item>
			for (int j = 0; j < nl.getLength(); j++) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				Element e = (Element) nl.item(j);
				// adding each child node to HashMap key => value
				name=parser.getValue(e, KEY_NAME);
				lng=parser.getValue(e, KEY_LNG);
				lat=parser.getValue(e, KEY_LAT);
				map.put(KEY_ID, parser.getValue(e, KEY_ID));
				map.put(KEY_NAME, name);
				map.put(KEY_LNG,lng);
				map.put(KEY_LAT, lat);

				// adding HashList to ArrayList
				stops.add(map);
				
			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(KEY_ID, "result");
			map.put(KEY_NAME,h+ "mile");
			
			
		}
		
		
		// Adding menuItems to ListView
		ListAdapter adapter = new SimpleAdapter(this, stops,
				R.layout.list_item,
				new String[] { KEY_NAME }, new int[] {
				R.id.name});

		setListAdapter(adapter);

		// selecting single ListView item
		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
				String lat=((TextView) view.findViewById(R.id.lat)).getText().toString();
				String lng=((TextView) view.findViewById(R.id.lng)).getText().toString();
				StringBuilder message =new StringBuilder();
				message.append(lat)
				.append(",").append(lng);
				Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_LONG).show();
				
				// Starting new intent
				Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
				in.putExtra(KEY_NAME, name);
				in.putExtra(KEY_LAT, lat);
				in.putExtra(KEY_LNG, lng);
				startActivity(in);

			}
		});
	}
	public void Map(View view){
		Intent myIntent = new Intent(this, RoadRunner.class);
		startActivity(myIntent);
	}

}