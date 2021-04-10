package com.kamaii.customer.interfacess;

import com.kamaii.customer.DTO.ProductDTO;

import java.util.ArrayList;

public interface OnSelectedItemListenerCart
{
    void setOnClick(ArrayList<ProductDTO> productDTOArrayList, int position);

}
