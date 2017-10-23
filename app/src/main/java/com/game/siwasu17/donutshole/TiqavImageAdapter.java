package com.game.siwasu17.donutshole;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.game.siwasu17.donutshole.models.TiqavImageEntry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;


public class TiqavImageAdapter extends BaseAdapter {
    private Picasso mPicasso;
    private LayoutInflater mLayoutInflater;
    private List<TiqavImageEntry> mImgEntryList;

    private static class ViewHolder {
        public ImageView hueImageView;
        public ImageView hueFavIcon;
    }

    public TiqavImageAdapter(Context context){
        mLayoutInflater = LayoutInflater.from(context);
        mPicasso = Picasso.with(context);
        mImgEntryList = new ArrayList<>();
    }

    public void appendElements(List<TiqavImageEntry> tiqavImageEntryList){
        this.mImgEntryList.addAll(tiqavImageEntryList);
    }

    @Override
    public int getCount() {
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
                //TODO: 画像の番号をきちんと関連付けられるようにしないと正しい画像がお気に入りされない
                //毎回API呼び出ししてリストに入れているので、お気に入りが保存されない

                //お気に入りアイコン押下時の挙動
                //TiqavImageRepository.getInstance(mContext).addFavorite(holder.hueImageEntry);
            });


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

        System.out.println("No." + i);

        //領域に合わせてロード
        mPicasso.load(mImgEntryList.get(i).getThumbUrl())
                .fit()
                .centerInside()
                .into(holder.hueImageView);
        return convertView;
    }
}
