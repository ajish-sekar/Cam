package com.example.android.cam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ajish on 15-07-2017.
 */

/*public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mimgs;

    public ImageAdapter(Context c, ArrayList<String> imgs) {
        mContext = c;
        mimgs = imgs;
    }

    public int getCount() {
        return mimgs.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        File file1 = new File(mimgs.get(position));
        Bitmap myBitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
        imageView.setImageBitmap(myBitmap);
        return imageView;
    }



}*/
