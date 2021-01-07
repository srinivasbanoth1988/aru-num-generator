package org.aru.number.generator.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id", scope = Tasks.class)
public class Tasks implements Serializable {
    @JsonProperty("tasks")
    private String taskId;
    private List<Task> tasks = new ArrayList<>();

    public Tasks(String taskId, List<Task> tasks) {
        this.taskId = taskId;
        this.tasks = tasks;
    }
    public Tasks(String taskId, Task task) {
        this.taskId = taskId;
        this.tasks.add(task);
    }
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
