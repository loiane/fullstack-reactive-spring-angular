package com.loiane.reactivespring.router;

import com.loiane.reactivespring.model.Task;
import com.loiane.reactivespring.model.TaskEvent;
import com.loiane.reactivespring.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TaskRouter {

    @Autowired
    private TaskService taskService;

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(taskService.getAll(), Task.class)
                .doOnError(throwable -> new IllegalStateException(":("));
    }

    public Mono<ServerResponse> events(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(taskService.streams(id), TaskEvent.class)
                .doOnError(throwable -> new IllegalStateException(":("));
    }
}
