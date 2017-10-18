package com.game.siwasu17.donutshole.models;

import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Setter;
import com.github.gfx.android.orma.annotation.Table;

import java.sql.Timestamp;

@Table
public class ImageEntry {
    @PrimaryKey(auto = false)
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

    @Column(indexed = true)
    @Nullable
    public Timestamp faved_at;

    @Setter
    public ImageEntry(String id, String ext, int height, int width, String source_url, Timestamp faved_at) {
        this.id = id;
        this.ext = ext;
        this.height = height;
        this.width = width;
        this.source_url = source_url;
        this.faved_at = faved_at;
    }

    //サムネイル画像URL
    public String getThumbUrl() {
        return "http://img.tiqav.com/" + this.id + ".th.jpg";
    }

    //実画像URL
    public String getRealUrl() {
        return "http://img.tiqav.com/" + this.id + "." + this.ext;
    }

    /**
     * Orma helper methods
     */
    public static ImageEntry_Relation relation(OrmaDatabase orma) {
        return orma.relationOfImageEntry().orderByIdDesc();
    }


}
