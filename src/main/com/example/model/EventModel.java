package com.example.model;

import com.example.api.Attribute;
import com.example.api.ObjectType;

@ObjectType(id = "event")
public class EventModel {

    private int countOfParticipant;

    private String countOfNewMessages;

    @Attribute(id = "event_name")
    private String nameOfEvent;

    @Attribute(id = "event_date_of_creation")
    private String eventDateOfCreation;

    @Attribute(id = "event_participant")
    private String eventParticipant;

    @Attribute(id = "event_id")
    private String id;

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

    @Attribute(id = "event_status")
    private String eventStatus = "active";

    public EventModel(){}

    public EventModel(String countOfNewMessages){
        this.countOfNewMessages = countOfNewMessages;
    }

    public EventModel(int countOfParticipant) {
        this.countOfParticipant = countOfParticipant;
    }

    public EventModel(String id, String nameOfEvent) {
        this.id = id;
        this.nameOfEvent = nameOfEvent;
    }

    public String getCountOfNewMessages() {
        return countOfNewMessages;
    }

    public void setCountOfNewMessages(String countOfNewMessages) {
        this.countOfNewMessages = countOfNewMessages;
    }

    public int getCountOfParticipant() {
        return countOfParticipant;
    }

    public void setCountOfParticipant(int countOfParticipant) {
        this.countOfParticipant = countOfParticipant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventDateOfCreation() {
        return eventDateOfCreation;
    }

    public void setEventDateOfCreation(String eventDateOfCreation) {
        this.eventDateOfCreation = eventDateOfCreation;
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

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }
}
