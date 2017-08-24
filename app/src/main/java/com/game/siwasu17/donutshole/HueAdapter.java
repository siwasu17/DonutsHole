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
import com.squareup.picasso.Picasso;

import java.util.List;


public class HueAdapter extends BaseAdapter {
    private Context mContext;
    private Picasso mPicasso;
    private LayoutInflater mLayoutInflater;
    private List<ImageEntry> mImgEntryList;

    private static class ViewHolder {
        public ImageView hueImageView;
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
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.grid_item_hue, null);
            holder = new ViewHolder();
            holder.hueImageView = (ImageView) convertView.findViewById(R.id.hue_imageview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //サムネイル  "http://img.tiqav.com/" + entry.id + ".th.jpg"
        //実画像  "http://img.tiqav.com/" + entry.id + "." + entry.ext
        //領域に合わせてロード
        mPicasso.load(String.format("http://img.tiqav.com/%s.th.jpg", mImgEntryList.get(i).id))
                .fit()
                .centerInside()
                .into(holder.hueImageView);
        return convertView;
    }
}
