package com.kamaii.customer.DTO;

import java.io.Serializable;

public class TrackingData implements Serializable {
    double vendorLatitude;
    double vendorLongitude;
    String sourceAddress;
    String destinationAddress;
    String vendorName;
    int artistId;
    String totalAmount;
    String productName;
    String shippingcharges;
    String subTotal;
    String cat_typemap;
    String preparationNote;
    String discount_txt;
    String discount_digit_txt;

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

    public String getPreparationNote() {
        return preparationNote;
    }

    public void setPreparationNote(String preparationNote) {
        this.preparationNote = preparationNote;
    }

    public String getCat_typemap() {
        return cat_typemap;
    }

    public void setCat_typemap(String cat_typemap) {
        this.cat_typemap = cat_typemap;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getShippingcharges() {
        return shippingcharges;
    }

    public void setShippingcharges(String shippingcharges) {
        this.shippingcharges = shippingcharges;
    }

    public String getArtistVehicleImage() {
        return artistVehicleImage;
    }

    public void setArtistVehicleImage(String artistVehicleImage) {
        this.artistVehicleImage = artistVehicleImage;
    }

    String vendorImage;
    String artistRating;
    String artistVehicleImage;

    public String getArtistRating() {
        return artistRating;
    }

    public void setArtistRating(String artistRating) {
        this.artistRating = artistRating;
    }

    String mobileNumber;

    public String getRider_rating() {
        return rider_rating;
    }

    public void setRider_rating(String rider_rating) {
        this.rider_rating = rider_rating;
    }

    String screenFlag;
    int bookingFlag;
    int bookingId;
    int paymentType;
    String bookingDate;
    String bookingTime;
    String vehicleNumber = "";
    String rider_name = "";

    public String getRider_image() {
        return rider_image;
    }

    public void setRider_image(String rider_image) {
        this.rider_image = rider_image;
    }

    String rider_mobileno = "";
    String rider_rating = "";
    String rider_image = "";


    public String getRider_name() {
        return rider_name;
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

    public double getVendorLatitude() {
        return vendorLatitude;
    }

    public void setVendorLatitude(double vendorLatitude) {
        this.vendorLatitude = vendorLatitude;
    }

    public double getVendorLongitude() {
        return vendorLongitude;
    }

    public void setVendorLongitude(double vendorLongitude) {
        this.vendorLongitude = vendorLongitude;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVendorImage() {
        return vendorImage;
    }

    public void setVendorImage(String vendorImage) {
        this.vendorImage = vendorImage;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getScreenFlag() {
        return screenFlag;
    }

    public void setScreenFlag(String screenFlag) {
        this.screenFlag = screenFlag;
    }

    public int getBookingFlag() {
        return bookingFlag;
    }

    public void setBookingFlag(int bookingFlag) {
        this.bookingFlag = bookingFlag;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
