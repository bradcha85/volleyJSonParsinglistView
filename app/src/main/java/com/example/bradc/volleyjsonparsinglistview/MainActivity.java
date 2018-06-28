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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    ArrayList<deliverRequest> reqList = new ArrayList<deliverRequest>();
    Context context = MainActivity.this;
    private SwipeRefreshLayout mSwipeRefresh;
    String url = "http://192.168.0.25:8080/user/androidTest";
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
                Log.d("refresh", "!!!!!!!!!!!");
                mSwipeRefresh.setRefreshing(false);
                //reqList = RequestList.getInstance().getReqList();
                //Log.d("onRefresh", reqList.toString());
                ListView lv = (ListView) findViewById(R.id.listView);
                RequestList.getInstance().requestRender(url, lv, getApplicationContext());
            }});
        ListView lv = (ListView) findViewById(R.id.listView);
        RequestList.getInstance().requestRender(url, lv, getApplicationContext());
      //  Log.d("reQuestList!!!",RequestList.getInstance().getReqList().toString());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_bottom_navigation_menu1:
                    // Toast.makeText(getApplicationContext(), "menu1", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, RequestRegisterActivity.class);
                    //intent.putExtra("deliverRequest", "dddddd" );
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

    public String getRealPathFromURI(Uri contentUri) {

        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Activity activity = new Activity();
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);

        }
        cursor.close();
        return res;
    }

    public void DoFileUpload(String apiUrl, String absolutePath) {
        HttpFileUpload(apiUrl, "", absolutePath);
    }

    public void HttpFileUpload(String urlString, String params, String fileName) {

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {

            File sourceFile = new File(fileName);

            DataOutputStream dos;

            if (!sourceFile.isFile()) {

                Log.e("uploadFile", "Source File not exist :" + fileName);

            } else {

                FileInputStream mFileInputStream = new FileInputStream(sourceFile);

                URL connectUrl = new URL(urlString);

                // open connection

                HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();

                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                // write data
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                int bytesAvailable = mFileInputStream.available();
                int maxBufferSize = 1024 * 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);

                byte[] buffer = new byte[bufferSize];
                int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);



                // read image
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = mFileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
                }


                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                mFileInputStream.close();
                dos.flush(); // finish upload...
                if (conn.getResponseCode() == 200) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                }
                mFileInputStream.close();
                dos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}