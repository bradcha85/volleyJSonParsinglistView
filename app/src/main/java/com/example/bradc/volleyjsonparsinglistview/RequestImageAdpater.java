package com.example.bradc.volleyjsonparsinglistview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.ui.NetworkImageView;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class RequestImageAdpater extends BaseAdapter{

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<String> imageList;
    private ImageLoader mImageLoader;
    private String imagePath = "http://192.168.219.187:8080/request/productOriginalImage/";
    //  private String imagePath = "http://localhost:8080/request/productOriginalImage/";


    public RequestImageAdpater(Context context, int layout, ArrayList<String> imageList){
        this.context = context;
        this.layout = layout;
        this.imageList = imageList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
        NetworkImageView reqImage = convertView.findViewById(R.id.reqImage);
        Log.d("RequestImageAdapter", imagePath+imageList.get(position));

        reqImage.setImageUrl(imagePath+imageList.get(position), mImageLoader);

        return convertView;
    }

}
