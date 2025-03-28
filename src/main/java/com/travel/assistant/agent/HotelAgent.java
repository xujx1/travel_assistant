package com.travel.assistant.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.travel.assistant.domain.PlanStep;
import com.travel.assistant.tool.HotelTool;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("hotelAgent")
public class HotelAgent implements BaseAgent {

    @Resource
    private ChatLanguageModel model;

    private static final String PROMPT
        = "你是一个酒店预订助手，能够理解用户的意图，并根据用户的目的地选择出合适的酒店，并且给出最优的方案";

    @Override
    public String executeStep(PlanStep step, List<String> userInput) {

        List<ChatMessage> messages = new ArrayList<>();

        messages.addAll(userInput.stream().map(dev.langchain4j.data.message.UserMessage::new).collect(Collectors.toSet()));
        messages.add(new dev.langchain4j.data.message.UserMessage(step.getStepName()));

        HotelAssistant assistant = AiServices.builder(HotelAssistant.class)
            .chatLanguageModel(model)
            .tools(new HotelTool())
            .build();

        return assistant.answer(messages);
    }

    @SystemMessage(value = PROMPT)
    public interface HotelAssistant {

        String answer(@dev.langchain4j.service.UserMessage List<ChatMessage> messages);
    }
}
