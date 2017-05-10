package com.homechart.app.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.homechart.app.R;
import com.homechart.app.commont.utils.CommontUtils;
import com.homechart.app.commont.utils.ImageUtils;
import com.homechart.app.commont.utils.UIUtils;
import com.homechart.app.glide.GlideImgManager;
import com.homechart.app.home.bean.PicDateItemBean;

import java.util.List;

/**
 * Created by GPD on 2017/3/1.
 */

public class HomePicListAdapter extends BaseAdapter {

    private List<PicDateItemBean> list;
    private Context activity;
    private TagClickListener mClickListener;

    public HomePicListAdapter(List<PicDateItemBean> list, Context activity) {
        this.list = list;
        this.activity = activity;
        Fresco.initialize(activity);
    }

    public void setClickListener(TagClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public int getCount() {

        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null == list ? 0 : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MyHolder myHolder;
        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_homepic_list, null);
            myHolder.iv_item_pic = (ImageView) convertView.findViewById(R.id.iv_homepic_item);
            myHolder.iv_item_head = (ImageView) convertView.findViewById(R.id.iv_item_head);
            myHolder.tv_item_space = (TextView) convertView.findViewById(R.id.tv_item_space);
            myHolder.tv_item_style = (TextView) convertView.findViewById(R.id.tv_item_style);
            myHolder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_pic_name);
            myHolder.tv_chat_desiner = (TextView) convertView.findViewById(R.id.tv_chat_desiner);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        if (list.get(position).getBig_image_url().equals((myHolder.iv_item_head.getTag()))) {
        } else {
            // 设置图片的宽高
            myHolder.iv_item_head.setTag(list.get(position).getBig_image_url());
            CommontUtils.setPicHeighAndWidth(myHolder.iv_item_pic, activity, list.get(position).getBig_image_width(), list.get(position).getBig_image_height(), 40);
            myHolder.iv_item_pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            GlideImgManager.glideLoader(activity,list.get(position).getBig_image_url(), UIUtils.getColor(R.color.white),UIUtils.getColor(R.color.white),myHolder.iv_item_pic,1);
            ImageUtils.displayHalfRoundImage(list.get(position).getBig_image_url(), myHolder.iv_item_pic);
            myHolder.iv_item_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onClickPic(position);
                }
            });
            myHolder.tv_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onJumpDesinerCenter(position);
                }
            });
            myHolder.tv_chat_desiner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onJumpChat(position);
                }
            });
            ImageUtils.displayRoundImage(list.get(position).getAvatar(), myHolder.iv_item_head);
            myHolder.iv_item_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onJumpDesinerCenter(position);
                }
            });
            String name = list.get(position).getNickname();
            if (name.length() > 15) {
                name = name.substring(0, 15) + "...";
            }
            myHolder.tv_item_name.setText(name);
            if (!TextUtils.isEmpty(list.get(position).getStyle_tag_name())) {
                myHolder.tv_item_style.setText("#" + list.get(position).getStyle_tag_name());
            }
            if (!TextUtils.isEmpty(list.get(position).getSpace_tag_name())) {
                myHolder.tv_item_space.setText("#" + list.get(position).getSpace_tag_name());
            }
            myHolder.tv_item_style.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String style = myHolder.tv_item_style.getText().toString();
                    if (!TextUtils.isEmpty(style) && mClickListener != null)
                        mClickListener.onClickStyle(style.substring(1), list.get(position).getStyle_tag_id(), list.get(position).getNickname());
                }
            });
            myHolder.tv_item_space.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String space = myHolder.tv_item_space.getText().toString();
                    if (!TextUtils.isEmpty(space) && mClickListener != null)
                        mClickListener.onClickSpace(space.substring(1), list.get(position).getSpace_tag_id(), list.get(position).getNickname());
                }
            });
        }
        return convertView;
    }

    class MyHolder {

        private ImageView iv_item_pic;
        private ImageView iv_item_head;
        private TextView tv_item_style;
        private TextView tv_item_space;
        private TextView tv_item_name;
        private TextView tv_chat_desiner;


    }

    public void addMoreData(List<PicDateItemBean> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    public interface TagClickListener {
        void onClickStyle(String style, String id, String nikeNmae);

        void onClickSpace(String space, String id, String nikeNmae);

        void onClickPic(int position);

        void onJumpChat(int position);

        void onJumpDesinerCenter(int position);
    }
}
