package com.example.shoppingapp.model;

import java.util.ArrayList;

public class VerticalList {
    String title;
    String scrollType;
    ArrayList<ListModel> arrayList;

    public String getScrollType() {
        return scrollType;
    }

    public void setScrollType(String scrollType) {
        this.scrollType = scrollType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ListModel> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<ListModel> arrayList) {
        this.arrayList = arrayList;
    }


}
