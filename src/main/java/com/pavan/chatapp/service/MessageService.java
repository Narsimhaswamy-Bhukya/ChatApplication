package com.pavan.chatapp.service;

import com.pavan.chatapp.model.Message;
import com.pavan.chatapp.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomService chatRoomService;

    public MessageService(MessageRepository messageRepository, ChatRoomService chatRoomService) {
        this.messageRepository = messageRepository;
        this.chatRoomService = chatRoomService;
    }

    public Message save(Message message) {
        var chatId = chatRoomService
                .getChatRoomId(message.getSenderId(), message.getReceiverId(), true)
                .orElseThrow(() -> new RuntimeException("Chat room could not be created or found"));
        message.setChatId(chatId);
        messageRepository.save(message);
        return message;
    }

    public List<Message> getChatMessage(String senderId, String receiverId) {
        var chatId = chatRoomService.getChatRoomId(senderId, receiverId, false);
        return chatId.map(messageRepository::findByChatId).orElse(new ArrayList<>());
    }
}
