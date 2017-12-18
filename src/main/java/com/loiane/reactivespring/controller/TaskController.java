package com.loiane.reactivespring.controller;

import com.loiane.reactivespring.model.Task;
import com.loiane.reactivespring.model.TaskEvent;
import com.loiane.reactivespring.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("tasks")
    public Flux<Task> getAll() {
        return taskService.getAll();
    }

    @PostMapping("tasks")
    public Mono<Task> create(@Valid @RequestBody Task task) {
        return taskService.create(task);
    }

    @PutMapping("/tasks/{id}")
    public Mono<ResponseEntity<Task>> update(@PathVariable String id, @Valid @RequestBody Task task) {
        return taskService.update(id, task)
                .map(updatedRecord -> new ResponseEntity<>(updatedRecord, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/tasks/{id}")
    public Mono<ResponseEntity<Task>> delete(@PathVariable String id) {
        return taskService.delete(id)
                .map(deletedRecord -> new ResponseEntity<>(deletedRecord, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/stream/tasks/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TaskEvent> streamAll(@PathVariable String id) {
        return taskService.streams(id);
    }

    // @GetMapping(value = "/stream/tasks", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @GetMapping(value = "/stream/tasks")
    public Flux<Task> getAllStream() {
        return taskService.getAll().delayElements(Duration.ofSeconds(2));
    }

    @GetMapping(value = "/stream/tasks2", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Task> getAllStream2() {
        return taskService.getAll().delayElements(Duration.ofSeconds(2));
    }

}
