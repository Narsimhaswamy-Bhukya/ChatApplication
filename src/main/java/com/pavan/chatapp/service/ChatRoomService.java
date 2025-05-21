package com.pavan.chatapp.service;

import com.pavan.chatapp.model.ChatRoom;
import com.pavan.chatapp.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public Optional<String> getChatRoomId(
        String senderId,
        String receiverId,
        boolean createRoomIfNotExist
    ) {
        return chatRoomRepository
                .findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (createRoomIfNotExist) {
                        var newChatRoomId = createChatId(senderId, receiverId);
                        return Optional.of(newChatRoomId);
                    }

                    return Optional.empty();
                });
    }

    private String createChatId(String senderId, String receiverId) {
        var chatId = String.format("%s_%s", senderId, receiverId);

        ChatRoom senderReceiver = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        ChatRoom receiverSender = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(receiverId)
                .receiverId(senderId)
                .build();

        chatRoomRepository.save(senderReceiver);
        chatRoomRepository.save(receiverSender);

        return chatId;
    }
}
