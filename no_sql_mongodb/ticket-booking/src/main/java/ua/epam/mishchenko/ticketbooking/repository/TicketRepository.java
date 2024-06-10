package ua.epam.mishchenko.ticketbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ua.epam.mishchenko.ticketbooking.model.Category;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;

import java.util.UUID;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, UUID> {
    Page<Ticket> getAllByEvent(PageRequest of, Event event);

    boolean existsByEventAndPlaceAndCategory(Event event, int place, Category category);
}
