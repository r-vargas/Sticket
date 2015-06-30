package com.example.roberto.sticket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.ChallengeScheme;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class MainActivity extends Activity {

//    TextView jsonOutput = (TextView) findViewById(R.id.jsonoutput);
    private final static String BASE_URL = "https://its.navizon.com/api/v1/";

    private final static String SITE_ID = "1450";                       // Your site ID here
    private final static String STATION_MAC = "4C:8D:79:E1:00:EE";      // The station's MAC address
    private final static String USERNAME = "username";                  // Your username
    private final static String PASSWORD = "password";                  // Your password
    private final static int numDevices = 10;
    private final static int OFF = 0;
    private HashMap<Integer, Device> sticketMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setTitle("Manutencoop Sticket");
        TextView textView = (TextView) findViewById(R.id.username);
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        new HttpRequestTask().execute();
        Button button = (Button) findViewById(R.id.report);
        button.setBackgroundColor(Color.TRANSPARENT);


       // if (username.length() == 0){ username = "Guest";}
        //textView.setText(username);
/*
        Button button = (Button) findViewById(R.id.logout);
        button.setBackgroundColor(Color.TRANSPARENT);

        Button button1 = (Button) findViewById(R.id.map);
        button1.setBackgroundColor(Color.TRANSPARENT);

        Button button2 = (Button) findViewById(R.id.listStickets);
        button2.setBackgroundColor(Color.TRANSPARENT);

        Button button3 = (Button) findViewById(R.id.myStickets);
        button3.setBackgroundColor(Color.TRANSPARENT);

        Button button4 = (Button) findViewById(R.id.report);
        button4.setBackgroundColor(Color.TRANSPARENT);
        */

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gpsButton(View V) throws IOException, JSONException {
        // Set the request parameters
        String url = BASE_URL + "sites/" + SITE_ID + "/stations/" + STATION_MAC + "/";
        ClientResource itsClient = new ClientResource(url);
        itsClient.setChallengeResponse(ChallengeScheme.HTTP_BASIC, USERNAME, PASSWORD);

        // Retrieve and parse the JSON representation
        JsonRepresentation jsonRep = new JsonRepresentation(itsClient.get());
        JSONObject jsonObj = jsonRep.getJsonObject();

        // Output results
        System.out.printf("Station: %s\n", jsonObj.getString("mac"));
        System.out.printf("loc.lat: %.6f\n", jsonObj.getJSONObject("loc").getDouble("lat"));
        System.out.printf("loc.lng: %.6f\n", jsonObj.getJSONObject("loc").getDouble("lng"));
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    /*
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            //jsonOutput.setText(result);
        }
    }
    */

    public void onClick(View v) {
        Intent intent;
        intent = new Intent(this,reportActivity.class);
        intent.putExtra("USERNAME",USERNAME);
        //if (v.getId() == R.id.map){
           // Intent i = new Intent(this,SticketMapActivity.class);
            //startActivity(i);
        //} else
        startActivity(intent);

        /*
        switch (v.getId()) {
            case R.id.myStickets:
                //intent = new Intent(this,ViewSticket.class);
                //startActivity(intent);
                break;
            case R.id.listStickets:
                intent = new Intent(this,ListSticketsActivity.class);
                startActivity(intent);
                break;
            //case R.id.allStickets:
              //  intent = new Intent(this,ListSticketsActivity.class);
                //startActivity(intent);
                //break;
            case R.id.report:
                intent = new Intent(this,reportActivity.class);
                intent.putExtra("USERNAME",USERNAME);
                startActivity(intent);
                break;
            case R.id.map:
                intent i = new Intent(this,SticketMapActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
        */
    }

    public void listStickets(View v){
        Intent intent = new Intent(this,ListSticketsActivity.class);
        startActivity(intent);
    }

    public int gpsToScreen(double xPos, double yPos, int output, double layoutWidth, double layoutHeight){
        Log.d("xPos", Integer.toString((int) xPos));
        Log.d("yPos", Integer.toString((int) yPos));
        double shiftedY = -1 * (((yPos * 1000000) - 37420000) - 6550);
        double shiftedX = -1 * ((xPos * 1000000) + 122170000) - 2126;
        Log.d("shiftedxPos", Integer.toString((int) shiftedX));
        Log.d("shiftedyPos", Integer.toString((int) shiftedY));
        double theta = 11.76;
        double thetaInRads = theta * 2 * Math.PI / 360;
        double xOffsetRaw = (((Math.cos(thetaInRads) * shiftedX) - (Math.sin(thetaInRads) * shiftedY)));///(598.56))*layoutWidth;
        double yOffsetRaw = (((Math.sin(thetaInRads) * shiftedX) + (Math.cos(thetaInRads) * shiftedY)));///(122))*layoutHeight;
        double maxXOffset = 598.56;
        double maxYOffset = 361.13;
        double xOffset = (xOffsetRaw/maxXOffset)*layoutWidth;
        double yOffset = (yOffsetRaw/maxYOffset)*layoutHeight;
        Log.d("XdoubleOffset", Double.toString(xOffset));
        Log.d("YdoubleOffset", Double.toString(yOffset));
        if (output == 0) {
            return (int) yOffset;
        } else if (output == 1) {
            if (xOffset < 0) {
                return (int) -xOffset;
            } else {
                return (int) xOffset;
            }
        } else {
            return -1;
        }

    /*        Log.d("xPos",Integer.toString((int) xPos));
        Log.d("yPos",Integer.toString((int) yPos));
        double shiftedY = -1*(((yPos*1000000)-37420000)-6550);
        double shiftedX = -1*((xPos*1000000)+122170000)-2126;
        Log.d("shiftedxPos",Integer.toString((int) shiftedX));
        Log.d("shiftedyPos",Integer.toString((int) shiftedY));
        double theta = 11.76;
        double thetaInRads = theta*2*Math.PI/360;
        double xOffset = (((Math.cos(thetaInRads)*shiftedX)-(Math.sin(thetaInRads)*shiftedY)));///(598.56))*layoutWidth;
        double yOffset = (((Math.sin(thetaInRads)*shiftedX)+(Math.cos(thetaInRads)*shiftedY)));///(122))*layoutHeight;
        Log.d("XdoubleOffset",Double.toString(xOffset));
        Log.d("YdoubleOffset",Double.toString(yOffset));
        if (output == 0){

            return Math.abs((int)xOffset);
        } else if (output == 1){
            return Math.abs((int)yOffset);
        } else {
            return -1;
        }
        */
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, sticketResponse> {
        @Override
        protected sticketResponse doInBackground(Void... params) {
            try {
                final String url = "http://192.168.0.200/api_v1/index.php/mobile/stickets"; // "http://federicopiccinno.it/sugar_stickets/api_v1/index.php/mobile/stickets"; //
                RestTemplate restTemplate = new RestTemplate();
                Log.d("tag","it's doing http shit");
                //restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                //String result = restTemplate.getForObject(url, String.class, "Android");
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                sticketResponse response = restTemplate.getForObject(url, sticketResponse.class);
                Log.d("got response", "got response");
                return response;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(sticketResponse response) {
            Log.d("wooter u doon","wooter u doon");
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_layout);
            sticketMap = new HashMap<Integer, Device>();
            Device[] devices = response.getDevices();
            Integer[] sticketIDs = new Integer[numDevices];
            if (response != null) {
                for (int i = 0; i < devices.length; i++) {
                    Device currentSticket = devices[i];
                    sticketIDs[i] = currentSticket.getSticket_id();
                    Log.d("current_sticketID",Integer.toString(currentSticket.getSticket_id()));
                    sticketMap.put(currentSticket.getSticket_id(), currentSticket);
                    Log.d("Size of hashmap", Integer.toString(sticketMap.size()));
                }
                Log.d("sticketIDs",sticketIDs.toString());
            }
            for (int j = 0; j < devices.length; j++) {
                Device sticket = sticketMap.get(sticketIDs[j]);

                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.buttons);
                int count = linearLayout.getChildCount();
                for (int i = 0; i < count; i++) {
                    final Button sticketButton = (Button) linearLayout.getChildAt(i);
                    sticketButton.setTextColor(Color.TRANSPARENT);
                    sticketButton.setVisibility(View.VISIBLE);
                    sticketButton.setTextSize(1);
                    if (sticketButton.getTag().toString().equals(Integer.toString(sticketIDs[j]))&& sticket.getTicket_status()>1) {
                        sticketButton.setId(j);
                        sticketButton.setText("taken");
                        //sticketButton.setText(Integer.toString(sticket.getSticket_id()));
                        sticketButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(), ViewSticket.class);
                                Device sticket = sticketMap.get(Integer.parseInt(sticketButton.getTag().toString()));
                                i.putExtra("TICKET_ID", Integer.toString(sticket.getTicket_id()));
                                i.putExtra("Y", Double.toString(sticket.getIndoorlocation().getLoc().getLat()));
                                i.putExtra("X", Double.toString(sticket.getIndoorlocation().getLoc().getLng()));
                                Integer.toString(sticket.getTicket_id());
                                startActivity(i);
                            }
                        });

                    } else {
                        // sticketButton.setBackgroundColor(Color.TRANSPARENT);
                    }

                }

            }
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.buttons);
            int count = linearLayout.getChildCount();
            for (int i = 0; i < count; i++) {
                final Button sticketButton = (Button) linearLayout.getChildAt(i);
                if (!sticketButton.getText().equals("taken")) {//sticketIDs.toString().contains(sticketButton.getTag().toString())){
                    sticketButton.setBackgroundColor(Color.TRANSPARENT);
                }
            }
                /*
                final Button sticketButton = new Button(getApplicationContext());


                int screenX = 0;
                int screenY = 0;
                double x = sticket.getIndoorlocation().getLoc().getLng();
                double y = sticket.getIndoorlocation().getLoc().getLat();
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.mapWrapper);
                screenX = gpsToScreen(x, y, 1, rl.getWidth(), rl.getHeight());
                screenY = gpsToScreen(x, y, 0, rl.getWidth(), rl.getHeight());
                //ImageView marker1 = new ImageView(getApplicationContext());
                //marker1.setImageResource(R.drawable.marker_1);
                //marker1.setMaxHeight(60);
                //marker1.setMaxWidth(60);
                //ImageView iv = marker1;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(60, 60);
                params.leftMargin = screenX;
                params.topMargin = screenY;
                Log.d("Xoffset", Integer.toString(screenX));
                Log.d("Yoffset", Integer.toString(screenY));
                rl.addView(sticketButton, params);
                */




        }
    }
}


