package com.homechart.app.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10/010.
 */

public class DesinerCityDataBean implements Serializable{

    private List<DesinerCityDataItemBean> citylist;

    public DesinerCityDataBean(List<DesinerCityDataItemBean> citylist) {
        this.citylist = citylist;
    }

    public DesinerCityDataBean() {
    }

    public List<DesinerCityDataItemBean> getCitylist() {
        return citylist;
    }

    public void setCitylist(List<DesinerCityDataItemBean> citylist) {
        this.citylist = citylist;
    }

    @Override
    public String toString() {
        return "DesinerCityDataBean{" +
                "citylist=" + citylist +
                '}';
    }
}
