package com.example.android.cam;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static android.R.attr.x;

public class Main2Activity extends AppCompatActivity {

    ArrayList<String> files = new ArrayList<String>();// list of file paths
    File[] listFile;
    int pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getFromSdcard();
        Toast.makeText(this,"Swipe to move from picture to picture",Toast.LENGTH_SHORT).show();

        //GridView gridView = (GridView) findViewById(R.id.grid);
        //ImageAdapter imageAdapter = new ImageAdapter(this,files);
        //gridView.setAdapter(imageAdapter);
        ImageView imageView = (ImageView) findViewById(R.id.img_view);
        File file1 = new File(files.get(pos));

        Bitmap myBitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
        imageView.setImageBitmap(myBitmap);
        imageView.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                if(pos>0){
                    pos--;
                }else {
                    Toast.makeText(getApplicationContext(),"End of file",Toast.LENGTH_SHORT).show();
                }
                ImageView imageView = (ImageView) findViewById(R.id.img_view);
                File file1 = new File(files.get(pos));
                Bitmap myBitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                if(pos<files.size()-1){
                    pos++;
                }else {
                    Toast.makeText(getApplicationContext(),"End of file",Toast.LENGTH_SHORT).show();
                }
                ImageView imageView = (ImageView) findViewById(R.id.img_view);
                File file1 = new File(files.get(pos));
                Bitmap myBitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
            }
        });

        ImageView delete = (ImageView) findViewById(R.id.del);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                builder.setTitle("Warning");
                builder.setMessage("Are you sure you want to delete this picture?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file3 = new File(files.get(pos));
                        file3.delete();
                        if(files.size()>0) {
                            files.remove(pos);
                        }
                        ImageView imageView = (ImageView) findViewById(R.id.img_view);
                        File file1 = new File(files.get(pos));
                        Bitmap myBitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
                        imageView.setImageBitmap(myBitmap);
                        Toast.makeText(Main2Activity.this,"Deleted!!",Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });

    }



    public void getFromSdcard()
    {
        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (file.isDirectory())
        {
            listFile = file.listFiles();


            for (int i = listFile.length-1; i>=0; i--)
            {

                files.add(listFile[i].getAbsolutePath());


            }

        }
    }


}
