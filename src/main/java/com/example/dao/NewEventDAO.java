package com.example.dao;

import com.example.mapper.EventMapperNewDB;
import com.example.model.EventInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class NewEventDAO extends JdbcDaoSupport{

    @Autowired
    public NewEventDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public List<EventInfo> getAllEvents() throws NullPointerException{
        String sql = EventMapperNewDB.SELECT_ALL_EVENTS;
        Object[] params = new Object[] {};
        EventMapperNewDB mapper = new EventMapperNewDB();
        return this.getJdbcTemplate().query(sql, params, mapper);
    }

    public List<EventInfo> getAllEventMarkers() throws NullPointerException{
        String sql = EventMapperNewDB.SELECT_ALL_EVENT_MARKERS;
        Object[] params = new Object[] {};
        return this.getJdbcTemplate().query(sql, params, (resultSet, i) -> {
            String eventName = resultSet.getString("event_name");
            String eventLat = resultSet.getString("event_lat");
            String eventLng = resultSet.getString("event_lng");
            String eventDescription = resultSet.getString("event_description");
            String date = resultSet.getString("event_time");
            String eventNameOfCreator = resultSet.getString("event_name_of_creator");

            return new EventInfo(date, eventName, eventNameOfCreator, eventDescription, eventLat, eventLng);
        });
    }

    public EventInfo findName(String name) {
        String sql = EventMapperNewDB.SELECT_NAME_OF_EVENT_SQL;
        Object[] params = new Object[] {name};

        try {
            return this.getJdbcTemplate().queryForObject(sql, params, new RowMapper<EventInfo>() {
                @Override
                public EventInfo mapRow(ResultSet resultSet, int i) throws SQLException {
                    String name = resultSet.getString("name");
                    return new EventInfo(name);
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String findNameOfCreator(String creator) {
        String sql = EventMapperNewDB.SELECT_NAME_OF_EVENT_CREATOR_SQL;
        Object[] params = new Object[] {creator};

        try {
            return this.getJdbcTemplate().queryForObject(sql, params, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("name");
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<EventInfo> getEventsName() {
        String sql = EventMapperNewDB.SELECT_ALL_EVENTS;
        Object[] params = new Object[] {};
        EventMapperNewDB mapper = new EventMapperNewDB();
        return this.getJdbcTemplate().query(sql, params, mapper);
    }

    public List<EventInfo> findEventsWhereCreator(String name) throws NullPointerException{
        String sql = EventMapperNewDB.SELECT_EVENS_WHERE_CREATOR;
        Object[] params = new Object[] {name};
        return this.getJdbcTemplate().query(sql, params, (resultSet, i) -> {

            String eventDateOfCreation = resultSet.getString("event_date_of_creation");
            String nameOfEvent = resultSet.getString("event_name");
            String eventDescription = resultSet.getString("event_description");
            String date = resultSet.getString("event_time");


            return new EventInfo(eventDateOfCreation, nameOfEvent, eventDescription, date.replace("T", " "));
        });
    }

    public List<EventInfo> findEventsWhereParticipant(String name) throws NullPointerException{
        String sql = EventMapperNewDB.SELECT_EVENS_WHERE_PARTICIPANT;
        Object[] params = new Object[] {name};
        return this.getJdbcTemplate().query(sql, params, (resultSet, i) -> {

            String eventDateOfCreation = resultSet.getString("event_date_of_creation");
            String nameOfEvent = resultSet.getString("event_name");
            String eventDescription = resultSet.getString("event_description");
            String date = resultSet.getString("event_time");


            return new EventInfo(eventDateOfCreation, nameOfEvent, eventDescription, date.replace("T", " "));
        });
    }

    public List<EventInfo> findCancelledChats(String name) throws NullPointerException{
        String sql = EventMapperNewDB.SELECT_CANCELLED_CHATS;
        Object[] params = new Object[] {name};
        return this.getJdbcTemplate().query(sql, params, (resultSet, i) -> {

            String nameOfEvent = resultSet.getString("chat_name");

            return new EventInfo(nameOfEvent);
        });
    }

    public EventInfo findCountOfParticipants(String name) {
        String sql = EventMapperNewDB.FIND_COUNT_OF_PARTICIPANTS_FOR_THIS_EVENT;
        Object[] params = new Object[] {name};

        try {
            return this.getJdbcTemplate().queryForObject(sql, params, new RowMapper<EventInfo>() {
                @Override
                public EventInfo mapRow(ResultSet resultSet, int i) throws SQLException {
                    int name = resultSet.getInt("participants");
                    return new EventInfo(name);
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}