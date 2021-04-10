package com.kamaii.customer.DTO;

import java.io.Serializable;
import java.util.ArrayList;

public class UserBooking implements Serializable {

    String id = "";
    String user_id = "";
    String artist_id = "";
    String booking_time = "";
    String description = "";
    String duration = "";
    String service_id = "";
    String start_time = "";
    String category_price = "";
    String booking_date = "";
    String end_time = "";
    String price = "";
    String status = "";
    String booking_flag = "";
    String rider_flag = "";
    String decline_by = "";
    String time_zone = "";
    String decline_reason = "";
    String booking_timestamp = "";
    String commission_type = "";
    String booking_type = "";
    String flat_type = "";
    String created_at = "";
    String updated_at = "";
    String job_id = "";
    String latitude = "";
    String longitude = "";
    String address = "";
    String discount_amount = "";
    String artistImage = "";
    String category_name = "";
    String artistName = "";
    String artist_commission_type = "";
    String artistMobile = "";
    String artistEmail = "";
    String artistLocation = "";
    String userName = "";
    String jobDone = "";
    String completePercentages = "";
    String ava_rating = "";
    String working_min = "";
    String userImage = "";
    String currency_type = "";
    String total_amount = "";
    String preparationNote = "";
    String invoice_id = "";
    HistoryDTO invoice;
    boolean isSection = false;
    String section_name = "";
    String location_status = "";
    String approxdatetime = "";
    String image = "";
    String destinationaddress = "";
    String artistlivelat = "";
    String artistlivelang = "";
    String artistRating = "";
    String artistVehicleImage = "";

    String booking_date2 = "";
    String source_lat = "";
    String source_long = "";
    String dest_lat = "";
    String dest_long = "";
    String pay_type = "";
    String review_status = "";
    String rider_lat = "";
    String rider_long = "";
    String itemstriketotal = "";
    String netpay = "";
    String shipping_charges = "";
    String sub_total = "";
    String cat_typemap = "";
    String rider_id = "";
    String discount_txt = "";
    String discount_digit_txt = "";
    String order_product = "";
    String Scratch_amount = "";
    String working_time;

    public String getWorking_time() {
        return working_time;
    }

    public void setWorking_time(String working_time) {
        this.working_time = working_time;
    }

    public String getScratch_amount() {
        return Scratch_amount;
    }

    public void setScratch_amount(String scratch_amount) {
        Scratch_amount = scratch_amount;
    }

    public String getOrder_product() {
        return order_product;
    }

    public void setOrder_product(String order_product) {
        this.order_product = order_product;
    }

    public String getDiscount_txt() {
        return discount_txt;
    }

    public void setDiscount_txt(String discount_txt) {
        this.discount_txt = discount_txt;
    }

    public String getDiscount_digit_txt() {
        return discount_digit_txt;
    }

    public void setDiscount_digit_txt(String discount_digit_txt) {
        this.discount_digit_txt = discount_digit_txt;
    }

    public String getRider_id() {
        return rider_id;
    }

    public void setRider_id(String rider_id) {
        this.rider_id = rider_id;
    }

    public String getCat_typemap() {
        return cat_typemap;
    }

    public void setCat_typemap(String cat_typemap) {
        this.cat_typemap = cat_typemap;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getShipping_charges() {
        return shipping_charges;
    }

    public void setShipping_charges(String shipping_charges) {
        this.shipping_charges = shipping_charges;
    }

    String rider_order = "";
    String rider_name = "";
    String rider_mobileno = "";
    String get_new_booking_flag = "";
    String booking_msg = "";
    String booking_msg2 = "";
    String itemtotal = "";
    String servicecharge = "";
    String there_is_rider_not = "";
    String ismap = "";
    String partnerisrider = "";

    public String getPartnerisrider() {
        return partnerisrider;
    }

    public void setPartnerisrider(String partnerisrider) {
        this.partnerisrider = partnerisrider;
    }

    public String getIsmap() {
        return ismap;
    }

    public void setIsmap(String ismap) {
        this.ismap = ismap;
    }

    public String getPreparationNote() {
        return preparationNote;
    }

    public void setPreparationNote(String preparationNote) {
        this.preparationNote = preparationNote;
    }

    public String getThere_is_rider_not() {
        return there_is_rider_not;
    }

    public void setThere_is_rider_not(String there_is_rider_not) {
        this.there_is_rider_not = there_is_rider_not;
    }

    public String getBooking_msg2() {
        return booking_msg2;
    }

    public void setBooking_msg2(String booking_msg2) {
        this.booking_msg2 = booking_msg2;
    }

    public String getBooking_date2() {
        return booking_date2;
    }

    public void setBooking_date2(String booking_date2) {
        this.booking_date2 = booking_date2;
    }

    public String getItemtotal() {
        return itemtotal;
    }

    public void setItemtotal(String itemtotal) {
        this.itemtotal = itemtotal;
    }

    public String getServicecharge() {
        return servicecharge;
    }

    public void setServicecharge(String servicecharge) {
        this.servicecharge = servicecharge;
    }

    public String getNetpay() {
        return netpay;
    }

    public void setNetpay(String netpay) {
        this.netpay = netpay;
    }

    public String getItemstriketotal() {
        return itemstriketotal;
    }

    public void setItemstriketotal(String itemstriketotal) {
        this.itemstriketotal = itemstriketotal;
    }


    public String getArtistVehicleImage() {
        return artistVehicleImage;
    }

    public void setArtistVehicleImage(String artistVehicleImage) {
        this.artistVehicleImage = artistVehicleImage;
    }

    public String getArtistRating() {
        return artistRating;
    }

    public void setArtistRating(String artistRating) {
        this.artistRating = artistRating;
    }

    String rider_rating = "";
    String rider_image = "";

    public String getRider_image() {
        return rider_image;
    }

    public void setRider_image(String rider_image) {
        this.rider_image = rider_image;
    }


    public String getBooking_msg() {
        return booking_msg;
    }

    public void setBooking_msg(String booking_msg) {
        this.booking_msg = booking_msg;
    }

    public String getGet_new_booking_flag() {
        return get_new_booking_flag;
    }

    public void setGet_new_booking_flag(String get_new_booking_flag) {
        this.get_new_booking_flag = get_new_booking_flag;
    }

    public String getRider_name() {
        return rider_name;
    }

    public String getRider_rating() {
        return rider_rating;
    }

    public void setRider_rating(String rider_rating) {
        this.rider_rating = rider_rating;
    }

    public void setRider_name(String rider_name) {
        this.rider_name = rider_name;
    }

    public String getRider_mobileno() {
        return rider_mobileno;
    }

    public void setRider_mobileno(String rider_mobileno) {
        this.rider_mobileno = rider_mobileno;
    }

    public String getRider_flag() {
        return rider_flag;
    }

    public void setRider_flag(String rider_flag) {
        this.rider_flag = rider_flag;
    }

    public String getRider_order() {
        return rider_order;
    }

    public void setRider_order(String rider_order) {
        this.rider_order = rider_order;
    }

    public String getRider_lat() {
        return rider_lat;
    }

    public void setRider_lat(String rider_lat) {
        this.rider_lat = rider_lat;
    }

    public String getRider_long() {
        return rider_long;
    }

    public void setRider_long(String rider_long) {
        this.rider_long = rider_long;
    }

    public String getDest_long() {
        return dest_long;
    }

    public void setDest_long(String dest_long) {
        this.dest_long = dest_long;
    }


    public String getSource_lat() {
        return source_lat;
    }

    public void setSource_lat(String source_lat) {
        this.source_lat = source_lat;
    }

    public String getSource_long() {
        return source_long;
    }

    public void setSource_long(String source_long) {
        this.source_long = source_long;
    }

    public String getDest_lat() {
        return dest_lat;
    }

    public void setDest_lat(String dest_lat) {
        this.dest_lat = dest_lat;
    }

    public String getReview_status() {
        return review_status;
    }

    public void setReview_status(String review_status) {
        this.review_status = review_status;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getArtistlivelang() {
        return artistlivelang;
    }

    public void setArtistlivelang(String artistlivelang) {
        this.artistlivelang = artistlivelang;
    }

    public String getArtistlivelat() {
        return artistlivelat;
    }

    public void setArtistlivelat(String artistlivelat) {
        this.artistlivelat = artistlivelat;
    }

    public String getDestinationaddress() {
        return destinationaddress;
    }

    public void setDestinationaddress(String destinationaddress) {
        this.destinationaddress = destinationaddress;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getApproxdatetime() {
        return approxdatetime;
    }

    public void setApproxdatetime(String approxdatetime) {
        this.approxdatetime = approxdatetime;
    }

    public String getLocation_status() {
        return location_status;
    }

    public void setLocation_status(String location_status) {
        this.location_status = location_status;
    }

    ArrayList<ProductDTO> product = new ArrayList<>();

    public ArrayList<ProductDTO> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<ProductDTO> product) {
        this.product = product;
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

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getCategory_price() {
        return category_price;
    }

    public void setCategory_price(String category_price) {
        this.category_price = category_price;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBooking_flag() {
        return booking_flag;
    }

    public void setBooking_flag(String booking_flag) {
        this.booking_flag = booking_flag;
    }

    public String getDecline_by() {
        return decline_by;
    }

    public void setDecline_by(String decline_by) {
        this.decline_by = decline_by;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getDecline_reason() {
        return decline_reason;
    }

    public void setDecline_reason(String decline_reason) {
        this.decline_reason = decline_reason;
    }

    public String getBooking_timestamp() {
        return booking_timestamp;
    }

    public void setBooking_timestamp(String booking_timestamp) {
        this.booking_timestamp = booking_timestamp;
    }

    public String getCommission_type() {
        return commission_type;
    }

    public void setCommission_type(String commission_type) {
        this.commission_type = commission_type;
    }

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
    }

    public String getFlat_type() {
        return flat_type;
    }

    public void setFlat_type(String flat_type) {
        this.flat_type = flat_type;
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

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getArtistImage() {
        return artistImage;
    }

    public void setArtistImage(String artistImage) {
        this.artistImage = artistImage;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtist_commission_type() {
        return artist_commission_type;
    }

    public void setArtist_commission_type(String artist_commission_type) {
        this.artist_commission_type = artist_commission_type;
    }

    public String getArtistMobile() {
        return artistMobile;
    }

    public void setArtistMobile(String artistMobile) {
        this.artistMobile = artistMobile;
    }

    public String getArtistEmail() {
        return artistEmail;
    }

    public void setArtistEmail(String artistEmail) {
        this.artistEmail = artistEmail;
    }

    public String getArtistLocation() {
        return artistLocation;
    }

    public void setArtistLocation(String artistLocation) {
        this.artistLocation = artistLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJobDone() {
        return jobDone;
    }

    public void setJobDone(String jobDone) {
        this.jobDone = jobDone;
    }

    public String getCompletePercentages() {
        return completePercentages;
    }

    public void setCompletePercentages(String completePercentages) {
        this.completePercentages = completePercentages;
    }

    public String getAva_rating() {
        return ava_rating;
    }

    public void setAva_rating(String ava_rating) {
        this.ava_rating = ava_rating;
    }

    public String getWorking_min() {
        return working_min;
    }

    public void setWorking_min(String working_min) {
        this.working_min = working_min;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public HistoryDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(HistoryDTO invoice) {
        this.invoice = invoice;
    }

    public boolean isSection() {
        return isSection;
    }

    public void setSection(boolean section) {
        isSection = section;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

}
