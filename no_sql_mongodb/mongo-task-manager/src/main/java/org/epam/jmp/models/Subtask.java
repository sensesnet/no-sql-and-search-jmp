package org.epam.jmp.models;

import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.Objects;

public class Subtask {

    private String description;

    @TextIndexed
    private String name;

    public Subtask() {
    }

    public Subtask(String description, String name) {
        this.description = description;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(name, subtask.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
