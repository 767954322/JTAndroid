package com.homechart.app.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10/010.
 */

public class DesinerCityDataItemBean implements Serializable{


    private String cate_name;
    private List<DesinerCityDataItemCityBean> citys;

    public DesinerCityDataItemBean(String cate_name, List<DesinerCityDataItemCityBean> citys) {
        this.cate_name = cate_name;
        this.citys = citys;
    }

    public DesinerCityDataItemBean() {
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public List<DesinerCityDataItemCityBean> getCitys() {
        return citys;
    }

    public void setCitys(List<DesinerCityDataItemCityBean> citys) {
        this.citys = citys;
    }

    @Override
    public String toString() {
        return "DesinerCityDataItemBean{" +
                "cate_name='" + cate_name + '\'' +
                ", citys=" + citys +
                '}';
    }
}
