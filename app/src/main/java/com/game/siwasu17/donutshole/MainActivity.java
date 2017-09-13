package com.game.siwasu17.donutshole;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.game.siwasu17.donutshole.models.ImageEntry;
import com.game.siwasu17.donutshole.services.TiqavService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private ImageView mImageView;

    private Button mCallButton;
    private GridView mGridView;
    private HueAdapter mHueAdapter;
    private List<ImageEntry> mImageEntryList = new ArrayList<>();

    public static final String IMAGE_CACHE_KEY = "IMAGE_CACHE_KEY";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextMessage = (TextView) findViewById(R.id.message);
        //mImageView = (ImageView) findViewById(R.id.image_view);
        //BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // グリッドビュー
        mGridView = (GridView) findViewById(R.id.gridview);

        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean loading = true;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /*
                System.out.println(
                        MessageFormat.format("FirstVisPos: {0}, VisibleItemCount: {1}, total: {2}",
                                firstVisibleItem, visibleItemCount, totalItemCount)
                );
                */

                if (totalItemCount == (firstVisibleItem + visibleItemCount)) {
                    if (!loading) {
                        int pos = absListView.getFirstVisiblePosition();
                        //ロード中でなければロード
                        System.out.println("Load! " + pos);

                        loading = true;
                        callTiqavService();
                    }
                } else {
                    loading = false;
                }

            }
        });


        //詳細画面への遷移
        mGridView.setOnItemClickListener((parent, view, position, id) -> {
            ImageEntry imageEntry = mImageEntryList.get(position);
            System.out.println(
                    MessageFormat.format("pos: {0}, id: {1}, ImageID: {2}",
                            position, id, imageEntry.id)
            );

            //Bitmap取得のためのcallback
            Target mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    System.out.println("Bitmap get complete");

                    File cachePath = new File(getApplicationContext().getCacheDir(), "images");
                    cachePath.mkdirs();
                    File filePath = new File(cachePath,imageEntry.id + ".jpg");

                    try(FileOutputStream stream = new FileOutputStream(filePath)){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                    System.out.println("Bitmap save: " + filePath.getAbsolutePath());

                    Intent intent = new Intent(MainActivity.this, ImageDetailActivity.class);
                    //キャッシュファイルのパスを送信
                    intent.putExtra(IMAGE_CACHE_KEY, filePath.toString());
                    startActivity(intent);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    System.out.println("Bitmap load failed");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            //実画像を取得して、画像詳細画面へ遷移
            //callbackが重なるためこんな作りになってる
            Picasso.with(this)
                    .load(imageEntry.getRealUrl())
                    .into(mTarget);

        });

        //画像配列とそのアダプタを生成
        mHueAdapter = new HueAdapter(this, mImageEntryList);
        //初期画像のロード
        callTiqavService();
    }


    private void callTiqavService() {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();

        //Interfaceから実装を取得
        TiqavService tiqavService = ServiceFactory.createTiqavService();

        Observable<ImageEntry[]> apiCall = tiqavService.searchRandom();

        apiCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imgs -> {
                            System.out.println(imgs);
                            //リストに画像要素を追加していく
                            // adapterで関連付けられているので要素追加するだけでOK
                            mImageEntryList.addAll(Arrays.asList(imgs));
                            if (null == mGridView.getAdapter()) {
                                //初回だけGridViewにアダプタを関連付け
                                mGridView.setAdapter(mHueAdapter);
                            }

                            mGridView.invalidate();
                        }
                        , Throwable::printStackTrace
                );
    }
}
