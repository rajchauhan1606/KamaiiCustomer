package com.kamaii.customer.DTO;

import com.kamaii.customer.ui.models.SubCateModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ArtistDetailsDTO implements Serializable {

    String id = "";
    String artist_banner = "";
    String user_id = "";
    String name = "";
    String category_id = "";
    String description = "";
    String about_us = "";
    String image = "";
    String completion_rate = "";
    String created_at = "";
    String updated_at = "";
    String bio = "";
    String longitude = "";
    String latitude = "";
    String location = "";
    String video_url = "";
    String price = "";
    String booking_flag = "";
    String is_online = "";
    String gender = "";
    String preference = "";
    String update_profile = "";
    String category_name = "";
    String category_price = "";
    String ava_rating = "";
    String shipping_charges = "";
    String subtotal = "";
    String total_amount = "";
    String item_count = "";
    ArrayList<ProductDTO> products = new ArrayList<>();
    ArrayList<ReviewsDTO> reviews = new ArrayList<>();
    ArrayList<GalleryDTO> gallery = new ArrayList<>();
    ArrayList<QualificationsDTO> qualifications = new ArrayList<>();
    ArrayList<ArtistBookingDTO> artist_booking = new ArrayList<>();
    ArrayList<SubCateModel> subcategory = new ArrayList<>();
    String earning = "";
    String jobDone = "";
    String totalJob = "";
    String completePercentages = "";
    String fav_status = "";
    String commission_type = "";
    String flat_type = "";
    String artist_commission_type = "";
    String currency_type = "";
    String banner_image = "";
    String service_charge = "";
    String maxprice = "";
    String service_charge_txt = "";
    String servce_charge_tracker = "";
    String free_delivery_price = "";
    String rider1_id = "";
    String total_rider_charges = "";
    String can_do_order = "";
    String rider_not_found_msg = "";
    String there_is_rider_not = "";
    String ridersubcat_id = "";
    String preparationNote = "";
    String orderdelivereddate = "";
    String mobile_no = "";
    String dis_price_text = "";
    String dis_price = "";
    String defualt_whatsappno = "";
    String slotnote = "";

    public String getSlotnote() {
        return slotnote;
    }

    public void setSlotnote(String slotnote) {
        this.slotnote = slotnote;
    }

    public String getDefualt_whatsappno() {
        return defualt_whatsappno;
    }

    public void setDefualt_whatsappno(String defualt_whatsappno) {
        this.defualt_whatsappno = defualt_whatsappno;
    }

    public String getDis_price() {
        return dis_price;
    }

    public void setDis_price(String dis_price) {
        this.dis_price = dis_price;
    }

    public String getDis_price_text() {
        return dis_price_text;
    }

    public void setDis_price_text(String dis_price_text) {
        this.dis_price_text = dis_price_text;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getOrderdelivereddate() {
        return orderdelivereddate;
    }

    public void setOrderdelivereddate(String orderdelivereddate) {
        this.orderdelivereddate = orderdelivereddate;
    }

    public String getPreparationNote() {
        return preparationNote;
    }

    public void setPreparationNote(String preparationNote) {
        this.preparationNote = preparationNote;
    }

    public String getRidersubcat_id() {
        return ridersubcat_id;
    }

    public void setRidersubcat_id(String ridersubcat_id) {
        this.ridersubcat_id = ridersubcat_id;
    }

    public String getThere_is_rider_not() {
        return there_is_rider_not;
    }

    public void setThere_is_rider_not(String there_is_rider_not) {
        this.there_is_rider_not = there_is_rider_not;
    }

    public String getRider_not_found_msg() {
        return rider_not_found_msg;
    }

    public void setRider_not_found_msg(String rider_not_found_msg) {
        this.rider_not_found_msg = rider_not_found_msg;
    }

    public String getCan_do_order() {
        return can_do_order;
    }

    public void setCan_do_order(String can_do_order) {
        this.can_do_order = can_do_order;
    }

    public String getRider1_id() {
        return rider1_id;
    }

    public void setRider1_id(String rider1_id) {
        this.rider1_id = rider1_id;
    }

    public String getTotal_rider_charges() {
        return total_rider_charges;
    }

    public void setTotal_rider_charges(String total_rider_charges) {
        this.total_rider_charges = total_rider_charges;
    }

    public String getFree_delivery_price() {
        return free_delivery_price;
    }

    public void setFree_delivery_price(String free_delivery_price) {
        this.free_delivery_price = free_delivery_price;
    }

    public String getService_charge_txt() {
        return service_charge_txt;
    }

    public void setService_charge_txt(String service_charge_txt) {
        this.service_charge_txt = service_charge_txt;
    }

    public String getServce_charge_tracker() {
        return servce_charge_tracker;
    }

    public void setServce_charge_tracker(String servce_charge_tracker) {
        this.servce_charge_tracker = servce_charge_tracker;
    }

    public String getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(String maxprice) {
        this.maxprice = maxprice;
    }

    public ArrayList<SubCateModel> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(ArrayList<SubCateModel> subcategory) {
        this.subcategory = subcategory;
    }

    public String getArtist_banner() {
        return artist_banner;
    }

    public void setArtist_banner(String artist_banner) {
        this.artist_banner = artist_banner;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbout_us() {
        return about_us;
    }

    public void setAbout_us(String about_us) {
        this.about_us = about_us;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCompletion_rate() {
        return completion_rate;
    }

    public void setCompletion_rate(String completion_rate) {
        this.completion_rate = completion_rate;
    }

    public String getShipping_charges() {
        return shipping_charges;
    }

    public void setShipping_charges(String shipping_charges) {
        this.shipping_charges = shipping_charges;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getItem_count() {
        return item_count;
    }

    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBooking_flag() {
        return booking_flag;
    }

    public void setBooking_flag(String booking_flag) {
        this.booking_flag = booking_flag;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getUpdate_profile() {
        return update_profile;
    }

    public void setUpdate_profile(String update_profile) {
        this.update_profile = update_profile;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }


    public ArrayList<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductDTO> products) {
        this.products = products;
    }

    public ArrayList<ReviewsDTO> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<ReviewsDTO> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<GalleryDTO> getGallery() {
        return gallery;
    }

    public void setGallery(ArrayList<GalleryDTO> gallery) {
        this.gallery = gallery;
    }

    public ArrayList<QualificationsDTO> getQualifications() {
        return qualifications;
    }

    public void setQualifications(ArrayList<QualificationsDTO> qualifications) {
        this.qualifications = qualifications;
    }

    public ArrayList<ArtistBookingDTO> getArtist_booking() {
        return artist_booking;
    }

    public void setArtist_booking(ArrayList<ArtistBookingDTO> artist_booking) {
        this.artist_booking = artist_booking;
    }

    public String getEarning() {
        return earning;
    }

    public void setEarning(String earning) {
        this.earning = earning;
    }

    public String getJobDone() {
        return jobDone;
    }

    public void setJobDone(String jobDone) {
        this.jobDone = jobDone;
    }

    public String getTotalJob() {
        return totalJob;
    }

    public void setTotalJob(String totalJob) {
        this.totalJob = totalJob;
    }

    public String getAva_rating() {
        return ava_rating;
    }

    public void setAva_rating(String ava_rating) {
        this.ava_rating = ava_rating;
    }

    public String getCompletePercentages() {
        return completePercentages;
    }

    public void setCompletePercentages(String completePercentages) {
        this.completePercentages = completePercentages;
    }

    public String getCategory_price() {
        return category_price;
    }

    public void setCategory_price(String category_price) {
        this.category_price = category_price;
    }

    public String getFav_status() {
        return fav_status;
    }

    public void setFav_status(String fav_status) {
        this.fav_status = fav_status;
    }

    public String getCommission_type() {
        return commission_type;
    }

    public void setCommission_type(String commission_type) {
        this.commission_type = commission_type;
    }

    public String getFlat_type() {
        return flat_type;
    }

    public void setFlat_type(String flat_type) {
        this.flat_type = flat_type;
    }

    public String getArtist_commission_type() {
        return artist_commission_type;
    }

    public void setArtist_commission_type(String artist_commission_type) {
        this.artist_commission_type = artist_commission_type;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }
}
