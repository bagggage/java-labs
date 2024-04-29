package com.example.lab.dto;

import com.example.lab.entity.Link;

import lombok.Data;

@Data
public class LinkDto {
    public LinkDto(Link link) {
        service = link.getService().name();
        url = link.getUrl();
    }

    private String service;
    private String url;
}
