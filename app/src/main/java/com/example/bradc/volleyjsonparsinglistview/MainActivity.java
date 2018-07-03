package com.example.bradc.volleyjsonparsinglistview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private Context context = MainActivity.this;
    private SwipeRefreshLayout mSwipeRefresh;
    String url = "http://192.168.0.25:8080/user/androidTest";
    private final int PICK_FROM_ALBUM=1;
    private Uri mImageCaptureUri;
    private Bitmap bitmap;

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
            public void onRefresh() {
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
                case R.id.action_bottom_navigation_menu1:
                    // Toast.makeText(getApplicationContext(), "menu1", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, RequestRegisterActivity.class);
                    context.startActivity(intent);
                    return true;
                case R.id.action_bottom_navigation_menu2:
                    Toast.makeText(getApplicationContext(), "menu2", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.action_bottom_navigation_menu3:
                    Toast.makeText(getApplicationContext(), "menu3", Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }
    };


    public void takePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mImageCaptureUri = data.getData();
        Log.d("sss", mImageCaptureUri.getPath().toString());
    }
}
