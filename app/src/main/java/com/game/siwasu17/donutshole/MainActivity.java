package com.game.siwasu17.donutshole;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.game.siwasu17.donutshole.models.ImageEntry;
import com.game.siwasu17.donutshole.services.TiqavService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Button mCallButton;

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

        mCallButton = (Button) findViewById(R.id.call_button);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTiqavService();
            }
        });
    }


    private void callTiqavService() {
        //Interfaceから実装を取得
        TiqavService tiqavService = ServiceFactory.createTiqavService();

        //Call<ImageEntry[]> apiCall = tiqavService.search("ちくわ");
        Observable<ImageEntry[]> apiCall = tiqavService.searchNewest();

        apiCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                            System.out.println(event.toString());
                        }
                        , Throwable::printStackTrace);

/*
        apiCall.enqueue(new Callback<ImageEntry[]>() {
            @Override
            public void onResponse(Call<ImageEntry[]> call, retrofit2.Response<ImageEntry[]> response) {
                if (response.isSuccessful()) {
                    //通信結果をオブジェクトで受け取る
                    ImageEntry[] result = response.body();

                    for (ImageEntry e : result) {
                        Log.d("ImageEntity", "http://img.tiqav.com/" + e.id + "." + e.ext);
                    }

                    String imgURL = "http://img.tiqav.com/" + result[0].id + "." + result[0].ext;

                    //普通のviewの生成
                    ImageView oImg = new ImageView(getApplicationContext());
                    URL url;
                    InputStream istream;
                    try {
                        //画像のURLを直うち
                        url = new URL(imgURL);
                        //インプットストリームで画像を読み込む
                        istream = url.openStream();
                        //読み込んだファイルをビットマップに変換
                        Bitmap oBmp = BitmapFactory.decodeStream(istream);
                        //ビットマップをImageViewに設定
                        oImg.setImageBitmap(oBmp);
                        //インプットストリームを閉じる
                        istream.close();
                    } catch (IOException e) {
                        // TODO 自動生成された catch ブロック
                        e.printStackTrace();
                    }
                } else {
                    //通信が成功したが、エラーcodeが返ってきた場合はこちら
                    Log.d("RETROFIT_TEST", "error_code" + response.code());
                }
            }

            @Override
            public void onFailure(Call<ImageEntry[]> call, Throwable t) {
                //通信が失敗した場合など
                t.printStackTrace();
            }
        });
        */

    }


}
