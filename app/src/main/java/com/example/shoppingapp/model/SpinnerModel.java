package com.example.shoppingapp.model;



import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class SpinnerModel {
    @SerializedName("country_code")
    private String countrycode;
    @SerializedName("country_name")
    private String countryName;
    @SerializedName("country_id")
    private String countryId;

    public SpinnerModel() {

    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @NotNull
    @Override
    public String toString() {
        return "SpinnerModel{" +
                "countrycode='" + countrycode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", countryId='" + countryId + '\'' +
                '}';
    }
}
