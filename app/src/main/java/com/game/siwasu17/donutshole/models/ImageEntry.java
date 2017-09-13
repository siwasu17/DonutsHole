package com.game.siwasu17.donutshole.models;

import java.io.Serializable;

public class ImageEntry implements Serializable{
    public String id;
    public String ext;
    public int height;
    public int width;
    //URLだけど実体は無い
    public String source_url;

    //サムネイル画像URL
    public String getThumbUrl(){
        return "http://img.tiqav.com/" + this.id + ".th.jpg";
    }

    //実画像URL
    public String getRealUrl(){
        return "http://img.tiqav.com/" + this.id + "." + this.ext;
    }
}
