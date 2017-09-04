package com.game.siwasu17.donutshole;

import android.app.ActionBar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        Picasso.with(this)
                .load(imageUrl)
                .fit()
                .centerInside()
                .into(imageView);

        // builderの生成　ShareCompat.IntentBuilder.from(Context context);
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(this);

        Button shareButton = (Button)findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // アプリ一覧が表示されるDialogのタイトルの設定
                builder.setChooserTitle("アプリを選択");

                // シェアするタイトル
                builder.setSubject("Image from tiqav");

                // シェアするテキスト
                builder.setText(imageUrl);

                // シェアするタイプ
                builder.setType("text/plain");

                // Shareアプリ一覧のDialogの表示
                builder.startChooser();
            }
        });

    }
}
