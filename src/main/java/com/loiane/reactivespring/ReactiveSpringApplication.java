package com.loiane.reactivespring;

import com.loiane.reactivespring.model.Task;
import com.loiane.reactivespring.repository.TaskRepository;
import com.loiane.reactivespring.router.TaskRouter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class ReactiveSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveSpringApplication.class, args);
	}

	@Bean
	RouterFunction<?> taskRoutes(TaskRouter taskRouter) {
		return RouterFunctions.route(
				RequestPredicates.GET("/async/tasks"), taskRouter::getAll)
				.andRoute(RequestPredicates.GET("/async/stream/tasks/{id}"), taskRouter::events);
	}

	@Bean
	CommandLineRunner employees(TaskRepository taskRepository) {

		return args -> {
			taskRepository
					.deleteAll()
					.subscribe(null, null, () -> {
						Flux.interval(Duration.ofSeconds(1))
								.take(20)
								.map(i -> i.intValue() + 1)
								.map(i -> new Task(UUID.randomUUID().toString(), "TODO " + i, false))
								.map(record -> taskRepository.save(record)
										.subscribe(System.out::println))
								.subscribe();
					})
			;
		};

	}
}
