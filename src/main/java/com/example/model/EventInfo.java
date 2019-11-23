package com.example.model;

import java.time.LocalDateTime;
import java.util.Date;

public class EventInfo {

    private String eventDateOfCreation;
    private String nameOfEvent;
    private String nameOfEventCreator;
    private String eventDescription;
    private Double eventLatitude;
    private Double eventLongitude;
    private String date;

    public EventInfo(){}

    public EventInfo(String nameOfEvent, String nameOfEventCreator) {
        this.nameOfEvent = nameOfEvent;
        this.nameOfEventCreator = nameOfEventCreator;
    }

    public EventInfo(String eventDateOfCreation, String nameOfEvent, String nameOfEventCreator) {
        this.eventDateOfCreation = eventDateOfCreation;
        this.nameOfEvent = nameOfEvent;
        this.nameOfEventCreator = nameOfEventCreator;
    }

    public EventInfo(String eventDateOfCreation, String nameOfEvent, String nameOfEventCreator,
                     String eventDescription, String eventLatitude, String eventLongitude) {
        this.eventDateOfCreation = eventDateOfCreation;
        this.nameOfEvent = nameOfEvent;
        this.nameOfEventCreator = nameOfEventCreator;
        this.eventDescription = eventDescription;
        this.eventLatitude = Double.parseDouble(eventLatitude);
        this.eventLongitude = Double.parseDouble(eventLongitude);
    }


        public EventInfo(String eventName, String eventLat, String eventLng, String eventDescription, String date) {
            this.nameOfEvent = eventName;
            this.eventLatitude =  Double.parseDouble(eventLat);
            this.eventLongitude = Double.parseDouble(eventLng);
            this.eventDescription = eventDescription;
            this.date = date;
        }

    public EventInfo(String eventName) {
        this.nameOfEvent = eventName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEventDateOfCreation() {
        return eventDateOfCreation;
    }

    public Double getEventLatitude() {
        return eventLatitude;
    }

    public void setEventLatitude(Double eventLatitude) {
        this.eventLatitude = eventLatitude;
    }

    public Double getEventLongitude() {
        return eventLongitude;
    }

    public void setEventLongitude(Double eventLongitude) {
        this.eventLongitude = eventLongitude;
    }

    public void setEventDateOfCreation(String eventDateOfCreation) {
        this.eventDateOfCreation = eventDateOfCreation;
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

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
