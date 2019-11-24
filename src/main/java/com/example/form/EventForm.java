package com.example.form;

import com.example.api.Attribute;
import com.example.api.ObjectType;

import java.sql.Date;

@ObjectType(id = "event")
public class EventForm {

    @Attribute(id = "event_name")
    private String nameOfEvent;

    @Attribute(id = "event_name_of_creator")
    private String nameOfEventCreator;

    @Attribute(id = "event_description")
    private String descriptionOfEvent;

    @Attribute(id = "event_lat")
    private String latitude;

    @Attribute(id = "event_lng")
    private String longitude;

    @Attribute(id = "event_time")
    private String date;

    @Attribute(id = "event_participant")
    private String eventParticipant;

    public EventForm(){}

    public EventForm(String nameOfEvent, String nameOfEventCreator) {
        this.nameOfEvent = nameOfEvent;
        this.nameOfEventCreator = nameOfEventCreator;
    }

    public EventForm(String nameOfEvent, String nameOfEventCreator, String descriptionOfEvent, String latitude, String longitude, String date) {
        this.nameOfEvent = nameOfEvent;
        this.nameOfEventCreator = nameOfEventCreator;
        this.descriptionOfEvent = descriptionOfEvent;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public String getEventParticipant() {
        return eventParticipant;
    }

    public void setEventParticipant(String eventParticipant) {
        this.eventParticipant = eventParticipant;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescriptionOfEvent() {
        return descriptionOfEvent;
    }

    public void setDescriptionOfEvent(String descriptionOfEvent) {
        this.descriptionOfEvent = descriptionOfEvent;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNameOfEvent() {
        return nameOfEvent;
    }

    public void setNameOfEvent(String nameOfEvent) {
        this.nameOfEvent = nameOfEvent;
    }

    public String getNameOfEventCreator() {
        return nameOfEventCreator;
    }

    public void setNameOfEventCreator(String nameOfEventCreator) {
        this.nameOfEventCreator = nameOfEventCreator;
    }
}
