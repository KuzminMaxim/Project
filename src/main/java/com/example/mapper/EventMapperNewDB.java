package com.example.mapper;

import com.example.model.EventInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventMapperNewDB implements RowMapper<EventInfo> {

    public static final String SELECT_NAME_OF_EVENT_SQL = "select distinct params.value_text as name from params\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "join object on params.object_id = object.id\n" +
            "where attributes.Attribute = 'event_name'\n" +
            "and object.id = \n" +
            "(select distinct params.object_id from params where \n" +
            "value_text = ?\n" +
            "and params.attribute_id = (select distinct attributes.id from attributes\n" +
            "where attributes.Attribute = 'event_name'))";

    public static final String SELECT_NAME_OF_EVENT_CREATOR_SQL = "select distinct params.value_text as name from params\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "join object on params.object_id = object.id\n" +
            "where attributes.Attribute = 'event_name_of_creator'\n" +
            "and object.id = \n" +
            "(select distinct params.object_id from params where \n" +
            "value_text = ?\n" +
            "and params.attribute_id = (select distinct attributes.id from attributes\n" +
            "where attributes.Attribute = 'event_name_of_creator'))";

    public static final String SELECT_ALL_EVENTS = "select \n" +
            "event_name.value_text as event_name,\n" +
            "event_name_of_creator.value_text as event_name_of_creator,\n" +
            "event_description.value_text as event_description,\n" +
            "event_date_of_creation.value_date as event_date_of_creation,\n" +
            "event_lat.value_text as event_lat,\n" +
            "event_lng.value_text as event_lng\n" +
            "from \n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_name')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params)) as event_name,\n" +
            "(select distinct params.value_date, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_date_of_creation')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params)) as event_date_of_creation,\n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_name_of_creator')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params)) as event_name_of_creator, \n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_lat')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params))  as event_lat, \n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_lng')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params)) \n" +
            "as event_lng, \n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on params.attribute_id = attributes.id \n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_description')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params))\n" +
            "as event_description\n" +
            "where event_name.object_id = event_lat.object_id \n" +
            "and event_name.object_id = event_lng.object_id \n" +
            "and event_name.object_id = event_description.object_id\n" +
            "and event_name.object_id = event_date_of_creation.object_id\n" +
            "and event_name.object_id = event_name_of_creator.object_id;";

    /*public static final String SELECT_ALL_EVENT_MARKERS = "select distinct \n" +
            "event_name.value_text as event_name,\n" +
            "event_lat.value_text as event_lat,\n" +
            "event_lng.value_text as event_lng, \n" +
            "event_description.value_text as event_description\n" +
            "from \n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_name')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params)) as event_name, \n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_lat')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params))  as event_lat, \n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_lng')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params)) \n" +
            "as event_lng, \n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on params.attribute_id = attributes.id \n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_description')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params))\n" +
            "as event_description\n" +
            "where event_name.object_id = event_lat.object_id \n" +
            "and event_name.object_id = event_lng.object_id \n" +
            "and event_name.object_id = event_description.object_id ;";*/

    public static final String SELECT_ALL_EVENT_MARKERS = "select distinct \n" +
            "event_name.value_text as event_name,\n" +
            "event_lat.value_text as event_lat,\n" +
            "event_lng.value_text as event_lng, \n" +
            "event_description.value_text as event_description,\n" +
            "event_time.value_text as event_time,\n" +
            "event_name_of_creator.value_text as event_name_of_creator\n" +
            "from \n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_name')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params)) as event_name, \n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_lat')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params))  as event_lat, \n" +
            "(select distinct params.value_text, params.object_id \n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_lng')\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params))\n" +
            "as event_lng,\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on params.attribute_id = attributes.id \n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_description')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params))\n" +
            "as event_description,\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on params.attribute_id = attributes.id \n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_time')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params))\n" +
            "as event_time,\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on params.attribute_id = attributes.id \n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_name_of_creator')\n" +
            "and object.id IN \n" +
            "(select distinct params.object_id from params))\n" +
            "as event_name_of_creator\n" +
            "where event_name.object_id = event_lat.object_id \n" +
            "and event_name.object_id = event_lng.object_id \n" +
            "and event_name.object_id = event_description.object_id\n" +
            "and event_name.object_id = event_time.object_id\n" +
            "and event_name.object_id = event_name_of_creator.object_id;";

    /*public static final String SELECT_ALL_EVENT_DESCRIPTION = "select D.value_text as event_description\n" +
            "from params as D\n" +
            "where\n" +
            "D.value_text in (select distinct params.value_text \n" +
            "from params\n" +
            "join object on params.object_id IN (select object.id from object where object.name = 'OBJECT_EVENT')\n" +
            "join attributes on params.attribute_id = attributes.id \n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'event_description'));";
*/
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

    public static final String INSERT_DESCRIPTION_OF_EVENT_SQL = "insert into params(object_id, attribute_id, value_text)\n" +
            "values ((select id from object order by id desc\n" +
            "limit 1),\n" +
            "(select attributes.id from attributes where attributes.Attribute = 'event_description'), ?);";

    public static final String CREATE_EVENT_OBJECT_REFERENCES = "update params\n" +
            "Set params.object_references = (select id from object order by id desc\n" +
            "limit 1)\n" +
            "where params.object_id = (select id from object order by id desc\n" +
            "limit 1)";



    @Override
    public EventInfo mapRow(ResultSet resultSet, int i) throws SQLException {

        String eventDateOfCreation = resultSet.getString("event_date_of_creation");
        String nameOfEvent = resultSet.getString("event_name");
        String nameOfEventCreator = resultSet.getString("event_name_of_creator");
        String eventDescription = resultSet.getString("event_description");
        String eventLatitude = resultSet.getString("event_lat");
        String eventLongitude = resultSet.getString("event_lng");

        return new EventInfo(eventDateOfCreation, nameOfEvent, nameOfEventCreator,
                eventDescription, eventLatitude, eventLongitude);
    }
}
