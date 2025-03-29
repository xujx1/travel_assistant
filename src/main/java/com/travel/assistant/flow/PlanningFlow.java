package com.travel.assistant.flow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.travel.assistant.agent.Agent;
import com.travel.assistant.domain.Plan;
import com.travel.assistant.eums.StepStatusEnum;
import com.travel.assistant.tool.PlanTool;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
public class PlanningFlow {

    @Resource
    private ChatLanguageModel model;

    @Resource
    private Map<String, Agent> agentMap;

    private static final String PLANNING_SYSTEM_PROMPT
        = "你是一个资深旅行助手，能将用户负责的出行需求，分解为包含交通、住宿、景点信息等可管理的小的步骤，"
        + "并且一步步执行，并记录执行进度。"
        + "比如提醒用户补充信息(日期、时间、出发城市、目的城市等)、查询交通出行方案、查询酒店信息等，"
        + "最终帮助用户完成整个出行规划（包含去程、返程）的旅行助手。\n"
        + "今天的日期是 {current_date}。";

    public String execute(String inputText) {

        PlanTool tool = new PlanTool();

        StringBuilder result = new StringBuilder();

        String createInitialPlan = createInitialPlan(inputText, tool);

        result.append(createInitialPlan);

        Plan plan = tool.getPlan();

        List<String> inputList = new ArrayList<>();
        inputList.add(inputText);

        if (plan == null || CollectionUtils.isEmpty(plan.getSteps())) {
            return "任务拆解失败";
        }

        plan.getSteps().forEach(step -> {
            step.setStepStatus(StepStatusEnum.in_progress.name());
            System.out.println("当前步骤：" + step.getStepName());

            try {
                List<ChatMessage> messages = new ArrayList<>();
                messages.add(new SystemMessage(
                    "你是一个任务分发助手，能够解析用户的意图，必须选择一个合适的工具去处理当前需要解决的问题。只需要返回工具名\n"
                        + "工具列表如下，格式工具名：工具描述" +
                        "<工具列表>\n"
                        + "hotelAgent: 主要任务是查询、订酒店\n"
                        + "trafficAgent: 选择合适的交通出行方式并且预定\n"
                        + "completeAgent: 获取更多的用户输入，比如时间等\n"
                        + "</工具列表>\n"
                        + "\n"
                        + "格式要求：\n"
                        + "工具名: 工具名称，类似：hotelAgent"));
                messages.add(new UserMessage(step.getStepName()));

                String agentName = model.chat(messages).aiMessage().text();
                Agent agent = agentMap.get(agentName);
                String res = agent.executeStep(step, inputList);
                System.out.println(res);
                step.setStepNotes(res);
                step.setStepStatus(StepStatusEnum.completed.name());
            } catch (Exception e) {
                step.setStepStatus(StepStatusEnum.blocked.name());
                e.printStackTrace();
            }
        });

        return "";

    }

    /**
     * Create initial plan
     * @param request
     */
    private String createInitialPlan(String request, PlanTool tool) {
        List<ChatMessage> messages = new ArrayList<>();
        PromptTemplate template = new PromptTemplate(PLANNING_SYSTEM_PROMPT);
        String prompt = template.apply(Map.of("current_date", LocalDate.now().toString())).text();

        messages.add(new SystemMessage(prompt));
        messages.add(new UserMessage(request));

        PlanningFlowAssistant assistant = AiServices.builder(PlanningFlowAssistant.class)
            .chatLanguageModel(model)
            .tools(tool)
            .build();

        return assistant.execute(request, tool);
    }
}
