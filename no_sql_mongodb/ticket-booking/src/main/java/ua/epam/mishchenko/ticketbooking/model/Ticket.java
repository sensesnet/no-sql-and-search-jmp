package ua.epam.mishchenko.ticketbooking.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "tickets")
public class Ticket {

    @Id
    private UUID id = UUID.randomUUID();
    private Event event;
    private Integer place;
    private Category category;

    public Ticket(Event event, Integer place, Category category) {
        this.event = event;
        this.place = place;
        this.category = category;
    }

    public Ticket() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
