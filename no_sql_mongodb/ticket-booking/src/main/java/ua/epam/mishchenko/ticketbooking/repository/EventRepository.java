package ua.epam.mishchenko.ticketbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.epam.mishchenko.ticketbooking.model.Event;

import java.util.Date;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    Page<Event> getAllByTitle(Pageable pageable, String title);

    Page<Event> getAllByDate(Pageable pageable, Date day);

    Boolean existsByTitleAndDate(String title, Date date);
}
