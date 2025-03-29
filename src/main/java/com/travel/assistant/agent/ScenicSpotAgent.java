package com.travel.assistant.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.travel.assistant.domain.PlanStep;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component(value = "scenicSpotAgent")
public class ScenicSpotAgent implements Agent {

    @Resource
    private RetrievalAugmentor retrievalAugmentor;

    @Resource
    private ChatLanguageModel model;

    private static final String PROMPT
        = "你是一个导游助手，能够理解用户的意图，并根据用户的目的地和出行时间选择出合适的景点，并将景点规划好，返回给用户。"
        + "比如第一天：去XXX景点，第二天：去XXX景点";

    @Override
    public String executeStep(PlanStep step, List<String> userInput) {

        List<ChatMessage> messages = new ArrayList<>();

        messages.addAll(
            userInput.stream().map(dev.langchain4j.data.message.UserMessage::new).collect(Collectors.toSet()));
        messages.add(new dev.langchain4j.data.message.UserMessage(step.getStepName()));

        HotelAgent.HotelAssistant assistant = AiServices.builder(HotelAgent.HotelAssistant.class)
            .chatLanguageModel(model)
            .retrievalAugmentor(retrievalAugmentor)
            .build();

        return assistant.answer(messages);
    }

    @SystemMessage(value = PROMPT)
    public interface HotelAssistant {

        String answer(@dev.langchain4j.service.UserMessage List<ChatMessage> messages);
    }
}