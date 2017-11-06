package com.game.siwasu17.donutshole;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.game.siwasu17.donutshole.fragments.HomeFragment;
import com.game.siwasu17.donutshole.models.TiqavImageEntry;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDetailActivity extends AppCompatActivity {

    public static final String FILEPROVIDER_AUTHORITY = "com.game.siwasu17.donutshole.fileprovider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_detail);

        TiqavImageEntry imageEntry = (TiqavImageEntry) getIntent().getSerializableExtra(HomeFragment.IMAGE_ENTRY_KEY);

        ImageView imageView = (ImageView) findViewById(R.id.detail_imageview);
        Picasso.with(this)
                .load(imageEntry.getRealUrl())
                .into(imageView);

        prepareShareButton(imageEntry);
    }

    //シェアボタンの準備
    private void prepareShareButton(TiqavImageEntry imageEntry) {
        Context context = getApplicationContext();
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(this);

        Target mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                System.out.println("Bitmap get complete");

                File cachePath = new File(context.getCacheDir(), "images");
                cachePath.mkdirs();
                File filePath = new File(cachePath, imageEntry.id + ".jpg");

                try (FileOutputStream stream = new FileOutputStream(filePath)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Bitmap save: " + filePath.getAbsolutePath());

                //シェア用にContentURIに変換
                Uri contentURI = FileProvider.getUriForFile(
                        context,
                        FILEPROVIDER_AUTHORITY,
                        filePath
                );

                Button shareButton = (Button) findViewById(R.id.share_button);
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
                //準備が整ったらボタンを押せるようにする
                shareButton.setEnabled(true);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                System.out.println("Bitmap load failed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        //やや冗長だが、Picasso経由で画像をロードしてローカルファイルとして保存させている
        Picasso.with(context)
                .load(imageEntry.getRealUrl())
                .into(mTarget);
    }

}
