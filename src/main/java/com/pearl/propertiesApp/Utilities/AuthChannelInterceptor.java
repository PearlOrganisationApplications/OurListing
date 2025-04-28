package com.pearl.propertiesApp.Utilities;

import com.pearl.propertiesApp.Entities.Users;
import com.pearl.propertiesApp.Repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class AuthChannelInterceptor implements ChannelInterceptor {
    @Autowired
    private UsersRepository userRepository; // Your user repository

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String detect = accessor.getFirstNativeHeader("detect");
            if ("false".equals(detect)) {
                return message;
            }


            String token = accessor.getFirstNativeHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                throw new AuthenticationCredentialsNotFoundException("No token provided");
            }


            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                try {
                    // Validate the token
                    if (userRepository.existsByToken(token)) {

                        Users user = userRepository.findByToken(token)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                        Set<GrantedAuthority> authorities = new HashSet<>();
                        if (user.getRole() != null) {
                            authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
                        }

                        // Create authentication
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                user.getEmail(),
                                user.getPassword(),
                                authorities
                        );

                        accessor.setUser(authentication);
                        return message;
                    }
                } catch (Exception e) {
                    throw new AuthenticationCredentialsNotFoundException("Invalid token", e);
                }
            }

            throw new AuthenticationCredentialsNotFoundException("No token provided");
        }

        return message;
    }

}
