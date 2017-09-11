package com.game.siwasu17.donutshole;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.game.siwasu17.donutshole.models.ImageEntry;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_detail);

        ImageEntry entry = (ImageEntry) getIntent().getSerializableExtra(MainActivity.IMAGE_ENTRY_KEY);
        System.out.println("ImageEntry OBJ: " + entry);

        String imageUrl = "http://img.tiqav.com/" + entry.id + "." + entry.ext;
        ImageView imageView = (ImageView) findViewById(R.id.detail_imageview);

        RequestCreator requestCreator = Picasso.with(this)
                .load(imageUrl)
                .fit()
                .centerInside();

        //Viewに画像を入れる(非同期)
        requestCreator.into(imageView);

        /*
        Bitmap bitmap = null;

        try {
        //FIXME: このメソッドを呼ぶと落ちる
            bitmap = requestCreator.get();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        // builderの生成　ShareCompat.IntentBuilder.from(Context context);
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(this);

        Button shareButton = (Button)findViewById(R.id.share_button);
        shareButton.setOnClickListener(view -> {


            Context context = getApplicationContext();
            Uri contentUri = null;
            /*
            try{
                //画像だけ同期的に取得
                //Bitmap bitmap = requestCreator.get();
                System.out.println(context);
                //TODO: このへん要精査

                File cachePath = new File(context.getCacheDir(), "images");
                cachePath.mkdirs(); // don't forget to make the directory
                FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
                //取得した画像をキャッシュに書き込んでやる
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
                System.out.println("URI: " + cachePath.toString());

                //ContentProviderのパスを取得
                contentUri = FileProvider.getUriForFile(context, "com.game.siwasu17.donutshole.fileprovider", cachePath);


            }catch (IOException e){
                e.printStackTrace();
            }
            */


            // アプリ一覧が表示されるDialogのタイトルの設定
            builder.setChooserTitle("アプリを選択");

            // シェアについてのタイトル
            builder.setSubject("Image from tiqav");

            // シェアするもの
            //builder.setStream(contentUri);
            builder.setText(imageUrl);

            // シェアするタイプ
            //builder.setType("image/png");
            builder.setType("text/plain");

            // Shareアプリ一覧のDialogの表示
            builder.startChooser();
        });

    }
}
