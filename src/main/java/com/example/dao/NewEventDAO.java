package com.example.dao;

import com.example.mapper.EventMapperNewDB;
import com.example.mapper.UserMapperNewDB;
import com.example.model.EventInfo;
import com.example.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
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

            return new EventInfo(eventName, eventLat, eventLng, eventDescription, date);
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

    public List<EventInfo> getEventsName() {
        String sql = EventMapperNewDB.SELECT_ALL_EVENTS;
        Object[] params = new Object[] {};
        EventMapperNewDB mapper = new EventMapperNewDB();
        return this.getJdbcTemplate().query(sql, params, mapper);
    }

}