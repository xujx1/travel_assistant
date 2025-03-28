package com.travel.assistant.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.travel.assistant.domain.PlanStep;
import com.travel.assistant.tool.FlightTool;
import com.travel.assistant.tool.TrainTool;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component("trafficAgent")
public class TrafficAgent implements BaseAgent {

    @Resource
    private ChatLanguageModel model;

    private static final String PROMPT
        = "你是一个交通出行的助手，能够根据用户的出发点和目的地罗列出交通方案，并且给出最优的方案";

    @Override
    public String executeStep(PlanStep step, List<String> userInput) {
        List<ChatMessage> messages = new ArrayList<>();

        messages.addAll(userInput.stream().map(UserMessage::new).collect(Collectors.toSet()));
        messages.add(new UserMessage(step.getStepName()));

        TrafficAssistant assistant = AiServices.builder(TrafficAssistant.class)
            .chatLanguageModel(model)
            .tools(new TrainTool(), new FlightTool())
            .build();

        return assistant.execute(messages);
    }

    @dev.langchain4j.service.SystemMessage(value = PROMPT)
    interface TrafficAssistant {
        String execute(@dev.langchain4j.service.UserMessage List<ChatMessage> messages);
    }
}
