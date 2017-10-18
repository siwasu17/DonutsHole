package com.game.siwasu17.donutshole;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.siwasu17.donutshole.models.ImageEntry;
//import com.game.siwasu17.donutshole.models.OrmaDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;


public class HueAdapter extends BaseAdapter {
    private Context mContext;
    private Picasso mPicasso;
    private LayoutInflater mLayoutInflater;
    private List<ImageEntry> mImgEntryList;

    private static class ViewHolder {
        public ImageView hueImageView;
        public ImageView hueFavIcon;
    }

    public HueAdapter(Context context, List<ImageEntry> imgEntryList) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mPicasso = Picasso.with(context);
        mImgEntryList = imgEntryList;
    }

    @Override
    public int getCount() {
        if (mImgEntryList == null) {
            return 0;
        }
        return mImgEntryList.size();
    }

    @Override
    public Object getItem(int i) {
        return mImgEntryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.grid_item_hue, null);
            holder = new ViewHolder();
            holder.hueImageView = (ImageView) convertView.findViewById(R.id.hue_imageview);
            holder.hueFavIcon = (ImageView) convertView.findViewById(R.id.hue_fav_icon);
            holder.hueFavIcon.setOnClickListener(view -> {
                System.out.println("Faved: " + i);
                //お気に入りアイコン押下時の挙動
                ImageRepository.getInstance(mContext).addFavorite(mImgEntryList.get(i));
            });

            //TODO: 画像の番号をきちんと関連付けられるようにしないと正しい画像がお気に入りされない
            //毎回API呼び出ししてリストに入れているので、お気に入りが保存されない

            convertView.setTag(holder);
        } else {
            //convertViewを一度でも作っていたらholderをタグに持っているはずなので流用
            holder = (ViewHolder) convertView.getTag();
        }

        if(mImgEntryList.get(i).faved_at != null) {
            //お気に入りしているやつはやつはアイコン変える
            holder.hueFavIcon.setImageResource(btn_star_big_on);
        }else{
            holder.hueFavIcon.setImageResource(btn_star_big_off);
        }

        //領域に合わせてロード
        mPicasso.load(mImgEntryList.get(i).getThumbUrl())
                .fit()
                .centerInside()
                .into(holder.hueImageView);
        return convertView;
    }
}
