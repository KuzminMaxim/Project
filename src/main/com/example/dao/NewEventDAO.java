package com.example.dao;

import com.example.model.EventModel;
import com.example.mapper.EventMapperNewDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Transactional
public class NewEventDAO extends JdbcDaoSupport{

    @Autowired
    public NewEventDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public List<EventModel> findCancelledChats(String name) throws NullPointerException{
        String sql = EventMapperNewDB.SELECT_CANCELLED_CHATS;
        Object[] params = new Object[] {name};
        assert this.getJdbcTemplate() != null;
        return this.getJdbcTemplate().query(sql, params, (resultSet, i) -> {

            String nameOfEvent = resultSet.getString("chat_name");
            String id = resultSet.getString("chat_id");

            return new EventModel(id, nameOfEvent);
        });
    }

    public EventModel findCountOfParticipants(String id) {
        String sql = EventMapperNewDB.FIND_COUNT_OF_PARTICIPANTS_FOR_THIS_EVENT;
        Object[] params = new Object[] {id};

        try {
            assert this.getJdbcTemplate() != null;
            return this.getJdbcTemplate().queryForObject(sql, params, (resultSet, i) -> {
                int countOfParticipant = resultSet.getInt("participants");
                return new EventModel(countOfParticipant);
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}