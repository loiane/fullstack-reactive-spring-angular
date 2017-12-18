package com.loiane.reactivespring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    @NotBlank
    @Size(max = 200)
    private String title;

    private boolean completed = false;
}
