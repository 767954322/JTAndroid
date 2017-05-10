package com.homechart.app.scrolllistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.homechart.app.R;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    Context context;
    private List<Integer> data;

    public void setData(List<Integer> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public GridViewAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
        ImageView imageView1 = (ImageView) convertView.findViewById(R.id.imageView);
        imageView1.setBackgroundResource(data.get(position));
        return convertView;

    }

}
