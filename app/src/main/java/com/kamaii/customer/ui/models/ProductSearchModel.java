package com.kamaii.customer.ui.models;

public class ProductSearchModel {

    String artist_id = "";
    String cat_id = "";
    String subcat_id = "";
    String user_name = "";
    String subcat_name = "";


    public String getSubcat_name() {
        return subcat_name;
    }

    public void setSubcat_name(String subcat_name) {
        this.subcat_name = subcat_name;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSubcat_id() {
        return subcat_id;
    }

    public void setSubcat_id(String subcat_id) {
        this.subcat_id = subcat_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
