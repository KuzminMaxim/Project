package com.example.mapper;

import com.example.model.EventInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventMapperNewDB implements RowMapper<EventInfo> {

    public static final String SELECT_ALL_EVENTS = "select distinct A.value_date as event_date_of_creation,\n" +
            "            B.value_text as event_name,\n" +
            "            C.value_text as event_name_of_creator \n" +
            "            from params as A, params as B, params as C\n" +
            "            where A.value_date in (select params.value_date as event_date_of_creation from params\n" +
            "            join object on params.object_id = object.id\n" +
            "            join attributes on attributes.id = params.attribute_id\n" +
            "            where attributes.Attribute = 'event_date_of_creation'\n" +
            "            and object.id IN \n" +
            "            (select distinct params.object_id from params))\n" +
            "            and B.value_text in (select params.value_text as event_name from params\n" +
            "            join object on params.object_id = object.id\n" +
            "            join attributes on attributes.id = params.attribute_id\n" +
            "            where attributes.Attribute = 'event_name'\n" +
            "            and object.id IN \n" +
            "            (select distinct params.object_id from params))\n" +
            "            and C.value_text in (select params.value_text as event_name_of_creator from params\n" +
            "            join object on params.object_id = object.id\n" +
            "            join attributes on attributes.id = params.attribute_id\n" +
            "            where attributes.Attribute = 'event_name_of_creator'\n" +
            "            and object.id IN \n" +
            "            (select distinct params.object_id from params))\n" +
            "            and A.object_id = B.object_id \n" +
            "            and A.object_id = C.object_id";

    public static final String SELECT_ONE_EVENT_SQL = "" +
            "SELECT DISTINCT (select params.value_date from params\n" +
        "            join attributes on attributes.id = params.attribute_id\n" +
        "            join object on params.object_id = object.id\n" +
        "            where attributes.Attribute = 'event_date_of_creation'\n" +
        "            and object.id = \n" +
        "            (select distinct params.object_id from params where value_text = ?)) as event_date_of_creation,\n" +
        "            (select params.value_text from params\n" +
        "            join object on params.object_id = object.id\n" +
        "            join attributes on attributes.id = params.attribute_id\n" +
        "            where attributes.Attribute = 'event_name'\n" +
        "            and object.id = \n" +
        "            (select distinct params.object_id from params where value_text = ?)) as event_name,\n" +
        "            (select params.value_text from params\n" +
        "            join object on params.object_id = object.id\n" +
        "            join attributes on attributes.id = params.attribute_id\n" +
        "            where attributes.Attribute = 'event_name_of_creator'\n" +
        "            and object.id = \n" +
        "            (select distinct params.object_id from params where value_text = ?)) as event_name_of_creator\n" +
        "            from params";

    public static final String CREATE_OBJECT_EVENT_SQL ="insert into object(object_type_id, name) values\n" +
            "            ((select object_types.id from object_types where object_types.Object_Type = 'event'),'OBJECT_EVENT');";

    public static final String INSERT_DATE_OF_CREATION_SQL = "insert into params(object_id, attribute_id, value_date)\n" +
            "values ((select id from object order by id desc\n" +
            "limit 1),\n" +
            "(select attributes.id from attributes where attributes.Attribute = 'event_date_of_creation'), CURDATE());";

    public static final String INSERT_NAME_OF_EVENT_SQL = "insert into params(object_id, attribute_id, value_text)\n" +
            "values ((select id from object order by id desc\n" +
            "limit 1),\n" +
            "(select attributes.id from attributes where attributes.Attribute = 'event_name'), ?);";

    public static final String INSERT_NAME_OF_CREATOR_SQL = "insert into params(object_id, attribute_id, value_text)\n" +
            "values ((select id from object order by id desc\n" +
            "limit 1),\n" +
            "(select attributes.id from attributes where attributes.Attribute = 'event_name_of_creator'), ?);";

    public static final String INSERT_LATITUDE_OF_EVENT_SQL = "insert into params(object_id, attribute_id, value_text)\n" +
            "values ((select id from object order by id desc\n" +
            "limit 1),\n" +
            "(select attributes.id from attributes where attributes.Attribute = 'event_lat'), ?);";

    public static final String INSERT_LONGITUDE_OF_EVENT_SQL = "insert into params(object_id, attribute_id, value_text)\n" +
            "values ((select id from object order by id desc\n" +
            "limit 1),\n" +
            "(select attributes.id from attributes where attributes.Attribute = 'event_lng'), ?);";

    public static final String CREATE_EVENT_OBJECT_REFERENCES = "update params\n" +
            "Set params.object_references = (select id from object order by id desc\n" +
            "limit 1)\n" +
            "where params.object_id = (select id from object order by id desc\n" +
            "limit 1)";



    @Override
    public EventInfo mapRow(ResultSet resultSet, int i) throws SQLException {

        String eventDateOfCreation = resultSet.getString("event_date_of_creation");
        String eventName = resultSet.getString("event_name");
        String eventNameOfCreator = resultSet.getString("event_name_of_creator");
        //String eventLatitude = resultSet.getString("eventLatitude");
        //String eventLongitude = resultSet.getString("eventLongitude");

        return new EventInfo(eventDateOfCreation, eventName, eventNameOfCreator);
    }
}
