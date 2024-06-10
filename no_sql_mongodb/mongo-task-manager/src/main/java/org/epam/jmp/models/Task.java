package org.epam.jmp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Document(collection = "tasks")
public class Task {

    @Id
    private UUID id = UUID.randomUUID();
    private LocalDateTime dateOfCreation = LocalDateTime.now();
    private LocalDateTime deadLine;
    private String name;
    @TextIndexed
    private String description;
    private Set<Subtask> subtasks;
    private Category category;

    public Task() {
    }

    public Task(LocalDateTime deadLine, String name, String description, Set<Subtask> subtasks, Category category) {
        this.deadLine = deadLine;
        this.name = name;
        this.description = description;
        this.subtasks = subtasks;
        this.category = category;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDateTime deadLine) {
        this.deadLine = deadLine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Set<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
}
