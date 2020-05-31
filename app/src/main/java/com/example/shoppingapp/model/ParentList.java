package com.example.shoppingapp.model;

import java.util.ArrayList;

public class ParentList {
    String title;
    String scrollType;

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

    public ArrayList<ChildList> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<ChildList> arrayList) {
        this.arrayList = arrayList;
    }

    ArrayList<ChildList> arrayList;

}
