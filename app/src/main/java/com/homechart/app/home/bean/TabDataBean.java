package com.homechart.app.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GPD on 2017/3/2.
 */

public class TabDataBean implements Serializable{

    private List<TabSpaceBean> spaces;
    private List<TabStylesBean> styles;

    public TabDataBean(List<TabSpaceBean> spaces, List<TabStylesBean> styles) {
        this.spaces = spaces;
        this.styles = styles;
    }

    public TabDataBean() {
    }

    public List<TabSpaceBean> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<TabSpaceBean> spaces) {
        this.spaces = spaces;
    }

    public List<TabStylesBean> getStyles() {
        return styles;
    }

    public void setStyles(List<TabStylesBean> styles) {
        this.styles = styles;
    }

    @Override
    public String toString() {
        return "TabDataBean{" +
                "spaces=" + spaces +
                ", styles=" + styles +
                '}';
    }
}
