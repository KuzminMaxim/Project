package com.example.form;

import com.example.api.Attribute;
import com.example.api.ObjectType;

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

    public EventForm(){}

    public EventForm(String nameOfEvent, String nameOfEventCreator) {
        this.nameOfEvent = nameOfEvent;
        this.nameOfEventCreator = nameOfEventCreator;
    }

    public EventForm(String nameOfEvent, String nameOfEventCreator, String descriptionOfEvent, String latitude, String longitude) {
        this.nameOfEvent = nameOfEvent;
        this.nameOfEventCreator = nameOfEventCreator;
        this.descriptionOfEvent = descriptionOfEvent;
        this.latitude = latitude;
        this.longitude = longitude;
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
