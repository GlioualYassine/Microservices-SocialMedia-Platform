package org.example.chatserice2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.chatserice2.config.UserDetailsImpl;
import org.example.chatserice2.entity.ConversationEntity;
import org.example.chatserice2.entity.UserEntity;
import org.example.chatserice2.exception.EntityException;
import org.example.chatserice2.mapper.ChatMessageMapper;
import org.example.chatserice2.model.*;
import org.example.chatserice2.repository.ConversationRepository;
import org.example.chatserice2.repository.UserRepository;
import org.example.chatserice2.utils.SecurityUtils;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.example.chatserice2.utils.DbBoiii.getConvId;
@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationService {
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final ChatMessageMapper chatMessageMapper ;
    private final OnlineOfflineService onlineOfflineService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ConversationRepository conversationRepository;

    public List<UserConnection>getUserFriends(){
        UserDetailsImpl userDetails = securityUtils.getUser();
        String username = userDetails.getUsername();
        List<UserEntity> users = userRepository.findAll();
        UserEntity thisUser = users.stream()
                .filter(user->user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(EntityException::new);
        return users.stream()
                .filter(user->!user.getUsername().equals(username))
                .map(user->
                        UserConnection.builder()
                                .connectionId(user.getId())
                                .connectionUsername(user.getUsername())
                                .convId(getConvId(user,thisUser))
                                .unSeen(0)
                                .isOnline(onlineOfflineService.isUserOnline(user.getId()))
                                .build()
                ).toList();
    }

    public List<UnseenMessageCountResponse> getUnseenMessageCount(){
        List<UnseenMessageCountResponse> result = new ArrayList<>();
        UserDetailsImpl userDetails = securityUtils.getUser();
        List<ConversationEntity> unseenMessages =
                conversationRepository.findUnseenMessagesCount(userDetails.getId());

        if (!CollectionUtils.isEmpty(unseenMessages)){
            Map<UUID,List<ConversationEntity>> unseenMessageCountByUser = new HashMap<>();
            for (ConversationEntity entity : unseenMessages){
                List<ConversationEntity> values =
                        unseenMessageCountByUser.getOrDefault(entity.getFromUser(),new ArrayList<>());
                values.add(entity);
                unseenMessageCountByUser.put(entity.getFromUser(),values);
            }
            log.info("there are some unseen messages for {}", userDetails.getUsername());
            unseenMessageCountByUser.forEach(
                    (user,entities)->{
                        result.add(
                                UnseenMessageCountResponse
                                        .builder()
                                        .count((long) entities.size())
                                        .fromUser(user)
                                        .build());
                                updateMessageDelivery(user,entities, MessageDeliveryStatusEnum.DELIVERED);
                    }
            );
        }
        return  result ;
    }

    public List<ChatMessage> getUnseenMessages(UUID fromUserId){
        List<ChatMessage> result = new ArrayList<>();
        UserDetailsImpl userDetails = securityUtils.getUser();
        List<ConversationEntity> unseenMessages =
                conversationRepository.findUnseenMessages(userDetails.getId(),fromUserId);
        if (!CollectionUtils.isEmpty(unseenMessages)){
            log.info(
                    "there are some unseen messages for {} from {}", userDetails.getUsername(), fromUserId);
            updateMessageDelivery(fromUserId,unseenMessages,MessageDeliveryStatusEnum.SEEN);
            result = chatMessageMapper.toChatMessages(unseenMessages,userDetails,MessageType.UNSEEN);
        }
        return result;
    }

    private void updateMessageDelivery(
            UUID user ,
            List<ConversationEntity> entities,
            MessageDeliveryStatusEnum messageDeliveryStatusEnum
        ){
        entities.forEach(e->e.setDeliveryStatus(messageDeliveryStatusEnum.toString()));
        onlineOfflineService.notifySender(user,entities,messageDeliveryStatusEnum);
        conversationRepository.saveAll(entities);
    }

    public List<ChatMessage> setReadMessages(List<ChatMessage> chatMessages){
        List<UUID> inTransitMessageIds = chatMessages.stream()
                .map(ChatMessage::getId)
                .toList();
        List<ConversationEntity> conversationEntities =
                conversationRepository.findAllById(inTransitMessageIds);
        conversationEntities.forEach(
                message -> message.setDeliveryStatus(MessageDeliveryStatusEnum.SEEN.toString()));
        List<ConversationEntity> saved = conversationRepository.saveAll(conversationEntities);

        return chatMessageMapper.toChatMessages(saved,securityUtils.getUser(),MessageType.CHAT);

    }
}
