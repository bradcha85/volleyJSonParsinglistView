package com.example.bradc.volleyjsonparsinglistview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class requestDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        Intent intent = getIntent();

        TextView detail_startAddr = findViewById(R.id.detail_startAddr);
        TextView detail_arriveAddr = findViewById(R.id.detail_arriveAddr);
        TextView detail_product = findViewById(R.id.detail_product);
        TextView detail_box = findViewById(R.id.detail_box);
        TextView detail_howMany = findViewById(R.id.detail_howMany);
        TextView detail_weight = findViewById(R.id.detail_weight);
        TextView detail_price = findViewById(R.id.detail_price);
        TextView detail_memo = findViewById(R.id.detail_memo);

        deliverRequest dRequest = (deliverRequest)intent.getSerializableExtra("deliverRequest");

        detail_startAddr.setText(dRequest.getStartAddr());
        detail_arriveAddr.setText(dRequest.getArriveAddr());
        detail_product.setText(dRequest.getProduct());
        detail_box.setText(dRequest.getBox());
        detail_howMany.setText(Integer.toString(dRequest.getHowMany()));
        detail_weight.setText(Integer.toString(dRequest.getWeight()));
        detail_price.setText(Integer.toString(dRequest.getPrice()));
        detail_memo.setText(dRequest.getMemo());

        ArrayList<String> imageList = dRequest.getImageList();
        for(int i = 0 ; i < imageList.size() ; i++){
            Log.d("fileName : " , imageList.get(i));
        }

        GridView gv = findViewById(R.id.detail_image);
        RequestImageAdpater requestImageAdpater = new RequestImageAdpater(this.getApplicationContext(), R.layout.requestimage, imageList);
        gv.setAdapter(requestImageAdpater);

    }
}
