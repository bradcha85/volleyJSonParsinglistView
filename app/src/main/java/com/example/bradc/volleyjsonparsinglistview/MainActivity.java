package com.example.bradc.volleyjsonparsinglistview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private Context context = MainActivity.this;
    private SwipeRefreshLayout mSwipeRefresh;  //당겨서 새로고침하는 레이아웃
    private String url = "http://192.168.0.25:8080/user/androidTest";

   // ListView lv = (ListView) findViewById(R.id.listView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // verifyStoragePermissions(activity);

        mRequestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        // requestList.requestRendering(url, lv);
        BottomNavigationView navigationView = (BottomNavigationView)findViewById(R.id.main_navbar);
        navigationView.setOnNavigationItemSelectedListener(navigationListener);

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {   //아래로 당길때 리퀘스트목록이 리프레쉬된다.
                mSwipeRefresh.setRefreshing(false);
                ListView lv = (ListView) findViewById(R.id.listView);
                RequestList.getInstance().requestRender(url, lv, getApplicationContext());
            }});
        ListView lv = (ListView) findViewById(R.id.listView);
        RequestList.getInstance().requestRender(url, lv, getApplicationContext());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.requestRegistration:
                    Intent intent = new Intent(context, RequestRegisterActivity.class);
                    context.startActivity(intent);
                    return true;
                case R.id.main:
                    Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.myInformation:
                    Toast.makeText(getApplicationContext(), "myInfo", Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }
    };
}
