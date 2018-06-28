package com.example.bradc.volleyjsonparsinglistview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class RequestAdapter extends BaseAdapter{

    LayoutInflater inflater;
    Context context;

    int layout;
    ArrayList<deliverRequest> reqList;
    private ImageLoader mImageLoader;


    public RequestAdapter(Context context, int layout, ArrayList<deliverRequest> reqList){
        this.context = context;
        this.layout = layout;
        this.reqList = reqList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        Log.d("!!reqListSize" , Integer.toString(reqList.size()));
        return reqList.size();
    }

    @Override
    public Object getItem(int position) {
        return reqList.get(position);
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

        TextView startAddrText = convertView.findViewById(R.id.startAddr);
        TextView arriveAddrText = convertView.findViewById(R.id.arriveAddr);
        TextView priceText = convertView.findViewById(R.id.price);
        TextView productText = convertView.findViewById(R.id.product);

        final deliverRequest dRequest = reqList.get(position);
        final String startAddr = dRequest.getStartAddr();
        final String arriveAddr = dRequest.getArriveAddr();
        final String product = dRequest.getProduct();
        final String box = dRequest.getBox();
        final int howMany = dRequest.getHowMany();
        final int weight = dRequest.getWeight();
        final int price = dRequest.getPrice();
        final String thumbnailImageUrl = dRequest.getThumbnailImageUrl();
        final ArrayList<String> imageList = dRequest.getImageList();
        Log.d("=====THUMBNAIL URL==" , thumbnailImageUrl);
        startAddrText.setText(startAddr);
        arriveAddrText.setText(arriveAddr);
        // Currency.getInstance(Locale.KOREA).getSymbol() <== 한국원화표시 (\)
        priceText.setText(Currency.getInstance(Locale.KOREA).getSymbol() + Integer.toString(price));
        productText.setText(product);

        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
        NetworkImageView thumbnail = convertView.findViewById(R.id.thumbnail);
        thumbnail.setImageUrl(reqList.get(position).getThumbnailImageUrl(), mImageLoader);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, requestDetailActivity.class);
                intent.putExtra("deliverRequest", dRequest );
                context.startActivity(intent);

            }
        });


        return convertView;
    }

}
