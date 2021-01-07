package org.aru.number.generator.model;

import java.io.Serializable;

public class Task implements Serializable {
    private String task;
    private Integer goal;
    private Integer step;
    

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
