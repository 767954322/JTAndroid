package com.homechart.app.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.home.bean.DesinerCityMyBean;
import com.homechart.app.home.ui.AlphabetListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PinnedHeaderListViewAdapter<T> extends BasePinnedHeaderAdapter<T> {
    public List<String> alphabets;   //字母索引数据集合，品牌列表比较特殊，需要对原始的分组字母集合进行处理
    public OnItemClick onItemClick;

    public PinnedHeaderListViewAdapter(Context context, LinkedHashMap<String, List<T>> mMap, ListView listView,
                                       AlphabetListView alphabetListView, OnItemClick onItemClick) {
        super(context, mMap);
        this.onItemClick = onItemClick;
        alphabetListView.setAdapter(listView, this, alphabetPositionListener, initAlphabets(1));
    }


    AlphabetListView.AlphabetPositionListener alphabetPositionListener = new AlphabetListView.AlphabetPositionListener() {
        @Override
        public int getPosition(String letter) {
            if (alphabetPositionMap != null && alphabetPositionMap.containsKey(letter)) {
                return alphabetPositionMap.get(letter);
            }
            return 0;
        }
    };

    /**
     * 过滤掉手动添加的数据标签
     *
     * @param position 从哪个位置开始
     * @return
     */
    private List<String> initAlphabets(int position) {
        alphabets = new ArrayList<String>();
        if (sections != null && sections.size() > 0) {
            //sections中前两个是客户端加入的分组标签，初始化字母索引时需要剔除，但不能直接从删除sections中原始数据
            int size = sections.size();
            alphabets.add(0, "#");
            for (int i = position; i < size; i++) {
                alphabets.add(sections.get(i));
            }
        }
        return alphabets;
    }


    @Override
    protected View getListView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.person_item, null);
            viewHolder.header = (TextView) convertView.findViewById(R.id.pinnedheaderlistview_header);
            viewHolder.brandName = (TextView) convertView.findViewById(R.id.brand_name);
            viewHolder.ll_linearlayout = (LinearLayout) convertView.findViewById(R.id.ll_linearlayout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.setBackgroundResource(R.drawable.app_listview_item_bg);

        if (datas != null) {
            int section = getSectionForPosition(position);
            final DesinerCityMyBean brandItem = (DesinerCityMyBean) getItem(position);
            if (brandItem != null) {
                if (getPositionForSection(section) == position) {   //如果集合中字母对应的位置等于下标值，则显示字母，否则则隐藏
                    viewHolder.header.setVisibility(View.VISIBLE);
                    viewHolder.header.setText(sections.get(section));
                    viewHolder.header.setBackgroundColor(context.getResources().getColor(R.color.pinned_header_bg));
                } else {
                    viewHolder.header.setVisibility(View.GONE);
                }
                String brandName = brandItem.getName();
                if (null != brandName) {
                    viewHolder.brandName.setText(brandName);
                }
                viewHolder.ll_linearlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClick.onClickItem(brandItem);
                    }
                });
            }
        }

        return convertView;
    }

    /**
     * 设置头部固定布局和内容
     *
     * @param header  头部的布局
     * @param section 头部内容
     */
    @Override
    protected void setHeaderContent(View header, String section) {
        TextView textView = (TextView) header.findViewById(R.id.pinnedheaderlistview_header);
        textView.setText(section);
    }


    class ViewHolder {
        private TextView header;// 头部
        private TextView brandName;
        private LinearLayout ll_linearlayout;
    }


    public interface OnItemClick {
        public void onClickItem( DesinerCityMyBean brandItem);
    }

}
