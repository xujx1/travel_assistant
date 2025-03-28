package com.travel.assistant.config;

import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.google.customsearch.GoogleCustomWebSearchEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSearchAugmentorConfig {

    @Bean
    public RetrievalAugmentor webSearchAugmentor() {
        WebSearchEngine webSearchEngine = GoogleCustomWebSearchEngine.builder()
            .apiKey(System.getenv("GOOGLE_SEARCH_API_KEY"))
            .csi(System.getenv("GOOGLE_SEARCH_CSI"))
            .build();

        ContentRetriever webSearchContentRetriever = WebSearchContentRetriever.builder()
            .webSearchEngine(webSearchEngine).maxResults(3).build();

        QueryRouter queryRouter = new DefaultQueryRouter(webSearchContentRetriever);

        return DefaultRetrievalAugmentor.builder().queryRouter(queryRouter).build();
    }
}
