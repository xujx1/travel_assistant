package com.travel.assistant.flow;

import com.travel.assistant.tool.PlanTool;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface PlanningFlowAssistant {

    String execute(@UserMessage String inputText, @V("planTool") PlanTool tool);
}
