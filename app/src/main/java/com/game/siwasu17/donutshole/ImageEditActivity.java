package com.game.siwasu17.donutshole;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_edit);

        String imgSavedPath = getIntent().getStringExtra(ImageDetailActivity.IMAGE_SAVED_PATH_KEY);
        System.out.println("Edit target: " + imgSavedPath);

        ImageView imageView = (ImageView) findViewById(R.id.edit_imageview);
        Picasso.with(this)
                .load("file://" + imgSavedPath)
                .into(imageView);

    }
}
