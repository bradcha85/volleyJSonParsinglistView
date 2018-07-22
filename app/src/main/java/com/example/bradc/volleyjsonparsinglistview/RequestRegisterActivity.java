package com.example.bradc.volleyjsonparsinglistview;

import android.content.ClipData;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RequestRegisterActivity extends AppCompatActivity {

    static final int PICK_IMAGE_REQUEST = 1;
    private static Context context;
    private String uploadUrl = "http://192.168.0.25:8080/user/androidImageUploadTest";
    private String uploadUrl2 = "http://192.168.0.25:8080/user/androidImageUploadTest2";
    private Uri mImageCaptureUri;
    private String filePath;
    private String absolutePath;
    private ImageView registerImageView, registerImageView2;
    private TextView startAddr, arriveAddr, product, box, howMany, weight, price, memo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RequestRegisterActivity.context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requestregistlayout);
        registerImageView = findViewById(R.id.registerImage);
        registerImageView2 = findViewById(R.id.registerImage2);

        startAddr = findViewById(R.id.startAddr);
        arriveAddr = findViewById(R.id.arriveAddr);
        product = findViewById(R.id.product);
        box = findViewById(R.id.box);
        howMany = findViewById(R.id.howMany);
        weight = findViewById(R.id.weight);
        price = findViewById(R.id.price);
        memo = findViewById(R.id.memo);

        Button imagePreviewButton = findViewById(R.id.imagePreviewButton);
        Button requestRegistButton = findViewById(R.id.requestRegistButton);

        BottomNavigationView navigationView = (BottomNavigationView)findViewById(R.id.main_navbar);
        navigationView.setOnNavigationItemSelectedListener(navigationListener);

        imagePreviewButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                galleryIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE_REQUEST);
                //imageBrowse();
            }
        });

        requestRegistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath != null) {

                    View focusView = null;
                    boolean cancel = false;


                    if (TextUtils.isEmpty(startAddr.getText().toString())){
                        startAddr.setError("입력하세요");
                        focusView = startAddr;
                        cancel = true;
                    }

                    else if (TextUtils.isEmpty(arriveAddr.getText().toString())){
                        arriveAddr.setError("입력하세요");
                        focusView = arriveAddr;
                        cancel = true;
                    }

                    else if (TextUtils.isEmpty(product.getText().toString())){
                        product.setError("입력하세요");
                        focusView = product;
                        cancel = true;
                    }

                    else if (TextUtils.isEmpty(box.getText().toString())){
                        box.setError("입력하세요");
                        focusView = box;
                        cancel = true;
                    }

                    else if (TextUtils.isEmpty(howMany.getText().toString())){
                        howMany.setError("입력하세요");
                        focusView = howMany;
                        cancel = true;
                    }

                    else if (TextUtils.isEmpty(weight.getText().toString())){
                        weight.setError("입력하세요");
                        focusView = weight;
                        cancel = true;
                    }

                    else if (TextUtils.isEmpty(price.getText().toString())){
                        price.setError("입력하세요");
                        focusView = price;
                        cancel = true;
                    }

                    if (cancel) {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        focusView.requestFocus();
                    }
                    /*
                    if (nullCheck(startAddr, "출발지를") == false) return
                    if (nullCheck(arriveAddr, "도착지를") == false) return;
                    if (nullCheck(product, "제품종류를") == false) return;
                    if (nullCheck(box, "포장종류를") == false) return;
                    if (nullCheck(howMany, "포장수량을") == false) return;
                    if (nullCheck(weight, "총중량을") == false) return;
                    if (nullCheck(price, "배송요금을") == false) return;*/
                    if(NumericCheck("포장수량은", howMany.getText().toString()) == false) return;
                    if(NumericCheck("총중량은", weight.getText().toString()) == false) return;
                    if(NumericCheck("배송요금은", price.getText().toString()) == false) return;
                    requestUpload(filePath);
                } else {
                    Toast.makeText(getApplicationContext(), "이미지를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Uri picUri = data.getData();
                filePath = getPath(picUri);
                registerImageView.setImageURI(picUri);
            }
        }
    }

    protected void requestUpload(final String imagePath) {
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
        smr.addMultipartParam("startAddr", "text/plain", startAddr.getText().toString());
        smr.addMultipartParam("arriveAddr", "text/plain", arriveAddr.getText().toString());
        smr.addMultipartParam("product", "text/plain", product.getText().toString());
        smr.addMultipartParam("box", "text/plain", box.getText().toString());
        smr.addMultipartParam("howMany", "text/plain", howMany.getText().toString());
        smr.addMultipartParam("weight", "text/plain", weight.getText().toString());
        smr.addMultipartParam("price", "text/plain", price.getText().toString());
        smr.addMultipartParam("memo", "text/plain", memo.getText().toString());
        VolleySingleton.getInstance(this).getRequestQueue().add(smr);
    }

    //선택한 이미지 경로 얻기
    private String getPath(Uri contentUri) {
        Log.d("@@getPath", contentUri.toString());
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    //입력한 값의 null체크
    public boolean nullCheck(TextView textView, String message){
            if((TextUtils.isEmpty(textView.getText().toString()))) {
            //Toast.makeText(RequestRegisterActivity.context, message + " 입력해주세요.", Toast.LENGTH_SHORT).show();
            textView.setError("입력해주세요.");
            return false;
        }
        return true;
    }
    //입력한 String 값에 숫자가 들어가있는지 체크
    public boolean NumericCheck(String status,String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(RequestRegisterActivity.context, status +" 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.requestRegistration:
                    Intent requestRegisterIntent = new Intent(context, RequestRegisterActivity.class);
                    context.startActivity(requestRegisterIntent);
                    return true;
                case R.id.main:
                    Intent mainActivityIntent = new Intent(context, MainActivity.class);
                    context.startActivity(mainActivityIntent);
                    return true;
                case R.id.myInformation:
                    Toast.makeText(getApplicationContext(), "myInfo", Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }
    };
}






