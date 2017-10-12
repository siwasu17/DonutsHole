package com.game.siwasu17.donutshole;

import android.content.Context;
import android.media.Image;

import com.game.siwasu17.donutshole.models.ImageEntry;
import com.game.siwasu17.donutshole.models.ImageEntry_Relation;
import com.game.siwasu17.donutshole.models.ImageEntry_Selector;
import com.game.siwasu17.donutshole.models.OrmaDatabase;
import com.game.siwasu17.donutshole.services.TiqavService;
import com.github.gfx.android.orma.AccessThreadConstraint;
import com.github.gfx.android.orma.Inserter;

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
    private TiqavService mTiqavService;
    private OrmaDatabase mOrmaDatabase;

    public ImageRepository(Context context) {
        this.mTiqavService = ServiceFactory.createTiqavService();
        this.mOrmaDatabase = OrmaDatabase
                .builder(context)
                .writeOnMainThread(BuildConfig.DEBUG ? AccessThreadConstraint.WARNING : AccessThreadConstraint.NONE)
                .build();
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
        ImageEntry.relation(mOrmaDatabase).upsert(imageEntry);
    }

    public void removeFavorite(ImageEntry imageEntry) {
        //FIXME: エントリ削除を実装する
    }

    public List<ImageEntry> getFavoriteImages(){
        //TODO: ちゃんとfaved_atがあるものだけ抽出したい
        //これもsubscribe系にしたほうが統一感あってしっくりはくるかも
        return mOrmaDatabase.selectFromImageEntry().toList();
    }


/*
    public List<ImageEntry> getFavedImages(){
        OrmaDatabase orma = OrmaDatabase
                .builder(getActivity())
                .writeOnMainThread(BuildConfig.DEBUG ? AccessThreadConstraint.WARNING : AccessThreadConstraint.NONE)
                .build();
        Inserter<ImageEntry> inserter = orma.prepareInsertIntoImageEntry();
        inserter.executeAll(Arrays.asList(imgs));

        ImageEntry_Selector selector = orma.selectFromImageEntry()
                .orderByIdAsc();
        ImageEntry saved = selector.get(0);

        System.out.println(saved.getRealUrl());
    }
    */

}
