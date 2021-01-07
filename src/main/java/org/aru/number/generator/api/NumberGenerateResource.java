package org.aru.number.generator.api;

import org.aru.number.generator.api.service.TaskService;
import org.aru.number.generator.enums.Status;
import org.aru.number.generator.model.APIResponse;
import org.aru.number.generator.model.Task;
import org.aru.number.generator.model.Tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
/**
 * This class handles the controls for REST API calls and processes
 * request/response accordingly
 */
@RequestMapping("api")
@RestController
public class NumberGenerateResource {

    @Autowired
    private TaskService holder;
    /**
     * Rest API for single task
     * @param task-  Submitted task
     * @return - returns task id
     */
    @PostMapping("/generate")
    public ResponseEntity<APIResponse> generateNumbers(@RequestBody Task task) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(holder.publishTasks(new Tasks(UUID.randomUUID().toString(),task)));
    }

    /**
     * Rest API for fetching tasks status
     * @param uuid -  uuid of a task
     * @return - returns tasks status
     */

    @GetMapping("/tasks/{uuid}/status")
    public ResponseEntity<Status> getStatus(@PathVariable("uuid") String uuid) {
        return ResponseEntity.status(HttpStatus.OK).body(holder.getStatus(uuid));
    }

    /**
     * Rest API for fetching results
     * @param uuid - uuid of a task
     * @param action - action of a task
     * @return - returns tasks results
     */

    @GetMapping("/tasks/{uuid}")
    public ResponseEntity<APIResponse> getResultsList(@PathVariable("uuid") String uuid,
                                                 @RequestParam("action") String action) {
        return ResponseEntity.status(HttpStatus.OK).body(holder.getResults(uuid));
    }
    /**
     * Rest API for multiple/bulk task results
     * @param tasks - bulk task list
     * @return - returns tasks results
     */

    @PostMapping("/bulkGenerate")
    public ResponseEntity<APIResponse> bulkGenerate(@RequestBody List<Task> tasks) {
        String uuid = UUID.randomUUID().toString();
        APIResponse response = new APIResponse(uuid, null);
        holder.publishTasks(new Tasks(uuid,tasks));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

    }
}
