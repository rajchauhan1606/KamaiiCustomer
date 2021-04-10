package com.kamaii.customer.DTO;

import java.io.Serializable;


public class EarningDTO implements Serializable {

    String referral_earning = "";
    String tot_ref = "";


    public String getReferral_earning() {
        return referral_earning;
    }

    public String getTot_ref() {
        return tot_ref;
    }


    public void setReferral_earning(String referral_earning) {
        this.referral_earning = referral_earning;
    }


    public void setTot_ref(String tot_ref) {
        this.tot_ref = tot_ref;
    }
}
