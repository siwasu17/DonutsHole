package com.game.siwasu17.donutshole;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.siwasu17.donutshole.models.ImageEntry;
import com.game.siwasu17.donutshole.services.TiqavService;
import com.squareup.picasso.Picasso;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Button mCallButton;
    private ImageView mImageView;
    private Picasso mPicasso;

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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mImageView = (ImageView) findViewById(R.id.image_view);
        mPicasso = Picasso.with(this);

        mCallButton = (Button) findViewById(R.id.call_button);
        mCallButton.setOnClickListener(view -> callTiqavService());
    }


    private void callTiqavService() {

        //Interfaceから実装を取得
        TiqavService tiqavService = ServiceFactory.createTiqavService();

        //Call<ImageEntry[]> apiCall = tiqavService.search("ちくわ");
        Observable<ImageEntry[]> apiCall = tiqavService.searchNewest();

        apiCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                            System.out.println(event);
                            mPicasso.load("http://img.tiqav.com/" + event[0].id + "." + event[0].ext).into(mImageView);
                        }
                        , Throwable::printStackTrace
                );

    }


}
