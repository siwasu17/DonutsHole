package com.game.siwasu17.donutshole;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.game.siwasu17.donutshole.models.ImageEntry;
import com.squareup.picasso.Picasso;

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_detail);

        ImageEntry entry = (ImageEntry) getIntent().getSerializableExtra(MainActivity.IMAGE_ENTRY_KEY);
        System.out.println("ImageEntry OBJ: " + entry);

        String imageUrl = "http://img.tiqav.com/" + entry.id + "." + entry.ext;
        ImageView imageView = (ImageView) findViewById(R.id.detail_imageview);

        //Picassoを使いまわせるとキャッシュ使えそうだが。。。

        Picasso.with(this)
                .load(imageUrl)
                .fit()
                .centerInside()
                .into(imageView);

    }
}
