package com.travel.assistant.domain;

import lombok.Data;

@Data
public class PlanStep {

    private String stepId;
    private String stepName;
    private String stepStatus;
    private String stepNotes;
}
