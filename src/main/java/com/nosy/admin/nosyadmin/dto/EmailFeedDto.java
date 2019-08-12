package com.nosy.admin.nosyadmin.dto;

import javax.validation.constraints.NotNull;

public class EmailFeedDto {

    @NotNull private String id;
    @NotNull private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
