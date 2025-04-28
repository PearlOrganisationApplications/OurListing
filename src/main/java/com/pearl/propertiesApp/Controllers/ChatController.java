package com.pearl.propertiesApp.Controllers;

import com.pearl.propertiesApp.DTOs.Message;
import com.pearl.propertiesApp.Repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UsersRepository userRepository;


    @MessageMapping("/chat")
    public Message sendMessage(@Payload Message chatMessage,
                               Principal principal) {

        String sender = principal.getName();

        // Set the sender in the message
        chatMessage.setSender(sender);
        chatMessage.setTimestamp(LocalDateTime.now());
        // Validate recipient exists
        userRepository.findByEmail(chatMessage.getRecipient())
                .orElseThrow(() -> new UsernameNotFoundException("Recipient not found"));

        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient(),
                "/queue/messages",
                chatMessage);
        return chatMessage;
    }

}
