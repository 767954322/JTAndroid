package com.homechart.app.home.bean;

import java.io.Serializable;

/**
 * Created by GPD on 2017/3/6.
 */

public class DesinerCenterDataBean implements Serializable {

    private String user_id;
    private String nickname;
    private String avatar;
    private String gender;
    private String location;
    private String description;
    private String project_num;
    private String image_url;
    private String profession;
    private String auth_status;
    private String job_years;
    private String service_items;
    private String service_flow;
    private String fee_scale;
    private String service_price;
    private String service_area;
    private String mobile;
    private String telephone;
    private String homepage;
    private String email;
    private String qq;
    private String wechat;
    private String address;

    public DesinerCenterDataBean(String user_id, String nickname, String avatar, String gender, String location, String description, String project_num, String image_url, String profession, String auth_status, String job_years, String service_items, String service_flow, String fee_scale, String service_price, String service_area, String mobile, String telephone, String homepage, String email, String qq, String wechat, String address) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.gender = gender;
        this.location = location;
        this.description = description;
        this.project_num = project_num;
        this.image_url = image_url;
        this.profession = profession;
        this.auth_status = auth_status;
        this.job_years = job_years;
        this.service_items = service_items;
        this.service_flow = service_flow;
        this.fee_scale = fee_scale;
        this.service_price = service_price;
        this.service_area = service_area;
        this.mobile = mobile;
        this.telephone = telephone;
        this.homepage = homepage;
        this.email = email;
        this.qq = qq;
        this.wechat = wechat;
        this.address = address;
    }

    public DesinerCenterDataBean() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProject_num() {
        return project_num;
    }

    public void setProject_num(String project_num) {
        this.project_num = project_num;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAuth_status() {
        return auth_status;
    }

    public void setAuth_status(String auth_status) {
        this.auth_status = auth_status;
    }

    public String getJob_years() {
        return job_years;
    }

    public void setJob_years(String job_years) {
        this.job_years = job_years;
    }

    public String getService_items() {
        return service_items;
    }

    public void setService_items(String service_items) {
        this.service_items = service_items;
    }

    public String getService_flow() {
        return service_flow;
    }

    public void setService_flow(String service_flow) {
        this.service_flow = service_flow;
    }

    public String getFee_scale() {
        return fee_scale;
    }

    public void setFee_scale(String fee_scale) {
        this.fee_scale = fee_scale;
    }

    public String getService_price() {
        return service_price;
    }

    public void setService_price(String service_price) {
        this.service_price = service_price;
    }

    public String getService_area() {
        return service_area;
    }

    public void setService_area(String service_area) {
        this.service_area = service_area;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "DesinerCenterDataBean{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender='" + gender + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", project_num='" + project_num + '\'' +
                ", image_url='" + image_url + '\'' +
                ", profession='" + profession + '\'' +
                ", auth_status='" + auth_status + '\'' +
                ", job_years='" + job_years + '\'' +
                ", service_items='" + service_items + '\'' +
                ", service_flow='" + service_flow + '\'' +
                ", fee_scale='" + fee_scale + '\'' +
                ", service_price='" + service_price + '\'' +
                ", service_area='" + service_area + '\'' +
                ", mobile='" + mobile + '\'' +
                ", telephone='" + telephone + '\'' +
                ", homepage='" + homepage + '\'' +
                ", email='" + email + '\'' +
                ", qq='" + qq + '\'' +
                ", wechat='" + wechat + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
