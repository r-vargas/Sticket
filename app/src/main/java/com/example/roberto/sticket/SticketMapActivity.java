package com.example.roberto.sticket;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;


public class SticketMapActivity extends Activity {

    private double y1 = 37.426550;
    private double x1 = -122.172126;
    private HashMap<Integer, Device> sticketMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticket_map);
        getActionBar().setTitle("Manutencoop Sticket");
        ImageView map = (ImageView) findViewById(R.id.sticketMap);
        int xPosition = (int)map.getX();
        int yPosition = (int)map.getY();
        int width = map.getWidth();
        int height = map.getHeight();

        // Iterate over Stickets, create instances of sticket images...
        // and place them relative to the building. Since bldg 550 at Stanford
        // is oriented approximately 11.76 to longitude/latitude lines, outputs from
        // Navizon software must be rotated and scaled to be properly displayed.
        // X = longitude, Y = latitude



        //layout.addView(marker1);
        /* Resizing code
        LayoutParams lp = mImage.getLayoutParams();
        float width = (float)mImage.getDrawable().getIntrinsicWidth();
        int height = (int)(((float)mContext.getWindowManager().getDefaultDisplay().getWidth() / 10.0f) * ((float)mImage.getDrawable().getIntrinsicHeight() / width));
        lp.height = height;
        mImage.setLayoutParams(lp);
         */

    }

    @Override
    protected void onStart(){
        super.onStart();
        new HttpRequestTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sticket_map, menu);
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
                final String url = "http://federicopiccinno.it/sugar_stickets/api_v1/index.php/mobile/stickets"; // "http://rest-service.guides.spring.io/greeting";
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
                Device sticket = sticketMap.get(sticketIDs[j]);
                final Button sticketButton = new Button(getApplicationContext());

                sticketButton.setId(j);
                sticketButton.setText(Integer.toString(sticket.getSticket_id()));
                sticketButton.setTextSize(10);
                sticketButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(),ViewSticket.class);
                        Device sticket = sticketMap.get(Integer.parseInt(sticketButton.getText().toString()));
                        i.putExtra("TICKET_ID", Integer.toString(sticket.getTicket_id()));
                        i.putExtra("Y",Double.toString(sticket.getIndoorlocation().getLoc().getLat()));
                        i.putExtra("X",Double.toString(sticket.getIndoorlocation().getLoc().getLng()));
                        Integer.toString(sticket.getTicket_id());
                        startActivity(i);
                    }
                });
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
    }

}



