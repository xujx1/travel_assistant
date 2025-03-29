package com.travel.assistant.tool;

import java.util.List;

import com.travel.assistant.domain.Traffic;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

public class FlightTool {

    @Tool(name = "航班信息查询工具",
        value = "根据的用户的时间、出发点、目的地查询航班票信息")
    public List<Traffic> call(
        @P("出发城市") String departureCity,
        @P("目的城市") String destinationCity,
        @P("出发日期") String date) {
        return List.of(
            new Traffic("杭州", "北京", "2000", "4小时"),
            new Traffic("杭州", "北京", "1000", "5小时")
        );
    }

    @Tool(name = "航班票购买工具",
        value = "根据的用户的时间、出发点、目的地购买航班票信息，返回结果true就是购买成功")
    public boolean ticket(
        @P("出发城市") String departureCity,
        @P("目的城市") String destinationCity,
        @P("出发日期") String date) {
        return true;
    }
}
