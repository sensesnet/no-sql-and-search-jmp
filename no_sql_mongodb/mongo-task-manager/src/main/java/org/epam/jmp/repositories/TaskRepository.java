package org.epam.jmp.repositories;

import org.epam.jmp.models.Category;
import org.epam.jmp.models.Task;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends MongoRepository<Task, UUID> {
    List<Task> findByDeadLineBefore(LocalDateTime deadline);
    List<Task> findByCategory(Category category);

    List<Task> findAllBy(TextCriteria criteria);
}
