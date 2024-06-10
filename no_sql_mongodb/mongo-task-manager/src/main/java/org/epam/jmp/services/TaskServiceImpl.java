package org.epam.jmp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.epam.jmp.models.Category;
import org.epam.jmp.models.Subtask;
import org.epam.jmp.models.Task;
import org.epam.jmp.repositories.TaskRepository;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    @Override
    public String findAllTasks() {

        var tasks = taskRepository.findAll();
        try {
            return MAPPER.writeValueAsString(tasks);
        } catch (JsonProcessingException e) {
            System.err.println(e);
            return "{}";
        }
    }

    @Override
    public String findOverdueTasks() {
        var tasks = taskRepository.findByDeadLineBefore(LocalDateTime.now());

        try {
            return MAPPER.writeValueAsString(tasks);
        } catch (JsonProcessingException e) {
            System.err.println(e);
            return "{}";
        }
    }

    @Override
    public String findAllTasksByCategory(Category category) {
        var tasks = taskRepository.findByCategory(category);

        try {
            return MAPPER.writeValueAsString(tasks);
        } catch (JsonProcessingException e) {
            System.err.println(e);
            return "{}";
        }
    }

    @Override
    public String findAllSubtasksByTaskCategory(Category category) {
        var tasks = taskRepository.findByCategory(category);
        Map<UUID, Set<Subtask>> subtasks = tasks.stream().collect(Collectors.toMap(Task::getId, Task::getSubtasks));
        try {
            return MAPPER.writeValueAsString(subtasks);
        } catch (JsonProcessingException e) {
            System.err.println(e);
            return "{}";
        }
    }

    @Override
    public String createTask(LocalDateTime deadLine, String name, String description, Category category) {

        var newTask = taskRepository.save(new Task(deadLine, name, description, new HashSet<>(), category));
        try {
            return MAPPER.writeValueAsString(newTask);
        } catch (JsonProcessingException e) {
            System.err.println(e);
            return "{}";
        }
    }

    @Override
    public String updateTask(UUID id, Map<Integer, String> updateRequest) {

        Task task = taskRepository.findById(id).orElseThrow();

        String deadLine = updateRequest.get(1);
        if (!deadLine.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            task.setDeadLine(LocalDateTime.parse(deadLine, formatter));
        }

        String newName = updateRequest.get(2);
        if (!newName.isEmpty())
            task.setName(newName);

        String newDesc = updateRequest.get(3);
        if (!newDesc.isEmpty())
            task.setDescription(newDesc);

        var updatedTask = taskRepository.save(task);

        try {
            return MAPPER.writeValueAsString(updatedTask);
        } catch (JsonProcessingException e) {
            System.err.println(e);
            return "{}";
        }
    }

    @Override
    public String deleteTask(UUID taskId) {
        var deletedTask = taskRepository.findById(taskId).orElseThrow();
        taskRepository.delete(deletedTask);

        try {
            return MAPPER.writeValueAsString(deletedTask);
        } catch (JsonProcessingException e) {
            System.err.println(e);
            return "{}";
        }
    }

    @Override
    public String createSubtask(String name, String description, UUID taskId) {

        var newSubtask = new Subtask(description, name);

        Task task = taskRepository.findById(taskId).orElseThrow();

        task.getSubtasks().add(newSubtask);

        var updatedTask = taskRepository.save(task);

        try {
            return MAPPER.writeValueAsString(updatedTask);
        } catch (JsonProcessingException e) {
            System.err.println(e);
            return "{}";
        }
    }

    @Override
    public String updateSubtask(String oldName, String name, String description, UUID taskId) {

        Task task = taskRepository.findById(taskId).orElseThrow();

        Subtask subtask = task.getSubtasks().stream().filter(st -> st.getName().equals(oldName)).findFirst().orElseThrow(() -> new IllegalArgumentException("Cannot find subtask by name: " + oldName));

        subtask.setName(name);
        subtask.setDescription(description);

        if (task.getSubtasks().add(subtask)){
            var updatedTask = taskRepository.save(task);

            try {
                return MAPPER.writeValueAsString(updatedTask);
            } catch (JsonProcessingException e) {
                System.err.println(e);
                return "{}";
            }
        } else {
            return "Error update sub-task: not unique name";
        }
    }

    @Override
    public String deleteSubtaskByName(String name, UUID taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();

        Subtask subtask = task.getSubtasks().stream().filter(st -> st.getName().equals(name)).findFirst().orElseThrow(() -> new IllegalArgumentException("Cannot find subtask by name: " + name));

        task.getSubtasks().remove(subtask);

        var updatedTask = taskRepository.save(task);

        try {
            return MAPPER.writeValueAsString(updatedTask);
        } catch (JsonProcessingException e) {
            System.err.println(e);
            return "{}";
        }
    }

    @Override
    public String findTasksByDescription(String query) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().caseSensitive(false).matching(query);
        List<Task> result = taskRepository.findAllBy(criteria);

        try {
            return MAPPER.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            System.err.println(e);
            return "{}";
        }
    }

    @Override
    public String findSubtasksByName(String query) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().caseSensitive(false).matching(query);
        List<Task> result = taskRepository.findAllBy(criteria);

        Map<UUID, Set<Subtask>> subtasks = result.stream().collect(Collectors.toMap(Task::getId, Task::getSubtasks));
        try {
            return MAPPER.writeValueAsString(subtasks);
        } catch (JsonProcessingException e) {
            System.err.println(e);
            return "{}";
        }
    }

    @Override
    public boolean isTaskExists(UUID taskId) {
        return taskRepository.existsById(taskId);
    }
}
