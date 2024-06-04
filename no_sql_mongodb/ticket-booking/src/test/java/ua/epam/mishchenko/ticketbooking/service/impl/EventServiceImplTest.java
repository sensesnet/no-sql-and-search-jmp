package ua.epam.mishchenko.ticketbooking.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.repository.EventRepository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static ua.epam.mishchenko.ticketbooking.utils.Constants.DATE_FORMATTER;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventServiceImplTest {

    @Autowired
    private EventServiceImpl eventService;

    @MockBean
    private EventRepository eventRepository;

    @Test
    public void getEventByIdWithExistsIdShouldBeOk() throws ParseException {
        long eventId = 3L;
        Event expectedEvent = new Event(eventId, "Third event", DATE_FORMATTER.parse("16-05-2022 12:00"), BigDecimal.ONE);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(expectedEvent));

        Event actualEvent = eventService.getEventById(eventId);

        assertEquals(expectedEvent, actualEvent);
    }

    @Test
    public void getEventByIdWithExceptionShouldReturnNull() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        Event actualEvent = eventService.getEventById(10L);

        assertNull(actualEvent);
    }

    @Test
    public void getEventsByTitleWithExistsTitleShouldBeOk() throws ParseException {
        String title = "Third event";
        List<Event> content = Arrays.asList(
                new Event(3L, title, DATE_FORMATTER.parse("16-05-2022 12:00"), BigDecimal.ONE),
                new Event(5L, title, DATE_FORMATTER.parse("25-05-2022 9:10"), BigDecimal.ONE)
        );
        Page<Event> page = new PageImpl<>(content);

        when(eventRepository.getAllByTitle(any(Pageable.class), eq(title))).thenReturn(page);

        List<Event> actualEvents = eventService.getEventsByTitle(title, 2, 1);

        assertTrue(page.getContent().containsAll(actualEvents));
    }

    @Test
    public void getEventsByTitleWithExceptionShouldReturnEmptyList() {
        when(eventRepository.getAllByTitle(any(Pageable.class), anyString())).thenReturn(Page.empty());

        List<Event> actualEventsByTitle = eventService.getEventsByTitle("not exists title", 1, 1);

        assertTrue(actualEventsByTitle.isEmpty());
    }

    @Test
    public void getEventsByTitleWithEmptyTitleShouldReturnEmptyList() {
        List<Event> actualEventsByTitle = eventService.getEventsByTitle("", 1, 2);

        assertTrue(actualEventsByTitle.isEmpty());
    }

    @Test
    public void getEventsForDayWithExistsDayShouldBeOk() throws ParseException {
        Date day = DATE_FORMATTER.parse("15-05-2022 21:00");
        List<Event> content = Arrays.asList(
                new Event(2L, "Second event", DATE_FORMATTER.parse("15-05-2022 21:00"), BigDecimal.ONE),
                new Event(4L, "Fourth event", DATE_FORMATTER.parse("15-05-2022 21:00"), BigDecimal.ONE)
        );
        Page<Event> page = new PageImpl<>(content);

        when(eventRepository.getAllByDate(any(Pageable.class), eq(day))).thenReturn(page);

        List<Event> actualEvents = eventService.getEventsForDay(day, 2, 1);

        assertTrue(page.getContent().containsAll(actualEvents));
    }

    @Test
    public void getEventsForDayWithExceptionShouldReturnEmptyList() throws ParseException {
        Date day = DATE_FORMATTER.parse("15-05-2000 21:00");

        when(eventRepository.getAllByDate(any(Pageable.class), eq(day))).thenThrow(RuntimeException.class);

        List<Event> actualEventsForDay = eventService.getEventsForDay(day, 1, 1);

        assertTrue(actualEventsForDay.isEmpty());
    }

    @Test
    public void getEventsForDayWithNullDayShouldReturnEmptyList() {
        List<Event> actualEventsForDay = eventService.getEventsForDay(null, 1, 2);

        assertTrue(actualEventsForDay.isEmpty());
    }

    @Test
    public void createEventWithExceptionShouldReturnNull() {
        when(eventRepository.save(any(Event.class))).thenThrow(RuntimeException.class);

        Event actualEvent = eventService.createEvent(new Event());

        assertNull(actualEvent);
    }

    @Test
    public void createEventWithExistsTitleAndEmailShouldReturnNull() throws ParseException {
        Event expectedEvent = new Event(1L, "Second event", DATE_FORMATTER.parse("15-05-2022 21:00"), BigDecimal.ONE);

        when(eventRepository.save(expectedEvent)).thenReturn(expectedEvent);

        Event actualEvent = eventService.createEvent(expectedEvent);

        assertEquals(expectedEvent, actualEvent);
    }

    @Test
    public void createEventWithNullEventShouldReturnNull() {
        Event actualEvent = eventService.createEvent(null);

        assertNull(actualEvent);
    }

    @Test
    public void updateEventWithExistsEventShouldBeOk() throws ParseException {
        Event expectedEvent = new Event(1L, "Second event", DATE_FORMATTER.parse("15-05-2022 21:00"), BigDecimal.ONE);

        when(eventRepository.existsById(anyLong())).thenReturn(true);
        when(eventRepository.save(any(Event.class))).thenReturn(expectedEvent);

        Event actualEvent = eventService.updateEvent(expectedEvent);

        assertEquals(expectedEvent, actualEvent);
    }

    @Test
    public void updateEventWithExceptionShouldReturnNull() {
        when(eventRepository.save(any(Event.class))).thenThrow(RuntimeException.class);

        Event actualEvent = eventService.updateEvent(new Event());

        assertNull(actualEvent);
    }

    @Test
    public void updateEventWithNullEventShouldReturnNull() {
        Event actualEvent = eventService.updateEvent(null);

        assertNull(actualEvent);
    }

    @Test
    public void deleteEventExistsEventShouldReturnTrue() {
        boolean actualIsDeleted = eventService.deleteEvent(6L);

        assertTrue(actualIsDeleted);
    }

    @Test
    public void deleteEventWithExceptionShouldReturnFalse() {
        doThrow(new RuntimeException()).when(eventRepository).deleteById(anyLong());

        boolean actualIsDeleted = eventService.deleteEvent(10L);

        assertFalse(actualIsDeleted);
    }
}