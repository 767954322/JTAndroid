package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/10/010.
 */

public class DesinerCityMyBean implements Serializable {

    private String letter;
    private String name;
    private int city_id;

    public DesinerCityMyBean(String letter, String name, int city_id) {
        this.letter = letter;
        this.name = name;
        this.city_id = city_id;
    }

    public DesinerCityMyBean() {
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }


    @Override
    public String toString() {
        return "DesinerCityMyBean{" +
                "letter='" + letter + '\'' +
                ", name='" + name + '\'' +
                ", city_id=" + city_id +
                '}';
    }
}
