package com.example.model;

import com.example.api.Attribute;
import com.example.api.ObjectType;

@ObjectType(id = "event")
public class EventInfo {

    private int countOfParticipants;

    @Attribute(id = "event_date_of_creation")
    private String eventDateOfCreation;

    @Attribute(id = "event_name")
    private String nameOfEvent;

    @Attribute(id = "event_name_of_creator")
    private String nameOfEventCreator;

    @Attribute(id = "event_description")
    private String eventDescription;

    @Attribute(id = "event_lat")
    private Double eventLatitude;

    @Attribute(id = "event_lng")
    private Double eventLongitude;

    @Attribute(id = "event_time")
    private String date;

    public EventInfo(){}

    public EventInfo(String nameOfEvent, String eventDescription) {
        this.nameOfEvent = nameOfEvent;
        this.eventDescription = eventDescription;
    }

    public EventInfo(String eventDateOfCreation, String nameOfEvent, String eventDescription, String date) {
        this.eventDateOfCreation = eventDateOfCreation;
        this.nameOfEvent = nameOfEvent;
        this.eventDescription = eventDescription;
        this.date = date;
    }

        public EventInfo(String date, String eventName, String eventNameOfCreator, String eventDescription, String eventLat, String eventLng) {
            this.nameOfEvent = eventName;
            this.eventLatitude =  Double.parseDouble(eventLat);
            this.eventLongitude = Double.parseDouble(eventLng);
            this.eventDescription = eventDescription;
            this.date = date;
            this.nameOfEventCreator = eventNameOfCreator;
        }

    public EventInfo(String eventName) {
        this.nameOfEvent = eventName;
    }

    public EventInfo(int countOfParticipants) {
        this.countOfParticipants = countOfParticipants;
    }

    /*public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }*/

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

    /*public String getNameOfEvent() {
        return nameOfEvent;
    }

    public void setNameOfEvent(String nameOfEvent) {
        this.nameOfEvent = nameOfEvent;
    }

    public String getNameOfEventCreator() {
        return nameOfEventCreator;
    }*/

    public void setNameOfEventCreator(String nameOfEventCreator) {
        this.nameOfEventCreator = nameOfEventCreator;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public int getCountOfParticipants() {
        return countOfParticipants;
    }

    public void setCountOfParticipants(int countOfParticipants) {
        this.countOfParticipants = countOfParticipants;
    }
}
