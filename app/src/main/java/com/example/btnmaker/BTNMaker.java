package com.example.btnmaker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.hardware.camera2.CameraDevice;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.madrapps.pikolo.ColorPicker;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.RGBColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BTNMaker extends AppCompatActivity {

    private TextView mTextMessage;
    private String mImageFileLocation;
    private RoundImageView mButtonImage;



    private static final int REQUEST_OPEN_RESULT_CODE = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bntmaker);
        mButtonImage = (RoundImageView) findViewById(R.id.round_btn);

    //ButtonUtils(mButton,R.color.backgroundColor, R.dimen.conner_radius, R.color.shadowColor,R.dimen.elevation,5);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


            }

            //bottom navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                //set color colorPicker form -> https://android-arsenal.com/details/1/5871#!description
                case R.id.set_color:
                    final HSLColorPicker mColorPicker = findViewById(R.id.color_picker);
                    mColorPicker.setVisibility(View.VISIBLE);
                    mColorPicker.setColorSelectionListener(new SimpleColorSelectionListener(){
                        @Override
                                public void onColorSelected(int color){

                                mButtonImage.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

                        }
                    });
                    return true;
                case R.id.take_photo:
                    Intent photoIntent = new Intent();
                    photoIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                    File photoFile = null;
                    try{
                        photoFile = createImageFile();

                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    Uri uri = Uri.fromFile(photoFile);
                    photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(photoIntent, REQUEST_IMAGE_CAPTURE);
                    return true;
                case R.id.set_photo:
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_OPEN_RESULT_CODE);
                    return true;
                case R.id.set_text:
                    mTextMessage.setText("set texts");
                    return true;
            }
            return false;
        }
    };//select bottom nav
    //todo need to find out in there needs to needs to something in manifest to return the content

@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    if (requestCode == REQUEST_OPEN_RESULT_CODE && resultCode == RESULT_OK){
        Uri uri= null;
       if(data != null){
           uri = data.getData();
           Bitmap bitmap = null;
           try {
               bitmap = getBitMapFromUri(uri);
               mButtonImage.setImageBitmap(bitmap);
           } catch (IOException e) {

               e.printStackTrace();
           }
       }
    }else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

        if(data != null){
           Bitmap bitmap = BitmapFactory.decodeFile(mImageFileLocation);
           mButtonImage.setImageBitmap(bitmap);
        }
    }
}//onActivityResult
  private CameraDevice mCameraDevice;
  private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {
      @Override
      public void onOpened(CameraDevice cameraDevice) {
          mCameraDevice = cameraDevice;
      }

      @Override
      public void onDisconnected(CameraDevice cameraDevice) {
            cameraDevice.close();
            mCameraDevice = null;
      }

      @Override
      public void onError(CameraDevice cameraDevice, int i) {

      }
  };

  protected File createImageFile() throws IOException{

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "IMAGE_" + timeStamp + "_";
    File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

    File imageFile = File.createTempFile(imageFileName, ".jpg" , storageDirectory);
    mImageFileLocation = imageFile.getAbsolutePath();
    Toast.makeText(this, mImageFileLocation + "file location", Toast.LENGTH_SHORT);

    return imageFile;
    }

    private Bitmap getBitMapFromUri(Uri uri)throws IOException{
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;
    }
}
