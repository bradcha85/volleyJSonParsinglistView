package com.example.bradc.volleyjsonparsinglistview;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class RequestRegisterActivity extends AppCompatActivity {

    static final int PICK_IMAGE_REQUEST = 1;

    private String uploadUrl = "http://192.168.0.25:8080/user/androidImageUploadTest";
    private Uri mImageCaptureUri;
    private String filePath;
    private String absolutePath;
    private ImageView registerImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requestregistlayout);
        registerImageView = findViewById(R.id.registerImage);

        Button imagePreviewButton = findViewById(R.id.imagePreviewButton);
        Button imageUploadButton = findViewById(R.id.imageUpload);

        imagePreviewButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                imageBrowse();
            }
        });


        imageUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath != null) {
                    imageUpload(filePath);
                } else {
                    Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


//앨범에서 이미지 가져오기
    public void imageBrowse(){
        //앨범 호출
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if(requestCode == PICK_IMAGE_REQUEST){
                Uri picUri = data.getData();

                filePath = getPath(picUri);
                Log.d("@@filePath", filePath);

                registerImageView.setImageURI(picUri);

            }

        }
    }

    protected void imageUpload(final String imagePath){
        Log.d("@@imagePath", imagePath);
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, uploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jObj = new JSONObject(response);

                            String message = jObj.getString("message");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        smr.addFile("image", imagePath);
        VolleySingleton.getInstance(this).getRequestQueue().add(smr);
    }

    //선택한 이미지 경로 얻기
    private String getPath(Uri contentUri) {
        Log.d("@@getPath", contentUri.toString());
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(),    contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


}






