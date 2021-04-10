package com.kamaii.customer.ui.models;

public class PaymentModeModel {

    boolean selected;
    String payment_type;
    String getPayment_desc;

    public PaymentModeModel(boolean selected, String payment_type, String getPayment_desc) {
        this.selected = selected;
        this.payment_type = payment_type;
        this.getPayment_desc = getPayment_desc;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getGetPayment_desc() {
        return getPayment_desc;
    }

    public void setGetPayment_desc(String getPayment_desc) {
        this.getPayment_desc = getPayment_desc;
    }
}
