package com.kamaii.customer.interfacess;

import com.kamaii.customer.ui.models.RecentAddress;

import java.util.ArrayList;

public interface OnBottomSelectedItemListener
{
    void setOnClick(int position, ArrayList<RecentAddress> recentAddressArrayList, int flag);

}
