package io.pivotal.pal.tracker;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> map = new HashMap<>();
    private long timeId = 1;

    public TimeEntry find(long id) {
        return map.get(id);
    }

    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry tempTimeEntry = new TimeEntry(timeId++, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        map.put(tempTimeEntry.getId(), tempTimeEntry);
        return tempTimeEntry;
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (map.containsKey(id)) {
            TimeEntry tempTimeEntry = new TimeEntry(id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
            map.put(id, tempTimeEntry);
            return map.get(id);
        } else {
            return null;
        }
    }

    public Boolean delete(long id) {
        if (map.containsKey(id)) {
            map.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(map.values());
    }
}
