package com.travel.assistant.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.travel.assistant.domain.Plan;
import com.travel.assistant.domain.PlanStep;
import com.travel.assistant.eums.StepStatusEnum;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.Getter;

public class PlanTool {

    @Getter
    private Plan plan;

    @Tool(name = "出行分解工具",
        value = "一种出行分解工具，允许代理创建和管理解决复杂任务的计划。该工具提供了创建计划、更新计划步骤以及跟踪进度的功能。")
    public String call(
        @P(value = "The command to execute. Available commands: create, update, list, get,set_active, mark_step, "
            + "delete.")
        String command,
        @P(value = "plan steps. Required for create command, optional for update command.") List<String> planStepList) {

        Plan plan = new Plan();
        plan.setCommand(command);

        List<PlanStep> steps = new ArrayList<>();
        for (int i = 0; i < planStepList.size(); i++) {
            String step = planStepList.get(i);
            PlanStep planStep = new PlanStep();
            planStep.setStepId("step_" + i);
            planStep.setStepStatus(StepStatusEnum.not_started.name());
            planStep.setStepName(step);
            steps.add(planStep);
        }
        plan.setSteps(steps);
        return createPlan(plan);
    }

    private String createPlan(Plan plan) {

        StringBuilder output = new StringBuilder();

        List<PlanStep> steps = plan.getSteps();

        this.plan = plan;

        int totalSteps = steps.size();

        output.append("Progress: ").append("(0%)\n");

        output.append("Status: ")
            .append(steps.size()).append(" not started\n\n");
        output.append("Steps:\n");

        for (int i = 0; i < totalSteps; i++) {
            PlanStep step = steps.get(i);

            output.append(i + 1).append(". ").append("[ ]").append(" ").append(step.getStepName()).append("\n");
        }

        System.out.println(output);
        return output.toString();
    }
}
