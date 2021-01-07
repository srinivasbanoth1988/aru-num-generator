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
/**
 * Task service responsible for generating number. We are using static map to hold status and results. To Works with large data sets and distributed systems,
 * we can go for rabbitmq,kafka or redis
 */
@Service
public class TaskService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /** holds the tasks submitted from apis, Scheduler polls task from this queue and process.**/
    private final static ConcurrentLinkedQueue<Tasks> tasks = new ConcurrentLinkedQueue<>();
    /** This map holds results as array of strings**/
    private final static Map<String, List<String>> holder = new HashMap<>();
    /** this map holds the current status of the task**/
    private final static Map<String, Status> taskStatusHolder =  new HashMap<>();

    /**
     * Utility method to add tasks to queue
     *
     * @param tsks - submitted tasks from apis
     * @return - APIResponse with unique task id(UUID)
     */
    @Async
    public APIResponse publishTasks(Tasks tsks) {
        logger.info("> sendAsyncWithResult");
        tasks.add(tsks);
        return new APIResponse(tsks.getTaskId(), null);

    }
    /**
     * Utility method to poll tasks from queue and generates sequences and adds results to queue.
     *
     */
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

    /**
     * Utility method to fetch results of a particular task submitted.
     *
     * @param uuid - task id
     * @return - APIResponse with results.
     */
    public APIResponse getResults(final String uuid) {
        List<String> result =  holder.getOrDefault(uuid,new ArrayList<>());

        APIResponse apiResponse =  new APIResponse();
        if(result.size() == 1) {
            apiResponse.setResult(result.get(0));
        } else apiResponse.setResults(result);

        return apiResponse;
    }
    /**
     * Utility method to fetch status of a particular task submitted.
     *
     * @param uuid - task id
     * @return - returns status of a task.
     */
    public Status getStatus(final String uuid) {
        return taskStatusHolder.getOrDefault(uuid, Status.ERROR);
    }
    /**
     * Utility method to update status of a particular task submitted.
     *
     * @param uuid - task id
     * @param status - task status
     * @return - returns status of a task.
     */
    public synchronized void  updateStatus(final String uuid, final Status status) {
        taskStatusHolder.put(uuid, status);
    }
    /**
     * Utility method to update/save results of a particular task submitted.
     *
     * @param size - submitted tasks size
     * @param uuid - task id
     * @param results - task results
     * @return - returns status of a task.
     */
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
