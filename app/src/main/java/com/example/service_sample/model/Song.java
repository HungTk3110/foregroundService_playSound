package com.example.service_sample.model;

import java.io.Serializable;

public class Song implements Serializable {

    private String title , single ;
    private  int img , resource;

    public Song(String title, String single, int img, int resource) {
        this.title = title;
        this.single = single;
        this.img = img;
        this.resource = resource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
