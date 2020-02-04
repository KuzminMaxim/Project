package com.example.dao;

import com.example.mapper.EventMapperNewDB;
import com.example.model.EventModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Transactional
public class EventDAO extends JdbcDaoSupport{

    @Autowired
    public EventDAO(DataSource dataSource) {
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

    public List<EventModel> findCompletedChats(String name) throws NullPointerException{
        String sql = EventMapperNewDB.SELECT_COMPLETED_CHATS;
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
    public EventModel findCountOfNewMessagesAfterLogout(String chatId, String timeOfLogout) {
        String sql = EventMapperNewDB.FIND_COUNT_OF_NEW_MESSAGES_FOR_THIS_EVENT;
        Object[] params = new Object[] {chatId, timeOfLogout};

        try {
            assert this.getJdbcTemplate() != null;
            return this.getJdbcTemplate().queryForObject(sql, params, (resultSet, i) -> {
                String countOfNewMessages = resultSet.getString("messages");
                return new EventModel(countOfNewMessages);
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}