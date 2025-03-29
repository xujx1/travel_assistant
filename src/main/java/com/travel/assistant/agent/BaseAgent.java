package com.travel.assistant.agent;

import java.util.List;

import com.travel.assistant.domain.PlanStep;
import org.springframework.stereotype.Component;

@Component("baseAgent")
public class BaseAgent implements Agent {
    @Override
    public String executeStep(PlanStep step, List<String> userInput) {
        return "成功";
    }
}
