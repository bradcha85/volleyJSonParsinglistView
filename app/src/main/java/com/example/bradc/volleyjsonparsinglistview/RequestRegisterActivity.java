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

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private String uploadUrl = "http://192.168.219.187:8080/user/androidImageUploadTest";
    private Uri mImageCaptureUri;
    private String imagePath;
    private String absolutePath;
    private ImageView registerImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requestregistlayout);
        registerImageView = findViewById(R.id.registerImage);

        Button imagePreviewButton = findViewById(R.id.imagePreviewButton);
        imagePreviewButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });

    }

    //카메라에서 사진 촬영
    public void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }


//앨범에서 이미지 가져오기
    public void pickPhoto(){
        //앨범 호출
        Log.d("pickPhoto","test");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                mImageCaptureUri = data.getData();
                registerImageView.setImageURI(mImageCaptureUri);
                imagePath = getPath(mImageCaptureUri);
                imageUpload(imagePath);
            }

            case PICK_FROM_CAMERA: {
                //이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정
                //이후에 이미지 크롭 어플리케이션 호출
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                //CROPT할 이미지를 200*200 크기로 저장
                intent.putExtra("outputX", 200); //CROP한 이미지의 X축 크기
                intent.putExtra("outputY", 200); //CROP한 이미지의 Y축 크기
                intent.putExtra("aspectX", 1); //CROP한 이미지의 Y축 크기
                intent.putExtra("aspectY", 1); //CROP한 이미지의 Y축 크기
                intent.putExtra("scale", true); //CROP한 이미지의 Y축 크기
                intent.putExtra("return-data", true); //CROP한 이미지의 Y축 크기
                startActivityForResult(intent, CROP_FROM_IMAGE); //CROPT_FROM_IMAGE case문 이동
                break;
            }

            case CROP_FROM_IMAGE: {
                //크롭이 된 이후의 이미지를 넘겨 받습니다.
                //이미지뷰에 이미지를 보여준다거나
                //부가적인 작업 이후에 임시 파일을 삭제합니다.
                if (resultCode != RESULT_OK) {
                    return;
                }

                final Bundle extras = data.getExtras();

                //CROPT된 이미지를 저장하기 위한 FILE 경로
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/SmartWheel/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data"); // CROP 된 BITMAP
                    // iv_UserPhoto.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌

                    storeCropImage(photo, filePath);
                    absolutePath = filePath;
                    break;
                }
            }
        }
    }

    private void storeCropImage(Bitmap bitmap, String filePath){
        //SmartWheel 폴더를 생성하여 이미지를 저장하는 방식이다.
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartWheel";
        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists()) //SmartWheel 디렉터리에 폴더가 없다면(새로 이미지를 저장할 경우)
            directory_SmartWheel.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            //sendBroadcast를 통해 Cropt된 사진을 앨범에 보이도록 갱신한다.

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));
            out.flush();
            out.close();

        }catch (Exception e){
            e.printStackTrace();
        }


        }

    protected void imageUpload(final String imagePath){
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






