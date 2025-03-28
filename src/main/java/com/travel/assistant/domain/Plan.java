package com.travel.assistant.domain;

import java.util.List;

import dev.langchain4j.agent.tool.P;
import lombok.Data;

@Data
public class Plan {

    /**
     * 计划指令
     */
    private String command;

    /**
     * 计划步骤
     */
    private List<PlanStep> steps;
}
