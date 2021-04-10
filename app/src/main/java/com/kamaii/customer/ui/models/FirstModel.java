package com.kamaii.customer.ui.models;

import com.kamaii.customer.DTO.CategoryDTO;

import java.io.Serializable;
import java.util.List;

public class FirstModel implements Serializable {
    String id;
    String name="";
    List<CategoryDTO> childModelList;
    public FirstModel(String id, String name, List<CategoryDTO> childModelList) {
        this.id = id;
        this.name = name;
        this.childModelList = childModelList;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryDTO> getChildModelList() {
        return childModelList;
    }

    public void setChildModelList(List<CategoryDTO> childModelList) {
        this.childModelList = childModelList;
    }


}
