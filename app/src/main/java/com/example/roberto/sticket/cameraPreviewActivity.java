package com.example.roberto.sticket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class cameraPreviewActivity extends Activity {
    private static final String TAG = "tag";

    private Camera mCamera;
    private CameraPreview mPreview;
    private static String imageFile="NULL";
    private static String imageFileName;
    private static File attachment;

    // Pass through vars
    private int problemType;
    private int sticket_id;
    private String username;
    private int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
        getActionBar().setTitle("Manutencoop Sticket");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();
        problemType = intent.getIntExtra("CATEGORY",-1);
        sticket_id = intent.getIntExtra("STICKET_ID",-1);
        username = intent.getStringExtra("USERNAME");
        int numberAffected;

        sticket_id = intent.getIntExtra("STICKET_ID",-1);
        category = intent.getIntExtra("CATEGORY",-1);
        username = intent.getStringExtra("USERNAME");


        Context context = getApplicationContext();
        if (checkCameraHardware(context)){
            // Create an instance of Camera
            mCamera = openFrontFacingCamera();//getCameraInstance();
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            final SurfaceHolder holder = mPreview.getHolder();
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            mPreview.surfaceChanged(holder, 1, width/4, height/4);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);

            // Add listeners to the buttons
            Button orig_captureButton = (Button) findViewById(R.id.button_capture);
            orig_captureButton.setBackgroundColor(Color.TRANSPARENT);
            orig_captureButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // get an image from the camera
                            Button cameraButton = (Button) v;
                            v.setClickable(false);

                            mCamera.takePicture(null, null, mPicture);
                            RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
                            mainLayout.setBackgroundResource(R.drawable.next_button_page);

                            //Intent i = new Intent(getApplicationContext(),TicketCreationActivity.class);
                            //i.putExtra("IMAGE_FILENAME",attachment.getAbsolutePath());//imageFile);
                            //i.putExtra("STICKET_ID",sticket_id);
                            ////i.putExtra("CATEGORY",category);
                            //i.putExtra("USERNAME",username);
                            //startActivity(i);
                        }
                    }
            );

            Button skipButton = (Button) findViewById(R.id.skip);
            skipButton.setBackgroundColor(Color.TRANSPARENT);
            skipButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //mCamera.stopPreview();
                            //mCamera.release();
                            mCamera = null;
                            mPreview=null;
                            Intent i = new Intent(getApplicationContext(),TicketCreationActivity.class);
                            i.putExtra("IMAGE_FILENAME",imageFile);
                            i.putExtra("STICKET_ID",sticket_id);
                            i.putExtra("CATEGORY",category);
                            i.putExtra("USERNAME",username);
                            startActivity(i);
                        }
                    }
            );
            //Button captureButton = new Button(this);//(Button) findViewById(R.id.button_capture);
            /*Drawable background = getResources().getDrawable(R.drawable.capture_button);
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
            );*/
            //preview.addView(captureButton);

        }
    }


    @Override
    public void onStop(){
        super.onStop();

    }

    @Override
    public void onPause() {
        try {
            super.onPause();
            if (mCamera != null) {
                //previewing = false;
                FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                preview.removeView(mPreview);
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;

                mPreview=null;


            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("releaseCamera",Log.getStackTraceString(e));
        }
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

    /** Check if this device has a camera */
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
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
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

    private Camera openFrontFacingCamera() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }


}
