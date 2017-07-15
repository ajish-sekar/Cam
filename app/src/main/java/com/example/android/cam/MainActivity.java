package com.example.android.cam;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.id;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;



public class MainActivity extends AppCompatActivity {
    private Camera mCamera;
    private CameraPreview mPreview;
    private int cam=0;
    private String mcurrentPhoto;
    private String mCurrentPhotoPath;
    private Uri selectedImageUri;
    ArrayList<String> files = new ArrayList<String>();// list of file paths
    File[] listFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!checkCameraHardware(this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Note");
            builder.setMessage("No Camera Available");
            builder.setNeutralButton("Got it!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                    System.exit(0);
                }
            });
            builder.show();

        }
        ImageView img = (ImageView) findViewById(R.id.gallery);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(intent);
            }
        });
       /* mCamera = getCameraInstance(cam);



        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);*/
        ImageView captureButton = (ImageView) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera

                        //mCamera.takePicture(null, null, mPicture);


                        cameraIntent();
                    }
                }
        );
        ImageView switchButton = (ImageView) findViewById(R.id.switch_cam);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cam==0){
                    releaseCamera();
                    cam=1;
                    mCamera = getCameraInstance(cam);


                    mPreview.setmCamera(mCamera);
                    mPreview.refreshSurface();
                }else {
                    releaseCamera();
                    cam=0;
                    mCamera = getCameraInstance(cam);


                    mPreview.setmCamera(mCamera);
                    mPreview.refreshSurface();
                }
            }
        });






    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }




    public static Camera getCameraInstance(int cam){
        Camera c = null;
        try {
            c = Camera.open(cam); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    File pictureFile = getOutputMediaFile(101);
                    if (pictureFile == null) {
                        Log.d("Error", "Error creating media file, check storage permissions: ");
                        return;
                    }

                    try {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        if (cam == 0) {
                            bmp = rotateImage(90, bmp);
                        } else {
                            bmp = rotateImage(270, bmp);
                        }
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] flippedImageByteArray = stream.toByteArray();


                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        fos.write(flippedImageByteArray);
                        fos.close();


                    } catch (FileNotFoundException e) {
                        Log.d("Error", "File not found: " + e.getMessage());
                    } catch (IOException e) {
                        Log.d("Error", "Error accessing file: " + e.getMessage());
                    }

                    galleryAddPic();


                }
            });
            releaseCamera();
            mCamera = getCameraInstance(cam);


            mPreview.setmCamera(mCamera);
            mPreview.refreshSurface();

        }
    };

    public Bitmap rotateImage(int angle, Bitmap bitmapSrc) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmapSrc, 0, 0,bitmapSrc.getWidth(), bitmapSrc.getHeight(), matrix, true);
    }



    @Override
    protected void onPostResume() {
        super.onPostResume();
        /*mCamera = getCameraInstance(0);
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        //releaseCamera();
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
    private void cameraIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.v("Error","IO Exception");

            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider2",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 0);
            }
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(data.getData() != null){
                selectedImageUri = data.getData();
            }
            if (requestCode == 0 && resultCode == RESULT_OK) {


                File f = new File(mCurrentPhotoPath);
                selectedImageUri = Uri.fromFile(f);
                ImageView imageView = (ImageView) findViewById(R.id.gallery);
                imageView.setImageURI(selectedImageUri);
                ImageView imageView1 = (ImageView) findViewById(R.id.img);
                imageView1.setImageURI(selectedImageUri);


            }


        }

    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );


        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //releaseCamera();
    }

    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(int type){
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
        if (type == 101){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }
        mcurrentPhoto = mediaFile.getAbsolutePath();
        return mediaFile;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mcurrentPhoto) ;
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }







}