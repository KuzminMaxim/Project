package com.example.scheduler;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.EventModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTask {

    @Autowired
    ApiForInteractingWithTheDatabase api;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Scheduled(fixedRate = 1000*60*60*12)
    public void updateEventStatus(){

        Date date = new Date();
        Timestamp current = new Timestamp(date.getTime());

        List<EventModel> allEvents = api.readAll(EventModel.class);

        for (EventModel event : allEvents) {
            if (event.getEventStatus().equals("active")) {
                event.setDate(event.getDate().concat(":00").replace('T', ' '));
                Timestamp lastTs = Timestamp.valueOf(event.getDate());
                if (current.after(lastTs)) {
                    event.setEventStatus("complete");
                    api.update(event);
                    logger.info("Event {} was updated", event.getId());
                }
            }
        }
    }

}
