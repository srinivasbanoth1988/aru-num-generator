package org.aru.number.generator.api.service;

import org.aru.number.generator.enums.Status;
import org.aru.number.generator.model.APIResponse;
import org.aru.number.generator.model.Tasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final static ConcurrentLinkedQueue<Tasks> tasks = new ConcurrentLinkedQueue<>();
    private final static Map<String, List<String>> holder = new HashMap<>();
    private final static Map<String, Status> taskStatusHolder =  new HashMap<>();

    @Async
    public APIResponse publishTasks(Tasks tsks) {
        logger.info("> sendAsyncWithResult");
        tasks.add(tsks);
        return new APIResponse(tsks.getTaskId(), null);

    }

    public void processTasks() {
        while (!tasks.isEmpty()) {
            Tasks tsks = tasks.poll();
            taskStatusHolder.put(tsks.getTaskId(), Status.IN_PROGRESS);
            tsks.getTasks().stream().forEach(tsk -> {
                CompletableFuture.supplyAsync(() -> {
                    List<Integer> seq =  new ArrayList<>();
                    for (int i = tsk.getGoal() ; i>=0 ; i=i-tsk.getStep()) {
                        seq.add(i);
                    }
                    return seq.stream().map(String::valueOf)
                            .collect(Collectors.joining(","));
                }).thenAccept(result -> fillGeneratedNumbers(tsks.getTasks().size(),tsks.getTaskId(),result));
            });
        }

    }
    public APIResponse getResults(final String uuid) {
        List<String> result =  holder.getOrDefault(uuid,new ArrayList<>());

        APIResponse apiResponse =  new APIResponse();
        if(result.size() == 1) {
            apiResponse.setResult(result.get(0));
        } else apiResponse.setResults(result);

        return apiResponse;
    }
    public Status getStatus(final String uuid) {
        return taskStatusHolder.getOrDefault(uuid, Status.ERROR);
    }
    public synchronized void  updateStatus(final String uuid, final Status status) {
        taskStatusHolder.put(uuid, status);
    }
    public synchronized void fillGeneratedNumbers(int size, String uuid , String results) {
        updateStatus(uuid, Status.IN_PROGRESS);
        List<String>  stringList = holder.getOrDefault(uuid, new ArrayList<>());
        stringList.add(results);
        holder.put(uuid, stringList);
        if(size == holder.get(uuid).size()) {
            updateStatus(uuid, Status.SUCCESS);
        }


    }
}
