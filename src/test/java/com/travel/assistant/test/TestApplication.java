package com.travel.assistant.test;

import com.travel.assistant.flow.PlanningFlow;
import com.travel.assistant.flow.PlanningFlowAssistant;
import com.travel.assistant.tool.PlanTool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.WebSearchTool;
import dev.langchain4j.web.search.google.customsearch.GoogleCustomWebSearchEngine;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

@SpringBootTest
public class TestApplication {

    @Resource
    private PlanningFlow planningFlow;

    @Resource
    private ChatLanguageModel model;

    @Test
    public void getDeptLoopLevelType() {
        System.out.println("Enter your prompt: ");

        String prompt = "我计划从杭州去北京旅行三天，帮我制定一个出行计划";

        System.out.println("PlanName: " + prompt);

        String result = planningFlow.execute(prompt);
        System.out.println(result);
    }

    @Test
    void should_execute_google_tool_with_AiServices() {
        WebSearchEngine webSearchEngine = GoogleCustomWebSearchEngine.builder()
            .apiKey(System.getenv("GOOGLE_SEARCH_API_KEY")) // get a free key: https://app.tavily.com/sign-in
            .csi("924ca878462a94bf8")
            .build();

        ContentRetriever webSearchContentRetriever = WebSearchContentRetriever.builder()
            .webSearchEngine(webSearchEngine)
            .maxResults(3)
            .build();

        QueryRouter queryRouter = new DefaultQueryRouter(webSearchContentRetriever);

        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
            .queryRouter(queryRouter)
            .build();

        Assistant assistant = AiServices.builder(Assistant.class)
            .chatLanguageModel(model)
            .retrievalAugmentor(retrievalAugmentor)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
            .build();

        System.out.println(assistant.answer("杭州去北京开车需要多久？"));
    }

    public interface Assistant {

        String answer(String query);
    }
}
