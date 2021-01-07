package org.aru.number.generator.model;

import java.util.List;

public class TaskWorkerResult {

    private String uuid;
    private List<String> results;

    public TaskWorkerResult(String uuid, List<String> results) {
        this.uuid = uuid;
        this.results = results;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }
}
