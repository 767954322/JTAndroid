package com.homechart.app.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.utils.ImageUtils;
import com.homechart.app.home.bean.DesinerListDataItemBean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/10/010.
 */

public class DesinerListAdapter extends BaseAdapter {
    private List<DesinerListDataItemBean> list;
    private Context context;

    public DesinerListAdapter(List<DesinerListDataItemBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyHolder myHolder;
        if (null == convertView) {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_desinerlist_item, null);
            myHolder.listPic = (ImageView) convertView.findViewById(R.id.iv_item_desinerlist_pic);
            myHolder.listHeader = (ImageView) convertView.findViewById(R.id.iv_desiner_header);
            myHolder.iv_desiner_shiming = (ImageView) convertView.findViewById(R.id.iv_desiner_shiming);
            myHolder.desinerName = (TextView) convertView.findViewById(R.id.tv_desiner_name);
            myHolder.tv_desiner_price = (TextView) convertView.findViewById(R.id.tv_desiner_price);
            myHolder.tv_desiner_location = (TextView) convertView.findViewById(R.id.tv_desiner_location);
            myHolder.tv_desiner_info = (TextView) convertView.findViewById(R.id.tv_desiner_info);
            myHolder.tv_desiner_uppic_num = (TextView) convertView.findViewById(R.id.tv_desiner_uppic_num);
            myHolder.tv_desiner_service_num = (TextView) convertView.findViewById(R.id.tv_desiner_service_num);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        if (list.get(position).getAuth_status() == 1) {
            myHolder.iv_desiner_shiming.setVisibility(View.GONE);
        }
        myHolder.tv_desiner_service_num.setText(list.get(position).getProject_num());
        myHolder.tv_desiner_uppic_num.setText(list.get(position).getItem_num());
        myHolder.tv_desiner_info.setText(list.get(position).getDescription());
        myHolder.tv_desiner_location.setText(list.get(position).getProvinceCity());
        String price = list.get(position).getService_price();
        if (TextUtils.isEmpty(price)) {
            price = "价格面议";
        }
        myHolder.tv_desiner_price.setText(price);
        String nikename = "";
        if (list.get(position).getNickname().length() > 5) {
            nikename = list.get(position).getNickname().substring(0, 5) + "...";
        } else {
            nikename = list.get(position).getNickname();
        }
        myHolder.desinerName.setText(nikename);
        if (!list.get(position).getBig_image_url().equals((myHolder.listPic.getTag()))) {
            ImageUtils.displayHalfRoundImage(list.get(position).getBig_image_url(), myHolder.listPic);
            myHolder.listPic.setTag(list.get(position).getBig_image_url());
        }
        if (!list.get(position).getAvatar().equals((myHolder.listHeader.getTag()))) {
            ImageUtils.displayRoundImage(list.get(position).getAvatar(), myHolder.listHeader);
            myHolder.listHeader.setTag(list.get(position).getAvatar());
        }
//        ImageUtils.displayHalfRoundImage(list.get(position).getBig_image_url(), myHolder.listPic);
//        ImageUtils.displayRoundImage(list.get(position).getAvatar(), myHolder.listHeader);
        return convertView;
    }

    class MyHolder {
        private ImageView listPic;
        private ImageView listHeader;
        private ImageView iv_desiner_shiming;
        private TextView desinerName;
        private TextView tv_desiner_price;
        private TextView tv_desiner_location;
        private TextView tv_desiner_info;
        private TextView tv_desiner_uppic_num;
        private TextView tv_desiner_service_num;
    }

    public void addMoreData(List<DesinerListDataItemBean> list) {

        this.list = list;
        notifyDataSetChanged();
    }
}
