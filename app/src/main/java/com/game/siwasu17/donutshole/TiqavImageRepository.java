package com.game.siwasu17.donutshole;

import android.content.Context;

import com.game.siwasu17.donutshole.models.TiqavImageEntry;
import com.game.siwasu17.donutshole.models.OrmaDatabase;
import com.game.siwasu17.donutshole.services.TiqavService;
import com.github.gfx.android.orma.AccessThreadConstraint;

import java.sql.Timestamp;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ネット上の画像リストを返すリポジトリ
 * 画像をお気に入りしているかどうかの情報も合わせて管理する
 */
public class TiqavImageRepository {
    private static TiqavImageRepository instance;
    private TiqavService mTiqavService;
    private OrmaDatabase mOrmaDatabase;

    private TiqavImageRepository(Context context) {
        this.mTiqavService = ServiceFactory.createTiqavService();
        this.mOrmaDatabase = OrmaDatabase
                .builder(context)
                .writeOnMainThread(BuildConfig.DEBUG ? AccessThreadConstraint.WARNING : AccessThreadConstraint.NONE)
                .build();
    }

    //利用都度に利用場所のcontextに紐付いて動くことにする
    public static TiqavImageRepository getInstance(Context context){
        if(instance == null){
            instance = new TiqavImageRepository(context);
        }
        return instance;
    }

    /**
     * 関数型インタフェースを渡して、非同期処理完了時にどんな処理をさせるかは外側で定義する
     */
    public void subscribeRandomImages(Consumer<TiqavImageEntry[]> consumer) {
        mTiqavService.searchRandom()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer,Throwable::printStackTrace);
    }

    public void subscribeSearchImages(String query, Consumer<TiqavImageEntry[]> consumer) {
        mTiqavService.search(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer,Throwable::printStackTrace);
    }

    public void addFavorite(TiqavImageEntry tiqavImageEntry){
        //お気に入り時刻を更新しながらDBにupsert
        tiqavImageEntry.faved_at = new Timestamp(System.currentTimeMillis());
        TiqavImageEntry.relation(mOrmaDatabase).upsert(tiqavImageEntry);
    }

    public void removeFavorite(TiqavImageEntry tiqavImageEntry) {
        TiqavImageEntry.relation(mOrmaDatabase).idEq(tiqavImageEntry.id).deleter().execute();
    }

    public List<TiqavImageEntry> getFavoriteImages(){
        //お気に入り時刻が入っているものを返す
        return TiqavImageEntry.relation(mOrmaDatabase).faved_atIsNotNull().selector().toList();
    }
}
