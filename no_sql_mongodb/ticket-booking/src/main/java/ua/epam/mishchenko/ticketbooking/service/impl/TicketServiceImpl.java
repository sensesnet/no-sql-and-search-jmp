package ua.epam.mishchenko.ticketbooking.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.mishchenko.ticketbooking.model.Category;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.repository.EventRepository;
import ua.epam.mishchenko.ticketbooking.repository.TicketRepository;
import ua.epam.mishchenko.ticketbooking.repository.UserRepository;
import ua.epam.mishchenko.ticketbooking.service.TicketService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * The type Ticket service.
 */
@Service
public class TicketServiceImpl implements TicketService {

    /**
     * The constant log.
     */
    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    public TicketServiceImpl(UserRepository userRepository, EventRepository eventRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    /**
     * Book ticket.
     *
     * @param userId   the user id
     * @param eventId  the event id
     * @param place    the place
     * @param category the category
     * @return the ticket
     */
    @Override
    @Transactional
    public Ticket bookTicket(UUID userId, UUID eventId, int place, Category category) {
        log.info("Start booking a ticket for user with id {}, event with id event {}, place {}, category {}",
                userId, eventId, place, category);
        try {
            return processBookingTicket(userId, eventId, place, category);

        } catch (RuntimeException e) {
            log.warn("Can not to book a ticket for user with id {}, event with id {}, place {}, category {}",
                    userId, eventId, place, category, e);
            log.warn("Transaction rollback");
            return null;
        }
    }

    private Ticket processBookingTicket(UUID userId, UUID eventId, int place, Category category) {
        throwRuntimeExceptionIfUserNotExist(userId);
        throwRuntimeExceptionIfEventNotExist(eventId);
        throwRuntimeExceptionIfTicketAlreadyBooked(eventId, place, category);
        User user = getUser(userId);
        Event event = getEvent(eventId);
        throwRuntimeExceptionIfUserNotHaveEnoughMoney(user, event);
        buyTicket(user, event);
        Ticket ticket = saveBookedTicket(event, place, category);
        user.getTickets().add(ticket);
        userRepository.save(user);
        log.info("Successfully booking of the ticket: {}", ticket);
        return ticket;
    }

    private Ticket saveBookedTicket(Event event, int place, Category category) {
        return ticketRepository.save(createNewTicket(event, place, category));
    }

    private void buyTicket(User userAccount, Event event) {
        userAccount.setMoney(subtractTicketPriceFromUserMoney(userAccount, event));
    }

    private BigDecimal subtractTicketPriceFromUserMoney(User userAccount, Event event) {
        return userAccount.getMoney().subtract(event.getTicketPrice());
    }

    private void throwRuntimeExceptionIfUserNotHaveEnoughMoney(User user, Event event) {
        if (!userHasEnoughMoneyForTicket(user, event)) {
            throw new RuntimeException(
                    "The user with id " + user.getId() +
                            " does not have enough money for ticket with event id " + event.getId()
            );
        }
    }

    private void throwRuntimeExceptionIfTicketAlreadyBooked(UUID eventId, int place, Category category) {
        Event event = getEvent(eventId);
        if (ticketRepository.existsByEventAndPlaceAndCategory(event, place, category)) {
            throw new RuntimeException("This ticket already booked");
        }
    }

    private Event getEvent(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Can not to find an event by id: " + eventId));
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Can not to find a user account by user id: " + userId));
    }

    private void throwRuntimeExceptionIfEventNotExist(UUID eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("The event with id " + eventId + " does not exist");
        }
    }

    private void throwRuntimeExceptionIfUserNotExist(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("The user with id " + userId + " does not exist");
        }
    }

    private boolean userHasEnoughMoneyForTicket(User userAccount, Event event) {
        return userAccount.getMoney().compareTo(event.getTicketPrice()) > -1;
    }

    /**
     * Create new ticket.
     *
     * @param place    the place
     * @param category the category
     * @return the ticket
     */
    private Ticket createNewTicket(Event event, int place, Category category) {
        return new Ticket(event, place, category);
    }

    /**
     * Gets booked tickets.
     *
     * @param user     the user
     * @param pageSize the page size
     * @param pageNum  the page num
     * @return the booked tickets
     */
    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        log.info("Finding all booked tickets by user {} with page size {} and number of page {}",
                user, pageSize, pageNum);
        try {
            if (isUserNull(user)) {
                log.warn("The user can not be a null");
                return Collections.emptyList();
            }
            System.out.println(ticketRepository.findAll());
            List<Ticket> ticketsByUser = user.getTickets();
            if (!ticketsByUser.isEmpty()) {
                throw new RuntimeException("Can not to fina a list of booked tickets by user with id: " + user.getId());
            }
            log.info("All booked tickets successfully found by user {} with page size {} and number of page {}",
                    user, pageSize, pageNum);
            return ticketsByUser;
        } catch (RuntimeException e) {
            log.warn("Can not to find a list of booked tickets by user '{}'", user, e);
            return Collections.emptyList();
        }
    }

    /**
     * Is user null boolean.
     *
     * @param user the user
     * @return the boolean
     */
    private boolean isUserNull(User user) {
        return user == null;
    }

    /**
     * Gets booked tickets.
     *
     * @param event    the event
     * @param pageSize the page size
     * @param pageNum  the page num
     * @return the booked tickets
     */
    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        log.info("Finding all booked tickets by event {} with page size {} and number of page {}",
                event, pageSize, pageNum);
        try {
            if (isEventNull(event)) {
                log.warn("The event can not be a null");
                return new ArrayList<>();
            }
            Page<Ticket> ticketsByEvent = ticketRepository.getAllByEvent(
                    PageRequest.of(pageNum - 1, pageSize), event);
            if (!ticketsByEvent.hasContent()) {
                throw new RuntimeException("Can not to fina a list of booked tickets by event with id: " + event.getId());
            }
            log.info("All booked tickets successfully found by event {} with page size {} and number of page {}",
                    event, pageSize, pageNum);
            return ticketsByEvent.getContent();
        } catch (RuntimeException e) {
            log.warn("Can not to find a list of booked tickets by event '{}'", event, e);
            return new ArrayList<>();
        }
    }

    /**
     * Is event null boolean.
     *
     * @param event the event
     * @return the boolean
     */
    private boolean isEventNull(Event event) {
        return event == null;
    }

    /**
     * Cancel ticket boolean.
     *
     * @param ticketId the ticket id
     * @return the boolean
     */
    @Override
    public boolean cancelTicket(UUID ticketId) {
        log.info("Start canceling a ticket with id: {}", ticketId);
        try {
            ticketRepository.deleteById(ticketId);
            log.info("Successfully canceling of the ticket with id: {}", ticketId);
            return true;
        } catch (RuntimeException e) {
            log.warn("Can not to cancel a ticket with id: {}", ticketId, e);
            return false;
        }
    }
}
