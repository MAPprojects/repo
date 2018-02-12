package Service;

import Domain.Event;
import Domain.ExceptionValidator;
import Domain.Validator;
import Repository.EventDatabaseRepository;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EventService extends AbstractService<Integer, Event> {

    public EventService(Validator validator, EventDatabaseRepository repository) {
        super(validator, repository);
    }

    public List<Event> filterUser(Integer userId)
    {
        return filterAndSorter(StreamSupport.stream(this.getAll().spliterator(), false)
                .map(this::find)
                .collect(Collectors.toList()),
                x -> x.getUserIds().contains(userId),
                Comparator.comparingInt(Event::getId));
    }

    public void addUserToEvent(Integer userId, Integer eventId) throws ExceptionValidator{
        Event event = this.find(eventId);
        if (event.getDeadline().compareTo(new Date()) < 0)
            throw new ExceptionValidator("Deadline passed !");
        event.getUserIds().add(userId);
        ((EventDatabaseRepository) this.repository).saveUserEvent(userId, eventId);
    }

    @Override
    public void remove(Integer item) throws IOException {
        this.find(item).getUserIds().forEach(userId -> ((EventDatabaseRepository)
                this.repository).deleteUserEvent(userId, item));
        super.remove(item);
    }
}
