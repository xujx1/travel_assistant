package com.travel.assistant.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Traffic {

    /**
     * 出发城市
     */
    private String departureCity;

    /**
     * 目的城市
     */
    private String destinationCity;

    /**
     * 价格
     */
    private String price;

    /**
     * 旅途时间
     */
    private String time;
}
