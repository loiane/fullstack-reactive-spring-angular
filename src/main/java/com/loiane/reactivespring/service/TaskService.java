package com.loiane.reactivespring.service;

import com.loiane.reactivespring.model.Task;
import com.loiane.reactivespring.model.TaskEvent;
import com.loiane.reactivespring.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Date;
import java.util.stream.Stream;

import static java.time.Duration.ofSeconds;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Flux<Task> getAll() {
        return taskRepository.findAll();
    }

    public Mono<Task> create(Task task) {
        return taskRepository.save(task);
    }

    public Mono<Task> update(String id, Task task) {
        return taskRepository.findById(id)
                .flatMap(record -> {
                    record.setCompleted(task.isCompleted());
                    record.setTitle(task.getTitle());
                    return taskRepository.save(record);
                });
    }

    public Mono<Task> delete(String id) {
        return taskRepository.findById(id)
                .flatMap(record -> {
                    taskRepository.delete(record);
                    return Mono.just(record);
                });
    }

    public Mono<Task> getById(String id) {
        return taskRepository.findById(id);
    }

    public Flux<TaskEvent> streams(String id) {
        return getById(id).flatMapMany(record -> {
            Flux<Long> interval = Flux.interval(ofSeconds(1));
            Flux<TaskEvent> events = Flux.fromStream(
                    Stream.generate(() -> new TaskEvent(record, new Date())));
            return Flux.zip(interval, events).map(Tuple2::getT2);
        });
    }
}
