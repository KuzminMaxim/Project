package com.example.dao;

import com.example.model.ChatModel;
import com.example.mapper.ChatMapper;
import com.example.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Transactional
public class ChatDAO extends JdbcDaoSupport {

    @Autowired
    public ChatDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public List<ChatMessage> findAllContentForThisChat(String chatId) throws NullPointerException{
        String sql = ChatMapper.FIND_ALL_CONTENT_FOR_THIS_CHAT;
        Object[] params = new Object[] {chatId};
        ChatMapper mapper = new ChatMapper();
        assert this.getJdbcTemplate() != null;
        return this.getJdbcTemplate().query(sql, params, mapper);
    }

    public List<ChatModel> findAllParticipants(String chatId) throws NullPointerException{
        String sql = ChatMapper.FIND_ALL_PARTICIPANTS_FOR_THIS_EVENT;
        Object[] params = new Object[] {chatId};
        assert this.getJdbcTemplate() != null;
        return this.getJdbcTemplate().query(sql, params, (resultSet, i) -> {

            String nameOfEvent = resultSet.getString("participants");

            return new ChatModel(nameOfEvent);
        });
    }
}
