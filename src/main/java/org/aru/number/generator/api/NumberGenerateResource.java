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

@RequestMapping("api")
@RestController
public class NumberGenerateResource {

    @Autowired
    private TaskService holder;

    @PostMapping("/generate")
    public ResponseEntity<APIResponse> generateNumbers(@RequestBody Task task) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(holder.publishTasks(new Tasks(UUID.randomUUID().toString(),task)));
    }
    @GetMapping("/tasks/{uuid}/status")
    public ResponseEntity<Status> getStatus(@PathVariable("uuid") String uuid) {
        return ResponseEntity.status(HttpStatus.OK).body(holder.getStatus(uuid));
    }
    @GetMapping("/tasks/{uuid}")
    public ResponseEntity<APIResponse> getResultsList(@PathVariable("uuid") String uuid,
                                                 @RequestParam("action") String action) {
        return ResponseEntity.status(HttpStatus.OK).body(holder.getResults(uuid));
    }
    @PostMapping("/bulkGenerate")
    public ResponseEntity<APIResponse> bulkGenerate(@RequestBody List<Task> tasks) {
        String uuid = UUID.randomUUID().toString();
        APIResponse response = new APIResponse(uuid, null);
        holder.publishTasks(new Tasks(uuid,tasks));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

    }
}
