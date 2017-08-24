package com.game.siwasu17.donutshole;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.game.siwasu17.donutshole.models.ImageEntry;
import com.game.siwasu17.donutshole.services.TiqavService;
import com.squareup.picasso.Picasso;

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
    private List<ImageEntry> mImageEntryList = new ArrayList<>();

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
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
                    if(!loading) {
                        int pos = absListView.getFirstVisiblePosition();
                        //ロード中でなければロード
                        System.out.println("Load! " + pos);

                        loading = true;
                        callTiqavService();
                    }
                }else{
                    loading = false;
                }

            }
        });
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
                            // adapterで関連付けられてからはここに要素追加するだけでOK
                            mImageEntryList.addAll(Arrays.asList(imgs));
                            if(null == mGridView.getAdapter()){
                                //初回のみアダプタを生成
                                mGridView.setAdapter(new HueAdapter(this, mImageEntryList));
                            }
                            mGridView.invalidate();
                        }
                        , Throwable::printStackTrace
                );
    }
}
