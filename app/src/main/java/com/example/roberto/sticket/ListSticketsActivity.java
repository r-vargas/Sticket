package com.example.roberto.sticket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.HashMap;


public class ListSticketsActivity extends Activity {
    private HashMap<Integer,Device> sticketMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stickets);
        Button button8 = (Button) findViewById(R.id.fillSticket);
        button8.setBackgroundColor(Color.TRANSPARENT);
        // Get list of all stickets from server and parse JSON output

        // The connection URL
       // String url = "https://ajax.googleapis.com/ajax/" +
              //  "services/search/web?v=1.0&q={query}";

// Create a new RestTemplate instance
      //  RestTemplate restTemplate = new RestTemplate();

// Add the String message converter
       // restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

// Make the HTTP GET request, marshaling the response to a String
       // String result = restTemplate.getForObject(url, String.class, "Android");

    }

    @Override
    protected void onStart() {
        super.onStart();
        new HttpRequestTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_stickets, menu);
        getActionBar().setTitle("Manutencoop Sticket");
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

    private class HttpRequestTask extends AsyncTask<Void, Void, sticketResponse> {
        @Override
        protected sticketResponse doInBackground(Void... params) {
            try {
                final String url = "http://federicopiccinno.it/sugar_stickets/api_v1/index.php/mobile/stickets"; // "http://rest-service.guides.spring.io/greeting";
                RestTemplate restTemplate = new RestTemplate();
                //restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                //String result = restTemplate.getForObject(url, String.class, "Android");
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                sticketResponse response = restTemplate.getForObject(url, sticketResponse.class);
                return response;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(sticketResponse response) {
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_layout);
            sticketMap = new HashMap<Integer, Device>();
            Device[] devices = response.getDevices();
            Integer[] sticketIDs = new Integer[10];
            if (response != null) {
                for (int i = 0; i < devices.length; i++) {
                    Device currentSticket = devices[i];
                    sticketIDs[i] = currentSticket.getSticket_id();
                    sticketMap.put(currentSticket.getSticket_id(), currentSticket);
                    Log.d("Size of hashmap", Integer.toString(sticketMap.size()));
                }
            }
            for (int j = 0; j < devices.length; j++) {
                    final Device sticket = sticketMap.get(sticketIDs[j]);
                    Button sticketButton = new Button(getApplicationContext());
                    sticketButton.setId(j);
                    sticketButton.setTag(Integer.toString(sticket.getTicket_id()));
                    sticketButton.setText("See Sticket: "+Integer.toString(sticket.getSticket_id()));
                sticketButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button sticketButton = (Button) view;
                        Intent i = new Intent(getApplicationContext(),ViewSticket.class);
                        i.putExtra("TICKET_ID", (sticketButton.getTag().toString()));
                        i.putExtra("Y",Double.toString(sticket.getIndoorlocation().getLoc().getLat()));
                        i.putExtra("X",Double.toString(sticket.getIndoorlocation().getLoc().getLng()));
                        startActivity(i);
                    }
                });
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, 150);
                params.leftMargin = 220 + j*480;
                params.topMargin = 300;
                layout.addView(sticketButton, params);


            }
        }
    }
/*
        boolean first = true;
        result=result.substring(result.indexOf(":")+1);
        while(result.indexOf("id")!=-1)

        {
            if (!first) {
                result = result.substring(result.indexOf("id"));
            }
            Sticket sticket = new Sticket();
            int id = Integer.parseInt(result.substring(result.indexOf(":") + 2, result.indexOf(":") + 3));
            sticket.setSticket_id(id);
            result = result.substring(result.indexOf(":") + 1);
            result = result.substring(result.indexOf(":") + 2);
            Log.d("wtf", result);
            String MACaddress = result.substring(0, result.indexOf("\""));
            sticket.setMac_address(MACaddress);
            result = result.substring(result.indexOf(",\""));
            result = result.substring(result.indexOf(":") + 2);
            Log.d("wtf", result);
            int ticketID = Integer.parseInt(result.substring(0, 1));
            sticket.setTicket_id(ticketID);
            result = result.substring(result.indexOf(":") + 2);
            Log.d("wtf", result);
            int powerOn = Integer.parseInt(result.substring(0, 1));
            result = result.substring(result.indexOf(":") + 2);
            Log.d("wtf", result);
            int sticketID = Integer.parseInt(result.substring(0, 1));
            result = result.substring(result.indexOf(":") + 2);
            Log.d("wtf", result);
            if (id == 1) {
                int ticketStatus = Integer.parseInt(result.substring(0, 1));
                sticket.setTicket_status(ticketStatus);
            }
            sticketMap.put((Integer) sticketID, sticket);
        }*/

        //TextView sticketList = (TextView) findViewById(R.id.sticketList);
        //Integer keyInt = 1;
        //String output = sticketMap.get(keyInt).outputString();
        //sticketList.setText(output);


    public void OnClick(View v) {
        Button sticketButton = (Button) v;
        Intent i = new Intent(this,ViewSticket.class);
        i.putExtra("TICKET_ID", Integer.parseInt(sticketButton.getText().toString()));
    }
}



