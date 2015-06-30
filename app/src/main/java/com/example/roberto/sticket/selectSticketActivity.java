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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;


public class selectSticketActivity extends Activity {
    private int category;
    private String username;
    private HashMap<Integer, Device> sticketMap;
    private final static int numDevices = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sticket);
        Intent intent = getIntent();
        category = intent.getIntExtra("CATEGORY",-1);
        username = intent.getStringExtra("USERNAME");
        getActionBar().setTitle("Manutencoop Sticket");
        Button button = (Button) findViewById(R.id.one);
        button.setBackgroundColor(Color.TRANSPARENT);
        Button button1 = (Button) findViewById(R.id.two);
        button1.setBackgroundColor(Color.TRANSPARENT);
        Button button2 = (Button) findViewById(R.id.three);
        button2.setBackgroundColor(Color.TRANSPARENT);
        Button button3 = (Button) findViewById(R.id.four);
        button3.setBackgroundColor(Color.TRANSPARENT);
        Button button4 = (Button) findViewById(R.id.five);
        button4.setBackgroundColor(Color.TRANSPARENT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_sticket, menu);
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

    public void onClick(View v){
        Button button = (Button) v;
        int sticket_id = Integer.parseInt((String)button.getTag());
        Intent intent = new Intent(this,cameraPreviewActivity.class);   //TicketCreationActivity.class);
        intent.putExtra("STICKET_ID", sticket_id);
        intent.putExtra("CATEGORY",category);
        intent.putExtra("USERNAME",username);
        startActivity(intent);
    }

/*
    private class HttpRequestTask extends AsyncTask<Void, Void, sticketResponse> {
        @Override
        protected sticketResponse doInBackground(Void... params) {
            try {
                final String url = "http://192.168.0.200/api_v1/index.php/mobile/stickets"; // "http://federicopiccinno.it/sugar_stickets/api_v1/index.php/mobile/stickets"; //
                RestTemplate restTemplate = new RestTemplate();
                Log.d("tag", "it's doing http shit");
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
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.select_sticket);
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

                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.firstRowButtons);
                LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.secondRowButtons);
                int count = linearLayout.getChildCount();
                int count1 = linearLayout1.getChildCount();
                for (int i = 0; i < count; i++) {
                    final Button sticketButton = (Button) linearLayout.getChildAt(i);
                    sticketButton.setTextColor(Color.TRANSPARENT);
                    sticketButton.setVisibility(View.VISIBLE);
                    sticketButton.setTextSize(1);
                    if (sticketButton.getTag().toString().equals(Integer.toString(sticketIDs[j]))&& sticket.getTicket_status()>2) {
                        sticketButton.setId(j);
                        sticketButton.setText("taken");
                        //sticketButton.setText(Integer.toString(sticket.getSticket_id()));
                        sticketButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int sticket_id = Integer.parseInt((String)sticketButton.getTag());
                                Intent intent = new Intent(getApplicationContext(),cameraPreviewActivity.class);   //TicketCreationActivity.class);
                                intent.putExtra("STICKET_ID", sticket_id);
                                intent.putExtra("CATEGORY",category);
                                intent.putExtra("USERNAME",username);
                                startActivity(intent);
                            }
                        });

                    } else {
                        // sticketButton.setBackgroundColor(Color.TRANSPARENT);
                    }

                }

                for (int i = 0; i < count1; i++) {
                    final Button sticketButton = (Button) linearLayout1.getChildAt(i);
                    sticketButton.setTextColor(Color.TRANSPARENT);
                    sticketButton.setVisibility(View.VISIBLE);
                    sticketButton.setTextSize(1);
                    if (sticketButton.getTag().toString().equals(Integer.toString(sticketIDs[j]))&& sticket.getTicket_status()>2) {
                        sticketButton.setId(j);
                        sticketButton.setText("taken");
                        //sticketButton.setText(Integer.toString(sticket.getSticket_id()));
                        sticketButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int sticket_id = Integer.parseInt((String)sticketButton.getTag());
                                Intent intent = new Intent(getApplicationContext(),cameraPreviewActivity.class);   //TicketCreationActivity.class);
                                intent.putExtra("STICKET_ID", sticket_id);
                                intent.putExtra("CATEGORY",category);
                                intent.putExtra("USERNAME",username);
                                startActivity(intent);
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
                    sticketButton.setBackgroundColor(Color.WHITE);
                } else{
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





        }
    }

*/

}
