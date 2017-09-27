package com.game.siwasu17.donutshole;

import com.game.siwasu17.donutshole.services.TiqavService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {
    public static OkHttpClient buildJsonHttpClient(){

        //okhttpのclient作成
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                //header設定
                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        //ログ出力設定
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        httpClient.addInterceptor(logging);

        //クライアント生成
        return httpClient.build();
    }

    public static TiqavService createTiqavService(){
        OkHttpClient client = buildJsonHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.tiqav.com/")//基本のurl設定
                .addConverterFactory(GsonConverterFactory.create())//Gsonの使用
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)//カスタマイズしたokhttpのクライアントの設定
                .build();

        //Interfaceから実装を取得
        return retrofit.create(TiqavService.class);
    }
}
