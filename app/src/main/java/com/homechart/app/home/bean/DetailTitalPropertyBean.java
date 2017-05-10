package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/4.
 */

public class DetailTitalPropertyBean implements Serializable {

    private String house_type;
    private String area;
    private String project_type;
    private String price;
    private String style;
    private String color;
    private String location;
    private String house_name;

    public DetailTitalPropertyBean(String house_type, String area, String project_type, String price, String style, String color, String location, String house_name) {
        this.house_type = house_type;
        this.area = area;
        this.project_type = project_type;
        this.price = price;
        this.style = style;
        this.color = color;
        this.location = location;
        this.house_name = house_name;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProject_type() {
        return project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHouse_name() {
        return house_name;
    }

    public void setHouse_name(String house_name) {
        this.house_name = house_name;
    }

    @Override
    public String toString() {
        return "DetailTitalPropertyBean{" +
                "house_type='" + house_type + '\'' +
                ", area='" + area + '\'' +
                ", project_type='" + project_type + '\'' +
                ", price='" + price + '\'' +
                ", style='" + style + '\'' +
                ", color='" + color + '\'' +
                ", location='" + location + '\'' +
                ", house_name='" + house_name + '\'' +
                '}';
    }
}
