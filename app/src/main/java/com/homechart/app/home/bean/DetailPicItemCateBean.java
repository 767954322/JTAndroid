package com.homechart.app.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GPD on 2017/3/4.
 */

public class DetailPicItemCateBean implements Serializable{

    private List<DetailPicItemCateTagBean> decoration_tag;
    private List<DetailPicItemCateTagBean> storage_tag;
    private List<DetailPicItemCateTagBean> originality_tag;
    private List<DetailPicItemCateTagBean> layout_tag;
    private List<DetailPicItemCateTagBean> space_tag;

    public DetailPicItemCateBean(List<DetailPicItemCateTagBean> decoration_tag, List<DetailPicItemCateTagBean> storage_tag, List<DetailPicItemCateTagBean> originality_tag, List<DetailPicItemCateTagBean> layout_tag, List<DetailPicItemCateTagBean> space_tag) {
        this.decoration_tag = decoration_tag;
        this.storage_tag = storage_tag;
        this.originality_tag = originality_tag;
        this.layout_tag = layout_tag;
        this.space_tag = space_tag;
    }

    public DetailPicItemCateBean() {
    }

    public List<DetailPicItemCateTagBean> getDecoration_tag() {
        return decoration_tag;
    }

    public void setDecoration_tag(List<DetailPicItemCateTagBean> decoration_tag) {
        this.decoration_tag = decoration_tag;
    }

    public List<DetailPicItemCateTagBean> getStorage_tag() {
        return storage_tag;
    }

    public void setStorage_tag(List<DetailPicItemCateTagBean> storage_tag) {
        this.storage_tag = storage_tag;
    }

    public List<DetailPicItemCateTagBean> getOriginality_tag() {
        return originality_tag;
    }

    public void setOriginality_tag(List<DetailPicItemCateTagBean> originality_tag) {
        this.originality_tag = originality_tag;
    }

    public List<DetailPicItemCateTagBean> getLayout_tag() {
        return layout_tag;
    }

    public void setLayout_tag(List<DetailPicItemCateTagBean> layout_tag) {
        this.layout_tag = layout_tag;
    }

    public List<DetailPicItemCateTagBean> getSpace_tag() {
        return space_tag;
    }

    public void setSpace_tag(List<DetailPicItemCateTagBean> space_tag) {
        this.space_tag = space_tag;
    }

    @Override
    public String toString() {
        return "DetailPicItemCateBean{" +
                "decoration_tag=" + decoration_tag +
                ", storage_tag=" + storage_tag +
                ", originality_tag=" + originality_tag +
                ", layout_tag=" + layout_tag +
                ", space_tag=" + space_tag +
                '}';
    }
}
