package com.travel.assistant.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Hotel {

    /**
     * 目的城市
     */
    private String destinationCity;

    /**
     * 价格
     */
    private String price;

    /**
     * 酒店舒适度
     */
    private String comfortLevel;
}
