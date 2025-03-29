package com.travel.assistant.agent;

import java.util.List;
import java.util.Scanner;

import com.travel.assistant.domain.PlanStep;
import org.springframework.stereotype.Component;

@Component("completeAgent")
public class CompleteAgent implements Agent {

    private static final String PROMPT = "你是助手，能够获取更多的用户输入";

    @Override
    public String executeStep(PlanStep step, List<String> userInput) {
        System.out.println("输入: ");

        Scanner scanner = new Scanner(System.in);

        String prompt = scanner.nextLine().trim();

        userInput.add(prompt);
        return prompt;
    }
}
