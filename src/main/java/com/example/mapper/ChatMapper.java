package com.example.mapper;

import com.example.model.ChatMessage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatMapper implements RowMapper<ChatMessage> {

    public static final String FIND_ALL_CONTENT_FOR_THIS_CHAT = "" +
            "select \n" +
            "message_chat_name.value_text as message_chat_name,\n" +
            "message_name_of_sender.value_text as message_name_of_sender,\n" +
            "message_content.value_text as message_content,\n" +
            "message_time_of_send.value_text as message_time_of_send\n" +
            "from\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'message_chat_name')\n" +
            "and params.value_text = ?\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params)) as message_chat_name,\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'message_name_of_sender')\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params))  as message_name_of_sender,\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'message_content')\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params))\n" +
            "as message_content,\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'message_time_of_send')\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params))\n" +
            "as message_time_of_send\n" +
            "where message_chat_name.object_id = message_name_of_sender.object_id\n" +
            "and message_chat_name.object_id = message_content.object_id \n" +
            "and message_chat_name.object_id = message_time_of_send.object_id;";

    public static final String FIND_ALL_PARTICIPANTS_FOR_THIS_EVENT = "" +
            "select event_participant.value_text as participants\n" +
            "from\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id = (select attributes.id from attributes where attributes.Attribute = 'chat_name')\n" +
            "and params.value_text = ?\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params)) as event_name,\n" +
            "(select distinct params.value_text, params.object_id\n" +
            "from params\n" +
            "join object on params.object_id = object.id\n" +
            "join attributes on attributes.id = params.attribute_id\n" +
            "where attributes.id IN (select attributes.id from attributes where attributes.Attribute IN ('chat_participant', 'chat_name_of_creator'))\n" +
            "and object.id IN\n" +
            "(select distinct params.object_id from params))  as event_participant\n" +
            "where event_name.object_id = event_participant.object_id";



    @Override
    public ChatMessage mapRow(ResultSet resultSet, int i) throws SQLException {

        String messageChatName = resultSet.getString("message_chat_name");
        String messageNameOfSender = resultSet.getString("message_name_of_sender");
        String messageContent = resultSet.getString("message_content");
        String currentDate = resultSet.getString("message_time_of_send");

        return new ChatMessage(messageChatName, messageNameOfSender, messageContent, currentDate);
    }
}
