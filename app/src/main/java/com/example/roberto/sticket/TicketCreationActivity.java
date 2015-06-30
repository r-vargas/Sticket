package com.example.roberto.sticket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TicketCreationActivity extends Activity {
    private static final String TAG = "tag";
    //CameraPreview cameraPreview;
    private Ticket upload_ticket;
    private Camera mCamera;
    private CameraPreview mPreview;
    private static String imageFile;
    private static String imageFilename;
    private int problemType;
    private int sticket_id;
    private String username;
    private String baseURL = "http://federicopiccinno.it/sugar_stickets/api_v1/index.php";
    private String ticketURL = "http://192.168.0.200/api_v1/index.php/mobile/ticket";
    //private String ticketURL = "http://federicopiccinno.it/sugar_stickets/api_v1/index.php/mobile/ticket";
    private String uploadURLroot = "http://192.168.0.200/api_v1/index.php/mobile/ticket/";
    //private String uploadURLroot = "http://federicopiccinno.it/sugar_stickets/api_v1/index.php/mobile/ticket/";
    private String uploadURLend = "/attachment";
    private String uploadURL;
    private static File attachment;
    private int DEMO_NUMBER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_creation);
        getActionBar().setTitle("Manutencoop Sticket");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();
        problemType = intent.getIntExtra("CATEGORY",-1);
        sticket_id = intent.getIntExtra("STICKET_ID",-1);
        username = intent.getStringExtra("USERNAME");
        imageFilename = intent.getStringExtra("IMAGE_FILENAME");
        Log.d("imageFile",imageFilename);
        int numberAffected;

        Button sendButton = (Button) findViewById(R.id.sendTicket);
        sendButton.setBackgroundColor(Color.TRANSPARENT);

        if (!imageFilename.equals("NULL")){
            ImageView problemPreview = (ImageView) findViewById(R.id.imagePreview);
            problemPreview.setImageBitmap(BitmapFactory.decodeFile(imageFilename));
            problemPreview.setRotation(-90); // NEGATIVE for selfies, POSITIVE for back camera
        }
/*
        Button orig_captureButton = (Button) findViewById(R.id.button_capture);
        orig_captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);

                    }
                }
        );

        Context context = getApplicationContext();
        if (checkCameraHardware(context)){
            // Create an instance of Camera
            mCamera = getCameraInstance();

            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            SurfaceHolder holder = mPreview.getHolder();
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
           display.getSize(size);
           int width = size.x;
           int height = size.y;
            mPreview.surfaceChanged(holder, 1, width/4, height/4);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                preview.addView(mPreview);
            // Add a listener to the Capture button
            Button captureButton = new Button(this);//(Button) findViewById(R.id.button_capture);
            Drawable background = getResources().getDrawable(R.drawable.capture_button);
            captureButton.setBackground(background);
            captureButton.setWidth(20);
            captureButton.setHeight(10);
            captureButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // get an image from the camera
                            mCamera.takePicture(null, null, mPicture);

                        }
                    }
            );
            //preview.addView(captureButton);

        }*/
    }

    @Override
    public void onStop(){
//        mCamera.release();
        super.onStop();

    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ticket_creation, menu);
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
    /** Check if this device has a camera
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Toast.makeText(getBaseContext(), "Picture Saved!", Toast.LENGTH_SHORT).show();
                mCamera.release();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */

    /*
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        imageFile = (mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        attachment = mediaFile;
        return mediaFile;
    }
    */
    public void createTicket(View v) {
        int numberAffected;
        Ticket ticket = new Ticket();
       // imageFilename = imageFile;
/*
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.severity);
        int id = radioGroup.getCheckedRadioButtonId();
        if (id != -1) {
            RadioButton button = (RadioButton) findViewById(id);
            int severity = Integer.parseInt(button.getTag().toString());
        } else {
            int severity = -1;
        }
        */
        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.numberInvolved);
        int buttonID = radioGroup1.getCheckedRadioButtonId();
        if (buttonID != -1) {
            RadioButton button1 = (RadioButton) findViewById(buttonID);
            numberAffected = Integer.parseInt(button1.getTag().toString());
        } else {
            numberAffected = -1;
        }
            String description = ((EditText) findViewById(R.id.problemDescription)).getText().toString();
            ticket.setNotes(description);
            ticket.setPeople_involved(numberAffected);
            ticket.setCategory(problemType);
            ticket.setSticket_id(sticket_id);
            ticket.setUser_id(DEMO_NUMBER);

        upload_ticket = ticket;
        setContentView(R.layout.send_receipt);
        /*
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
*/
            //ticket.setProblemSeverity(severity);
            //ticket.setRepairImagePath(imageFile);



        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            // POST the ticket to the server
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

// Add the Jackson and String message converters
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            //restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

            // Post ticket
            String response = restTemplate.postForObject(ticketURL, ticket, String.class);
            String postedTicketIDfragment = response.substring(response.indexOf("id"));
            String postedTicketIDfragment1 = postedTicketIDfragment.substring(postedTicketIDfragment.indexOf(":")+2);
            String postedTicketID = postedTicketIDfragment1.substring(0,postedTicketIDfragment1.indexOf("\""));
            uploadURL = uploadURLroot+postedTicketID+uploadURLend; //Integer.toString(18)+uploadURLend;
            Log.d("response:",postedTicketID);

            // Post image
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
            if (!imageFilename.equals("NULL")) {
                Log.d("imageFilename",imageFilename);
                map.add("attachment", new FileSystemResource(imageFilename));
                HttpHeaders imageHeaders = new HttpHeaders();
                imageHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                HttpEntity<MultiValueMap<String, Object>> imageEntity = new HttpEntity<MultiValueMap<String, Object>>(map, imageHeaders);
                //restTemplate.exchange(uploadURL, HttpMethod.POST, imageEntity, Boolean.class);
                Log.d("uploadURL",uploadURL);
                String uploadResponse = restTemplate.postForObject(uploadURL, imageEntity, String.class);

                //MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
                //parts.add("attachment", attachment);
                //Log.d("uploadURL",uploadURL);
                //String uploadResponse = restTemplate.postForObject(uploadURL, parts, String.class);
                // String uploadResponse = restTemplate.postForObject(uploadURL, ticketImage, String.class);
                Log.d("uploadResponse", uploadResponse);
            }
            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Context context = getApplicationContext();
                        Intent i = new Intent(context, MainActivity.class);
                        i.putExtra("USERNAME", username);
                        startActivity(i);
                    }
                }
            };
            timerThread.start();
            //Toast.makeText(getBaseContext(), uploadResponse, Toast.LENGTH_SHORT).show();
        }


        }

}
