package com.travel.assistant.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.Getter;

public class RouthTool {

    @Tool(name = "路由工具",
        value = "解析用户的意图，选择合适的Agent去处理当前需要解决的问题")
    public String call(@P("agent required to complete the current step."
        + "Available agent: hotelAgent, trafficAgent, completeAgent. "
        + "hotelAgent: search for and book."
        + "trafficAgent:choose the appropriate mode of transportation and make a reservation."
        + "completeAgent:Get more user input, such as time") String routhAgent) {

        return routhAgent;
    }
}
