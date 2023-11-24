package ca.utoronto.cscb07project.ui.events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Event>> allEventsList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Event>> attendingEventsList = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Event>> getAllEventsList() {
        return allEventsList;
    }

    public LiveData<List<Event>> getAttendingEventsList() {
        return attendingEventsList;
    }

    public void addEvent(Event event) {
        List<Event> currentAllEvents = allEventsList.getValue();
        currentAllEvents.add(event);
        allEventsList.setValue(currentAllEvents);
    }

    public void rsvpEvent(Event event) {
        List<Event> currentAllEvents = allEventsList.getValue();
        currentAllEvents.remove(event);
        allEventsList.setValue(currentAllEvents);

        List<Event> currentAttendingEvents = attendingEventsList.getValue();
        currentAttendingEvents.add(event);
        attendingEventsList.setValue(currentAttendingEvents);
    }
}
