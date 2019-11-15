package com.example.dao;

import com.example.form.EventForm;
import com.example.form.RegistrationForm;
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
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class NewEventDAO extends JdbcDaoSupport{

    @Autowired
    public NewEventDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }


    public EventInfo createEvent(EventForm eventForm) throws NullPointerException{
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(EventMapperNewDB.CREATE_OBJECT_EVENT_SQL);
        getJdbcTemplate().update(EventMapperNewDB.INSERT_DATE_OF_CREATION_SQL);
        getJdbcTemplate().update(EventMapperNewDB.INSERT_NAME_OF_EVENT_SQL, new Object[]{eventForm.getNameOfEvent()});
        getJdbcTemplate().update(EventMapperNewDB.INSERT_NAME_OF_CREATOR_SQL, new Object[]{eventForm.getNameOfEventCreator()});
        getJdbcTemplate().update(EventMapperNewDB.INSERT_LATITUDE_OF_EVENT_SQL, new Object[]{eventForm.getLatitude()});
        getJdbcTemplate().update(EventMapperNewDB.INSERT_LONGITUDE_OF_EVENT_SQL, new Object[]{eventForm.getLongitude()});
        getJdbcTemplate().update(EventMapperNewDB.INSERT_DESCRIPTION_OF_EVENT_SQL, new Object[]{eventForm.getDescriptionOfEvent()});
        getJdbcTemplate().update(EventMapperNewDB.INSERT_DATE_OF_CREATION_SQL);
        getJdbcTemplate().update(EventMapperNewDB.CREATE_EVENT_OBJECT_REFERENCES);
        return new EventInfo();
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
            return new EventInfo(eventName, eventLat, eventLng, eventDescription);
        });
    }

}