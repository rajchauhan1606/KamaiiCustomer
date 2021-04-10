package com.kamaii.customer.ui.models;

import java.io.Serializable;

public class RecentAddress implements Serializable
{
    String address_type;
    String address;
    String lang;
    String lat;
    String user_id;
    String id;
    String status;
    String created_at;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type)
    {
        this.address_type = address_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public String getLat() {
        return lat;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


}
