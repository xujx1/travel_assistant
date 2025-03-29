package com.travel.assistant.agent;

import java.util.List;

import com.travel.assistant.domain.PlanStep;

public interface Agent {

    /**
     * Execute a step
     * @param step
     * @return
     */
    String executeStep(PlanStep step, List<String> userInput);
}
