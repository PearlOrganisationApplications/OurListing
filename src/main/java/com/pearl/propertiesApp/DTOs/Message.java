package com.pearl.propertiesApp.DTOs;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String sender;
    private String recipient;
    private String content;
    private LocalDateTime timestamp;
}
