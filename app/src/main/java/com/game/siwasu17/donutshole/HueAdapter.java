package com.game.siwasu17.donutshole;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yasu on 2017/07/12.
 */

public class HueAdapter extends BaseAdapter {
    private Context mContext;
    private Picasso mPicasso;
    private LayoutInflater mLayoutInflater;
    private List<String> mUrlList;

    private static class ViewHolder {
        public ImageView hueImageView;
        public TextView hueTextView;
    }

    public HueAdapter(Context context, List<String> urlList) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mPicasso = Picasso.with(context);
        mUrlList = urlList;
    }

    @Override
    public int getCount() {
        if (mUrlList == null) {
            return 0;
        }
        return mUrlList.size();
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
            holder.hueTextView = (TextView) convertView.findViewById(R.id.hue_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //領域に合わせてロード
        mPicasso.load(mUrlList.get(i)).fit().into(holder.hueImageView);
        holder.hueTextView.setText("No. " + i);

        return convertView;
    }
}
