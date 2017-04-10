package com.flyou.pictureselecter.bean;

import java.io.Serializable;


public class ImageBean implements Serializable {

    private String path;
    private boolean isSelected;

    public ImageBean() {
    }

    public ImageBean(String path, boolean isSelected) {
        this.path = path;
        this.isSelected = isSelected;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }





}
