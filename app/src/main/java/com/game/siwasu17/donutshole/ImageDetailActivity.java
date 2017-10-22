package com.game.siwasu17.donutshole;

import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.game.siwasu17.donutshole.fragments.HomeFragment;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_detail);

        File imgFilePath = new File(getIntent().getStringExtra(HomeFragment.IMAGE_CACHE_KEY));

        //シェア用にContentURIに変換
        Uri contentURI = FileProvider.getUriForFile(
                getApplicationContext(),
                "com.game.siwasu17.donutshole.fileprovider",
                imgFilePath
        );

        System.out.println("Content URL: " + contentURI);

        ImageView imageView = (ImageView) findViewById(R.id.detail_imageview);

        Picasso.with(this)
                .load(imgFilePath)
                .into(imageView);

        // builderの生成　ShareCompat.IntentBuilder.from(Context context);
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(this);

        Button shareButton = (Button)findViewById(R.id.share_button);
        shareButton.setOnClickListener(view -> {

            // アプリ一覧が表示されるDialogのタイトルの設定
            builder.setChooserTitle("アプリを選択");

            // シェアについてのタイトル
            builder.setSubject("Image from tiqav");

            // シェアするもの
            builder.setStream(contentURI);

            // シェアするタイプ
            builder.setType("image/jpeg");

            // Shareアプリ一覧のDialogの表示
            builder.startChooser();
        });

    }
}
