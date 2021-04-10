
package com.kamaii.customer.ui.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("shipping_charges")
    @Expose
    private String shippingCharges;
    @SerializedName("subtotal")
    @Expose
    private Integer subtotal;
    @SerializedName("total_amount")
    @Expose
    private Integer totalAmount;
    String service_charge_txt = "";
    String servce_charge_tracker = "";


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

    public String getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(String shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

}
