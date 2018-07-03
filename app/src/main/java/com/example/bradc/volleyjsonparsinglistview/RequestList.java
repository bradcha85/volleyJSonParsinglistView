package com.example.bradc.volleyjsonparsinglistview;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestList {
    Context context;
    private RequestQueue mRequestQueue = VolleySingleton.getInstance(context).getRequestQueue();
    private static RequestList instance = new RequestList();
    private ArrayList<deliverRequest> reqList;

    private RequestList(){
    }

    public static RequestList getInstance(){
        return instance;
    }

    public void setReqList(ArrayList<deliverRequest> reqList){
        this.reqList = reqList;
        Log.d("singleton", this.reqList.toString());
    }

    public ArrayList<deliverRequest> getReqList() {
        return reqList;
    }

    public void requestRender(String url, ListView listView, Context context){
        final ListView lv = listView;
        final Context ctx = context;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("dd","dd");
                            JSONObject wrapObject = new JSONObject(response);
                            JSONArray jsonArray = new JSONArray(wrapObject.getString("list"));
                            reqList = new ArrayList<deliverRequest>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dataJsonObject = jsonArray.getJSONObject(i);
                                int customerNo = Integer.parseInt(dataJsonObject.getString("customerNo"));
                                String startAddr = dataJsonObject.getString("startAddr");
                                String arriveAddr = dataJsonObject.getString("arriveAddr");
                                String product = dataJsonObject.getString("product");
                                String box = dataJsonObject.getString("box");
                                int howMany = dataJsonObject.getInt("howMany");
                                int weight = dataJsonObject.getInt("weight");
                                int price = dataJsonObject.getInt("price");
                                String memo = dataJsonObject.getString("memo");
                                JSONArray reqFileList = dataJsonObject.getJSONArray("reqFileList");
                                String ThumbnailUrl = "http://18.205.185.138:8080/request/productThumbnailImage/"
                                        + reqFileList.getJSONObject(0).getString("randomFileName");
                                ArrayList<String> imageList = new ArrayList<>();

                                for (int j = 0; j < reqFileList.length(); j++) {
                                    imageList.add(reqFileList.getJSONObject(j).getString("randomFileName"));
                                }
                                deliverRequest dRequest = new deliverRequest();
                                dRequest.setStartAddr(startAddr);
                                dRequest.setArriveAddr(arriveAddr);
                                dRequest.setProduct(product);
                                dRequest.setBox(box);
                                dRequest.setHowMany(howMany);
                                dRequest.setWeight(weight);
                                dRequest.setPrice(price);
                                dRequest.setMemo(memo);
                                dRequest.setImageList(imageList);
                                dRequest.setThumbnailImageUrl(ThumbnailUrl);
                                reqList.add(dRequest);
                            }
                           setReqList(reqList);
                           RequestAdapter reqAdapter = new RequestAdapter(ctx, R.layout.request, reqList);
                           reqAdapter.notifyDataSetChanged();
                           lv.setAdapter(reqAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // RequestQueue에 현재 Task를 추가해준다.
        mRequestQueue.add(stringRequest);
    }

}


