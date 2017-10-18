package com.game.siwasu17.donutshole;

import android.app.Application;
import android.content.Context;
import android.media.Image;

import com.game.siwasu17.donutshole.models.ImageEntry;
import com.game.siwasu17.donutshole.models.ImageEntry_Relation;
import com.game.siwasu17.donutshole.models.ImageEntry_Selector;
import com.game.siwasu17.donutshole.models.OrmaDatabase;
import com.game.siwasu17.donutshole.services.TiqavService;
import com.github.gfx.android.orma.AccessThreadConstraint;
import com.github.gfx.android.orma.Inserter;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * ImageEntryを返すリポジトリ
 */
public class ImageRepository {
    private static ImageRepository instance;
    private TiqavService mTiqavService;
    private OrmaDatabase mOrmaDatabase;

    private ImageRepository(Context context) {
        this.mTiqavService = ServiceFactory.createTiqavService();
        this.mOrmaDatabase = OrmaDatabase
                .builder(context)
                .writeOnMainThread(BuildConfig.DEBUG ? AccessThreadConstraint.WARNING : AccessThreadConstraint.NONE)
                .build();
    }

    //利用都度に利用場所のcontextに紐付いて動くことにする
    public static ImageRepository getInstance(Context context){
        if(instance == null){
            instance = new ImageRepository(context);
        }
        return instance;
    }

    /**
     * 関数型インタフェースを渡して、非同期処理完了時にどんな処理をさせるかは外側で定義する
     */
    public void subscribeRandomImages(Consumer<ImageEntry[]> consumer) {
        mTiqavService.searchRandom()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer,Throwable::printStackTrace);
    }

    public void subscribeSearchImages(String query, Consumer<ImageEntry[]> consumer) {
        mTiqavService.search(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer,Throwable::printStackTrace);
    }

    public void addFavorite(ImageEntry imageEntry){
        //お気に入り時刻を更新しながらDBにupsert
        imageEntry.faved_at = new Timestamp(System.currentTimeMillis());
        ImageEntry.relation(mOrmaDatabase).upsert(imageEntry);
    }

    public void removeFavorite(ImageEntry imageEntry) {
        ImageEntry.relation(mOrmaDatabase).idEq(imageEntry.id).deleter().execute();
    }

    public List<ImageEntry> getFavoriteImages(){
        //お気に入り時刻が入っているものを返す
        return ImageEntry.relation(mOrmaDatabase).faved_atIsNotNull().selector().toList();
    }


}
