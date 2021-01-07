package org.aru.number.generator.api.service;

import org.aru.number.generator.enums.Status;
import org.aru.number.generator.exception.TaskException;
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
    private final static Map<String, String[]> holder = new HashMap<>();
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
        tasks.add(tsks);
        updateStatus(tsks.getTaskId(),Status.SUBMITTED);
        logger.info("Task added to Queue {}", tsks.getTaskId());
        return new APIResponse(tsks.getTaskId(), null);

    }
    /**
     * Utility method to poll tasks from queue and generates sequences and adds results to queue.
     *
     */
    public void processTasks() {
        while (!tasks.isEmpty()) {
            Tasks tsks = tasks.poll();
            logger.info("Processing started for task id {}", tsks.getTaskId());
            taskStatusHolder.put(tsks.getTaskId(), Status.IN_PROGRESS);
            List<Integer> indexs =  new ArrayList<>();
            tsks.getTasks().parallelStream().forEachOrdered(tsk -> {
                CompletableFuture.supplyAsync(() -> {
                    List<Integer> seq =  new ArrayList<>();
                    try {
                        for (int i = tsk.getGoal() ; i>=0 ; i=i-tsk.getStep()) {
                            seq.add(i);
                        }
                    } catch (Exception ex) {
                        logger.error("Exception occurred in sequnece generation  and task id {}",tsks.getTaskId());
                        updateStatus(tsks.getTaskId(), Status.ERROR);
                        throw new TaskException(tsks.getTaskId());
                    }


                    return seq.stream().map(String::valueOf)
                            .collect(Collectors.joining(","));
                }).thenAccept(result -> {
                    try {
                        updateStatus(tsks.getTaskId(), Status.IN_PROGRESS);
                        String[]  stringList = holder.getOrDefault(tsks.getTaskId(), new String[tsks.getTasks().size()]);
                        int index  = tsks.getTasks().indexOf(tsk);
                        stringList[index] = result;
                        holder.put(tsks.getTaskId(), stringList);
                        indexs.add(index);
                        if(stringList.length == indexs.size()) {
                            updateStatus(tsks.getTaskId(), Status.SUCCESS);
                            logger.info("Processing ended for task id {}", tsks.getTaskId());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        logger.error("Exception occurred in sequnece generation and task id {}",tsks.getTaskId());
                        updateStatus(tsks.getTaskId(), Status.ERROR);
                        throw new TaskException(tsks.getTaskId());
                    }

                });
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
        String[] result =  holder.getOrDefault(uuid, new String[0]);

        APIResponse apiResponse =  new APIResponse();
        if(result.length == 1) {
            apiResponse.setResult(result[0]);
        } else apiResponse.setResults(Arrays.asList(result));

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
}
