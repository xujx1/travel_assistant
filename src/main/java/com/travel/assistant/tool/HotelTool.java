package com.travel.assistant.tool;

import java.util.List;

import com.travel.assistant.domain.Hotel;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

public class HotelTool {

    @Tool(name = "酒店信息查询工具", value = "根据的用户的时间、目的地查询酒店信息")
    public List<Hotel> query(
        @P("目的城市") String destinationCity,
        @P("出发日期") String date) {

        return List.of(
            new Hotel("北京", "100", "差"),
            new Hotel("北京", "200", "中"),
            new Hotel("北京", "200", "优")
        );
    }

    @Tool(name = "酒店预订工具", value = "根据的用户的时间、目的地预订酒店，返回成功预订的酒店信息")
    public Hotel ticket(
        @P("目的城市") String destinationCity,
        @P("出发日期") String date) {

        return new Hotel("北京", "200", "中");
    }
}
