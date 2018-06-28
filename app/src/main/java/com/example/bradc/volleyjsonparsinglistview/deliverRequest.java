package com.example.bradc.volleyjsonparsinglistview;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class deliverRequest implements Serializable {

    private int customerNo; // 의뢰자 회원번호
    private String startAddr; // 출발장소
    private String arriveAddr; // 도착장소
    private String product; // 제품종류
    private String box; //포장종류
    private int howMany; //포장수량
    private int weight; //총중량
    private int price; //배송의뢰비용
    private String memo; //고객요청사항
    private String thumbnailImageUrl; //썸네일 이미지 url
    private ArrayList<String> imageList;


    public deliverRequest() {
      super();
    }

    public int getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(int customerNo) {
        this.customerNo = customerNo;
    }

    public String getStartAddr() {
        return startAddr;
    }

    public void setStartAddr(String startAddr) {
        this.startAddr = startAddr;
    }

    public String getArriveAddr() {
        return arriveAddr;
    }

    public void setArriveAddr(String arriveAddr) {
        this.arriveAddr = arriveAddr;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public int getHowMany() {
        return howMany;
    }

    public void setHowMany(int howMany) {
        this.howMany = howMany;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public String toString() {
        return "deliverRequest{" +
                "customerNo=" + customerNo +
                ", startAddr='" + startAddr + '\'' +
                ", arriveAddr='" + arriveAddr + '\'' +
                ", product='" + product + '\'' +
                ", box='" + box + '\'' +
                ", howMany=" + howMany +
                ", weight=" + weight +
                ", price=" + price +
                ", memo='" + memo + '\'' +
                ", thumbnailImageUrl='" + thumbnailImageUrl + '\'' +
                ", imageList=" + imageList +
                '}';
    }
}
