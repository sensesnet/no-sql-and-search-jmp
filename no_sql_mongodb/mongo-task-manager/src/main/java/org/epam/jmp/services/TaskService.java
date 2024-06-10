package org.epam.jmp.services;

import org.epam.jmp.models.Category;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public interface TaskService {

    String findAllTasks();
    String findOverdueTasks();
    String findAllTasksByCategory(Category category);
    String findAllSubtasksByTaskCategory(Category category);
    String createTask(LocalDateTime deadLine,
                      String name,
                      String description,
                      Category category);

    String updateTask(UUID id, Map<Integer, String> updateRequest);

    String deleteTask(UUID taskId);

    String createSubtask(String name, String description, UUID taskId);

    String updateSubtask(String oldName, String name, String description, UUID taskId);

    String deleteSubtaskByName(String name, UUID taskId);

    String findTasksByDescription(String query);
    String findSubtasksByName(String query);

    boolean isTaskExists(UUID taskId);
}
