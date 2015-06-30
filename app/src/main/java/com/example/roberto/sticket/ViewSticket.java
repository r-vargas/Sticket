package com.example.roberto.sticket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class ViewSticket extends Activity {

    // Activity vars
    private Device sticket;
    private Ticket ticket_forDisplay;
    private String ticketID;
    private String getURL;
    Bitmap bitmap = null;

    // Ticket vars
    private int id;
    private int status;
    private int activation_date;
    private int category;
    private int people_involved;
    private String notes;
    private String attachment_name;
    private int user_id;
    private String user_name;
    private String attachment_url;

    // Sticket vars
    private int sticket_id;
    private String mac_address;
    private int ticket_id;
    private int power_on;
    private int ticket_status;
    private navizonDevice device;

    // RL Vars
    private int rlWidth;
    private int rlHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sticket);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // Get sticket and populate local vars
        Intent intent = getIntent();
        getActionBar().setTitle("Manutencoop Sticket");
        int ticketID = Integer.parseInt(intent.getStringExtra("TICKET_ID"));
        getURL = "http://192.168.0.200/api_v1/index.php/mobile/ticket/" + ticketID; //"http://federicopiccinno.it/sugar_stickets/api_v1/index.php/mobile/ticket/";

        double x = Double.parseDouble(intent.getStringExtra("X"));
        double y = Double.parseDouble(intent.getStringExtra("Y"));
        //new HttpRequestTask().execute();
        new HttpTicketRequestTask().execute();




        double xInput = x;
        double yInput = y;
        Log.d("xInput",Double.toString(xInput));
        Log.d("yInput",Double.toString(yInput));
        int screenX = gpsToScreen(xInput,yInput,0, rlWidth,rlHeight);//rl.getWidth(),rl.getHeight());
        int screenY = gpsToScreen(xInput,yInput,1, rlWidth,rlHeight); //rl.getWidth(), rl.getHeight());

        Button sticketButton = new Button(getApplicationContext());

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.mapWrapper);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(10, 10);
        int leftMargin = Math.round(screenX*getResources().getDisplayMetrics().density);
        int topMargin = Math.round(screenY*getResources().getDisplayMetrics().density);
        params.leftMargin = 130; //Math.round(screenX*getResources().getDisplayMetrics().density);//165+25;//screenX;
        params.topMargin = 60; //Math.round(screenY*getResources().getDisplayMetrics().density); //118+0;//screenY;
        //sticketButton.layout(screenX,screenY,10,10);
        //sticketButton.setLayoutParams(params);
        rl.addView(sticketButton,params);
        Log.d("Xoffset", Integer.toString(screenX));
        Log.d("Yoffset", Integer.toString(screenY));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_sticket, menu);
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
/*
    public void placeSticketOnMap(double x, double y) {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.mapWrapper);
        int screenX = gpsToScreen(x, y, 0, rl.getWidth(), rl.getHeight());
        int screenY = gpsToScreen(x, y, 1, rl.getWidth(), rl.getHeight());
        ImageView marker1 = new ImageView(getApplicationContext());
        marker1.setImageResource(R.drawable.marker_1);
        marker1.setMaxHeight(60);
        marker1.setMaxWidth(60);


        ImageView iv = marker1;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(60, 60);
        params.leftMargin = screenX;
        Log.d("Xoffset", Integer.toString(screenX));
        Log.d("Yoffset", Integer.toString(screenY));
        params.topMargin = screenY;
        rl.addView(iv, params);
    }*/

    private class HttpTicketRequestTask extends AsyncTask<Void, Void, ticketResponse> {
        @Override
        protected ticketResponse doInBackground(Void... params) {
            try {
                final String url = getURL; // "http://rest-service.guides.spring.io/greeting";
                RestTemplate restTemplate = new RestTemplate();
                Log.d("tag","it's doing http shit");
                //restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                //String result = restTemplate.getForObject(url, String.class, "Android");
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ticketResponse response = restTemplate.getForObject(url, ticketResponse.class);
                Log.d("got response", "got response");
                return response;
            } catch (Exception e) {
                Log.e("ViewSticket", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(ticketResponse response) {
            new HttpImageRequestTask().execute();
            // Get Sticket, then associated Ticket though API calls
            ticket_forDisplay = response.getTicket();
            Log.d("wooter u doon","wooter u doon");

            ticket_id = ticket_forDisplay.getId();
            ticket_status = ticket_forDisplay.getStatus();
            activation_date = ticket_forDisplay.getActivation_date();
            category = ticket_forDisplay.getCategory();
            people_involved = ticket_forDisplay.getPeople_involved();
            notes = ticket_forDisplay.getNotes();
            attachment_name = ticket_forDisplay.getAttachment_name();
            user_id = ticket_forDisplay.getUser_id();
            user_name = ticket_forDisplay.getUser_name();
            attachment_url = ticket_forDisplay.getAttachment_url();

            long time = ticket_forDisplay.getActivation_date();
            long timeUpdate = time * (long) 1000;
            Date date = new Date(timeUpdate);
            SimpleDateFormat format = new SimpleDateFormat("MMM dd HH:mm");
            TextView dateView = (TextView) findViewById(R.id.date);
            dateView.setText(format.format(date));


            TextView user_nameView = (TextView) findViewById(R.id.name);
            if (user_name == null){
                user_nameView.setText("< No user name found >");
            } else {
                user_nameView.setText(user_name);
            }

            TextView notesView = (TextView) findViewById(R.id.notes);
            if (notes == null) {
                notesView.setText("< No note found >");
            }else{
                notesView.setText(notes);
            }
/*
            RadioGroup radiogroup = (RadioGroup) findViewById(R.id.numberInvolved);
            int count = radiogroup.getChildCount();
            ArrayList<RadioButton> listOfRadioButtons = new ArrayList<RadioButton>();
            for (int i=0;i<count;i++) {
                RadioButton button = (RadioButton) radiogroup.getChildAt(i);
                if (button.getTag().toString().equals(Integer.toString(people_involved))){
                    button.setChecked(true);
                }
            }
*/
            ImageView categoryImage = (ImageView) findViewById(R.id.description_image);
            switch  (category){
                case(1):
                    categoryImage.setImageResource(R.drawable.internet_problem);
                    break;
                case(6):
                    categoryImage.setImageResource(R.drawable.wc_problem);
                    break;
                case(7):
                    categoryImage.setImageResource(R.drawable.door_problem);
                    break;
                case(2):
                    categoryImage.setImageResource(R.drawable.plumbing_problem);
                    break;
                case(4):
                    categoryImage.setImageResource(R.drawable.lighting_problem);
                    break;
                case(3):
                    categoryImage.setImageResource(R.drawable.building_problem);
                    break;
                case(9):
                    categoryImage.setImageResource(R.drawable.cleaning_problem);
                    break;
                case(8):
                    categoryImage.setImageResource(R.drawable.electrical_problem);
                    break;
                case(5):
                    categoryImage.setImageResource(R.drawable.temperature_problem);
                    break;
                default:
                    break;
            }
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.view_sticket_layout);
            switch (ticket_status){
                case (1):
                    rl.setBackgroundResource(R.drawable.sticket_status_one);
                    break;
                case (2):
                    rl.setBackgroundResource(R.drawable.sticket_status_two);
                    break;
                case (3):
                    rl.setBackgroundResource(R.drawable.sticket_status_three);
                    break;
                case (4):
                    rl.setBackgroundResource(R.drawable.sticket_status_four);
                    break;
            }

        }
    }

    public int gpsToScreen(double xPos, double yPos, int output, double layoutWidth, double layoutHeight) {
        layoutWidth = 165;
        layoutHeight = 118;
        Log.d("layoutWidth", Double.toString((int) layoutWidth));
        Log.d("layoutHeight", Double.toString((int) layoutHeight));
        double shiftedY = -1 * (((yPos * 1000000) - 37420000) - 6550);
        double shiftedX = -1 * ((xPos * 1000000) + 122170000) - 2126;
        Log.d("shiftedxPos", Integer.toString((int) shiftedX));
        Log.d("shiftedyPos", Integer.toString((int) shiftedY));
        double theta = 11.76;
        double thetaInRads = theta * 2 * Math.PI / 360;
        double xOffset = (((Math.cos(thetaInRads) * shiftedX) - (Math.sin(thetaInRads) * shiftedY)))/(598.56)*layoutWidth;
        double yOffset = (((Math.sin(thetaInRads) * shiftedX) + (Math.cos(thetaInRads) * shiftedY)))/(122)*layoutHeight;

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

    }

    private class HttpRequestTask extends AsyncTask<Void, Void, sticketResponse> {
        @Override
        protected sticketResponse doInBackground(Void... params) {
            try {
                final String url = getURL; // "http://rest-service.guides.spring.io/greeting";
                RestTemplate restTemplate = new RestTemplate();
                Log.d("tag","it's doing http shit");
                //restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                //String result = restTemplate.getForObject(url, String.class, "Android");
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                sticketResponse response = restTemplate.getForObject(url, sticketResponse.class);
                Log.d("got response", "got response");
                return response;
            } catch (Exception e) {
                Log.e("ViewSticket", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(sticketResponse response) {
            // Get Sticket, then associated Ticket though API calls
            sticket = response.getDevices()[sticket_id];
            Log.d("wooter u doon","wooter u doon");

/*
               // Device sticket = sticketMap.get(sticketIDs[j]);
                Button sticketButton = new Button(getApplicationContext());
               // sticketButton.setId(j);
                sticketButton.setText(Integer.toString(sticket.getTicket_id()));
                sticketButton.setTextSize(10);
                //sticketButton.setMaxHeight(60);
                //sticketButton.setMaxWidth(60);
                //ImageView marker1 = null;
                int screenX = 0;
                int screenY = 0;

                RelativeLayout rl = (RelativeLayout) findViewById(R.id.mapWrapper);
                double xInput = sticket.getIndoorlocation().getLoc().getLat();
                double yInput = sticket.getIndoorlocation().getLoc().getLng();
                Log.d("xInput",Double.toString(xInput));
                Log.d("yInput",Double.toString(yInput));
                screenX = gpsToScreen(xInput,yInput,0,rlWidth,rlHeight);//rl.getWidth(),rl.getHeight());
                screenY = gpsToScreen(xInput,yInput,1,rlWidth,rlHeight); //rl.getWidth(), rl.getHeight());

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(10, 10);
                params.leftMargin = screenY;
                Log.d("Xoffset", Integer.toString(screenX));
                Log.d("Yoffset", Integer.toString(screenY));
                params.topMargin = screenX;
                rl.addView(sticketButton, params);*/




        }
    }

    private class HttpImageRequestTask extends AsyncTask<Void, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                // Log.d("Attachment URL", attachment_url);
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(attachment_url).getContent());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView problemImage = (ImageView) findViewById(R.id.problem_image);
                        if (bitmap != null) {
                            problemImage.setImageBitmap(bitmap);
                            problemImage.setRotation(-90); // !!NEGATIVE FOR SELFIE CAM, POSITIVE FOR BACK CAM!!
                        }
                        //TextView loadingText = (TextView) findViewById(R.id.waiting);
                        //loadingText.setVisibility(View.GONE);
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        }

    private void onMapClick(View v){

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        updateSizeInfo();
    }

    private void updateSizeInfo() {
        RelativeLayout rl_cards_details_card_area = (RelativeLayout) findViewById(R.id.mapWrapper);
        rlWidth = rl_cards_details_card_area.getWidth();
        rlHeight = rl_cards_details_card_area.getHeight();
        Log.v("W-H", rlWidth+"-"+rlHeight);
    }
    }





