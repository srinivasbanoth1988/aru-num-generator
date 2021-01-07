package org.aru.number.generator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.aru.number.generator.enums.Status;

import java.util.List;

public class APIResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String task;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> results;


    public APIResponse(String task, String result) {
        this.result = result;
        this.task = task;
    }
    public APIResponse() {
    }
    public APIResponse(List<String> results) {
        this.results = results;
    }
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

}
