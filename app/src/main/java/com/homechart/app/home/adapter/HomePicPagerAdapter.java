package com.homechart.app.home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.homechart.app.R;
import com.homechart.app.commont.utils.ImageUtils;
import com.homechart.app.commont.utils.UIUtils;
import com.homechart.app.home.bean.MyTabBean;
import com.homechart.app.home.fragment.HomePicFragment;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class HomePicPagerAdapter extends PagerAdapter {

    public List<List<MyTabBean>> lists;
    public List<List<MyTabBean>> lists_enable;
    private Context context;
    private View itemView;
    private OnGridViewItemClick onGridViewItemClick;
    private MyAdapter mAdapter;
    private Map<Integer, String> mSelectMap = new Hashtable<>();

    public HomePicPagerAdapter(List<List<MyTabBean>> lists, Context context, OnGridViewItemClick onGridViewItemClick) {
        this.lists = lists;
        this.context = context;
        this.onGridViewItemClick = onGridViewItemClick;
    }

    @Override
    public int getCount() {
        if (lists != null && lists.size() > 0) {
            return lists.size();
        }
        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        itemView = View.inflate(context, R.layout.viewpage_home_pic, null);
        container.removeView(itemView);
        container.addView(itemView);
        GridView gv_pop_pager = (GridView) itemView.findViewById(R.id.gv_pop_pager);
        mAdapter = new MyAdapter(position);
        gv_pop_pager.setAdapter(mAdapter);
        return itemView;
    }

    class MyAdapter extends BaseAdapter {
        int mPosition;

        public MyAdapter(int position) {
            this.mPosition = position;
        }

        @Override
        public int getCount() {
            if (null != lists && lists.size() > mPosition) {
                return lists.get(mPosition).size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return lists.get(mPosition).get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyHolder myHolder;
            if (convertView == null) {
                myHolder = new MyHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_hometag_pager, null);
                myHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_item_hometag_name);
                myHolder.iv_select = (ImageView) convertView.findViewById(R.id.iv_item_select);
                myHolder.iv_item_no = (ImageView) convertView.findViewById(R.id.iv_item_no);
                myHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_hometag_name);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            String spaceName = lists.get(mPosition).get(position).getSpace_tag_name();
            String selectName = mSelectMap.get(mPosition);
            if (mSelectMap.size() > 0 && selectName != null
                    && selectName.equals(spaceName)) {
                myHolder.iv_select.setVisibility(View.VISIBLE);
            } else {
                myHolder.iv_select.setVisibility(View.GONE);
            }
            myHolder.tv_name.setText(spaceName);
            if (spaceName.contains("色")) {
                ViewGroup.LayoutParams layoutParams = myHolder.iv_image.getLayoutParams();
                layoutParams.height = UIUtils.getDimens(R.dimen.font_75);
                myHolder.iv_image.setLayoutParams(layoutParams);
            }
            if (lists_enable != null && lists_enable.size() > 0) {
                try {
                    if (mPosition > 1 && lists_enable.get(mPosition - 2).get(position).getSpace_tag_enable() != 1) {
                        ImageUtils.displayTagEnableHalfRoundImage("", myHolder.iv_image);
                    } else {
                        ImageUtils.displayHalfRoundImage(lists.get(mPosition).get(position).getSpace_tag_image_url(), myHolder.iv_image);
                        myHolder.iv_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String getSelectName = mSelectMap.get(mPosition);
                                String getPutName = lists.get(mPosition).get(position).getSpace_tag_name();
                                if (getSelectName != null && getSelectName.equals(getPutName)) {
                                    mSelectMap.remove(mPosition);
                                } else {
                                    putSelectMap(mPosition,
                                            lists.get(mPosition).get(position).getSpace_tag_name());
                                }
                                onGridViewItemClick.onGridViewItemClick(mPosition, position, lists.get(mPosition).get(position).getSpace_tag_id());
                            }
                        });
                    }
                } catch (Exception e) {
                    ImageUtils.displayHalfRoundImage(lists.get(mPosition).get(position).getSpace_tag_image_url(), myHolder.iv_image);
                    myHolder.iv_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String getSelectName = mSelectMap.get(mPosition);
                            String getPutName = lists.get(mPosition).get(position).getSpace_tag_name();
                            if (getSelectName != null && getSelectName.equals(getPutName)) {
                                mSelectMap.remove(mPosition);
                            } else {
                                putSelectMap(mPosition,
                                        lists.get(mPosition).get(position).getSpace_tag_name());
                            }
                            onGridViewItemClick.onGridViewItemClick(mPosition, position, lists.get(mPosition).get(position).getSpace_tag_id());
                        }
                    });
                }
            } else {
                ImageUtils.displayHalfRoundImage(lists.get(mPosition).get(position).getSpace_tag_image_url(), myHolder.iv_image);
                myHolder.iv_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String getSelectName = mSelectMap.get(mPosition);
                        String getPutName = lists.get(mPosition).get(position).getSpace_tag_name();
                        if (getSelectName != null && getSelectName.equals(getPutName)) {
                            mSelectMap.remove(mPosition);
                        } else {
                            putSelectMap(mPosition,
                                    lists.get(mPosition).get(position).getSpace_tag_name());
                        }
                        onGridViewItemClick.onGridViewItemClick(mPosition, position, lists.get(mPosition).get(position).getSpace_tag_id());
                    }
                });
            }

            /*myHolder.iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String getSelectName = mSelectMap.get(mPosition);
                    String getPutName = lists.get(mPosition).get(position).getSpace_tag_name();
                    if (getSelectName != null && getSelectName.equals(getPutName)) {
                        mSelectMap.remove(mPosition);
                    } else {
                        putSelectMap(mPosition,
                                lists.get(mPosition).get(position).getSpace_tag_name());
                    }
                    onGridViewItemClick.onGridViewItemClick(mPosition, position, lists.get(mPosition).get(position).getSpace_tag_id());
                }
            });*/
            return convertView;
        }
    }

    public class MyHolder {
        private ImageView iv_image;
        private ImageView iv_select;
        private ImageView iv_item_no;
        private TextView tv_name;
    }

    public void setDataChanged(List<List<MyTabBean>> list, List<List<MyTabBean>> lists_enable) {
        this.lists = list;
        this.lists_enable = lists_enable;
        HomePicPagerAdapter.this.notifyDataSetChanged();
    }

    public interface OnGridViewItemClick {
        void onGridViewItemClick(int mPagePosition, int position, String tag_id);
    }

    public void deleteItem(int position) {
        if (mSelectMap != null)
            mSelectMap.remove(position);
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    public void updateSelected() {
        String noeMap = mSelectMap.get(0);
        String twoMap = mSelectMap.get(1);
        mSelectMap.clear();
        if (!TextUtils.isEmpty(noeMap))
            putSelectMap(0, noeMap);

        if (!TextUtils.isEmpty(twoMap))
            putSelectMap(1, twoMap);
    }

    public void putSelectMap(int position, String name) {
        if (position > 1)
            updateSelected();
        mSelectMap.put(position, name);
    }
}
