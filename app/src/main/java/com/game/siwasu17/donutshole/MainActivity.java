package com.game.siwasu17.donutshole;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.game.siwasu17.donutshole.models.ImageEntry;
import com.game.siwasu17.donutshole.services.TiqavService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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


    private void callTiqavService(){
        //Interfaceから実装を取得
        TiqavService tiqavService = ServiceFactory.createTiqavService();

        //Call<ImageEntry[]> apiCall = tiqavService.search("ちくわ");
        Call<ImageEntry[]> apiCall = tiqavService.searchNewest();
        //実行
        apiCall.enqueue(new Callback<ImageEntry[]>() {
            @Override
            public void onResponse(Call<ImageEntry[]> call, retrofit2.Response<ImageEntry[]> response) {
                if (response.isSuccessful()) {
                    //通信結果をオブジェクトで受け取る
                    ImageEntry[] result = response.body();

                    for(ImageEntry e : result){
                        Log.d("ImageEntity", "http://img.tiqav.com/" + e.id + "." + e.ext);
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

    }



}
