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
    private TiqavImageRepository mTiqavImageRepository;
    private LayoutInflater mLayoutInflater;
    private List<TiqavImageEntry> mImgEntryList;

    private static class ViewHolder {
        public ImageView hueImageView;
        public ImageView hueFavIcon;
    }

    public TiqavImageAdapter(Context context) {
        mTiqavImageRepository = TiqavImageRepository.getInstance(context);
        mLayoutInflater = LayoutInflater.from(context);
        mPicasso = Picasso.with(context);
        mImgEntryList = new ArrayList<>();
    }

    public void appendElements(List<TiqavImageEntry> tiqavImageEntryList) {
        mImgEntryList.addAll(tiqavImageEntryList);

        //お気に入りした日付順でソート
        mImgEntryList.sort((a, b) -> {
            if (a.faved_at == null || b.faved_at == null) {
                return 0;
            } else {
                return b.faved_at.compareTo(a.faved_at);
            }
        });
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

            convertView.setTag(holder);
        } else {
            //convertViewを一度でも作っていたらholderをタグに持っているはずなので流用
            holder = (ViewHolder) convertView.getTag();
        }

        holder.hueFavIcon.setOnClickListener(new FavClickEvent(i));
        toggleFavIcon(i, holder.hueFavIcon);

        //領域に合わせてロード
        mPicasso.load(mImgEntryList.get(i).getThumbUrl())
                .fit()
                .centerInside()
                .into(holder.hueImageView);
        return convertView;
    }

    private void toggleFavIcon(int index, ImageView favIconView) {
        if (mTiqavImageRepository.isFavoritedImage(mImgEntryList.get(index))) {
            favIconView.setImageResource(btn_star_big_on);
        } else {
            favIconView.setImageResource(btn_star_big_off);
        }
    }


    private class FavClickEvent implements View.OnClickListener {
        private int indexNo;

        public FavClickEvent(int i) {
            this.indexNo = i;
        }

        @Override
        public void onClick(View v) {
            if (v != null) {
                System.out.println("Faved: " + this.indexNo);
                //内部クラスとして外部クラスのフィールドを利用(できれば治したい)
                TiqavImageEntry entry = mImgEntryList.get(indexNo);
                mTiqavImageRepository.toggleFavorite(entry);

                toggleFavIcon(indexNo, (ImageView) v);
            }
        }
    }

}
