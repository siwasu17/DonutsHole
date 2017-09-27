package com.game.siwasu17.donutshole;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * お気に入りした画像のURL, お気に入り日を保存する
 */
public class FavColumns implements BaseColumns {
    // URIパス
    public static final String PATH = "favs";
    // コンテントURI
    public static final Uri CONTENT_URI = Uri.parse("content://my_dictionary/" + PATH);
    //TODO: コンテンツプロバイダーをちゃんと理解する

    // テーブル名
    public static final String TABLE = "favs";


}
