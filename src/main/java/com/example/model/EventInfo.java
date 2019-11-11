package com.example.model;

public class EventInfo {

    private String eventDateOfCreation;
    private String nameOfEvent;
    private String nameOfEventCreator;
    private String eventLatitude;
    private String eventLongitude;

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

    public EventInfo(String eventDateOfCreation, String eventName, String eventNameOfCreator, String eventLatitude, String eventLongitude) {
        this.nameOfEventCreator = eventNameOfCreator;
        this.nameOfEvent = eventName;
        this.eventDateOfCreation = eventDateOfCreation;
        this.eventLatitude = eventLatitude;
        this.eventLongitude = eventLongitude;
    }

    public String getEventDateOfCreation() {
        return eventDateOfCreation;
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

}
