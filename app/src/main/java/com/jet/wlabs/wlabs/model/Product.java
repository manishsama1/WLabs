package com.jet.wlabs.wlabs.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Manish on 1/9/2018.
 */

public class Product implements Parcelable {
    public static final String PRODUCT = "product";

    @SerializedName("productId")
    private String productId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("shortDescription")
    private String shortDescription;

    @SerializedName("longDescription")
    private String longDescription;

    @SerializedName("price")
    private String price;

    @SerializedName("productImage")
    private String productImageURL;

    @SerializedName("reviewRating")
    private double reviewRating;

    @SerializedName("reviewCount")
    private int reviewCount;

    @SerializedName("inStock")
    private boolean inStock;

    protected Product(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        shortDescription = in.readString();
        longDescription = in.readString();
        price = in.readString();
        productImageURL = in.readString();
        reviewRating = in.readDouble();
        reviewCount = in.readInt();
        inStock = in.readByte() != 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    public double getReviewRating() {
        return reviewRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public boolean isInStock() {
        return inStock;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productId);
        parcel.writeString(productName);
        parcel.writeString(shortDescription);
        parcel.writeString(longDescription);
        parcel.writeString(price);
        parcel.writeString(productImageURL);
        parcel.writeDouble(reviewRating);
        parcel.writeInt(reviewCount);
        parcel.writeByte((byte) (inStock ? 1 : 0));
    }
}
