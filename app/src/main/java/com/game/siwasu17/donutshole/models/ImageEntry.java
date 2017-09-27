package com.game.siwasu17.donutshole.models;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Setter;
import com.github.gfx.android.orma.annotation.Table;

@Table
public class ImageEntry{
    @PrimaryKey(auto=false)
    public String id;
    @Column
    public String ext;
    @Column
    public int height;
    @Column
    public int width;
    //URLだけど実体は無い
    @Column
    public String source_url;

    @Setter
    public ImageEntry(String id, String ext, int height, int width, String source_url) {
        this.id = id;
        this.ext = ext;
        this.height = height;
        this.width = width;
        this.source_url = source_url;
    }

    //サムネイル画像URL
    public String getThumbUrl(){
        return "http://img.tiqav.com/" + this.id + ".th.jpg";
    }

    //実画像URL
    public String getRealUrl(){
        return "http://img.tiqav.com/" + this.id + "." + this.ext;
    }
}
