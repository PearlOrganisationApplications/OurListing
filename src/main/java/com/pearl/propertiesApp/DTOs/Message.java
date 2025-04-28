package com.pearl.propertiesApp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String sender;
    private String recipient;
    private String content;
    private LocalDateTime timestamp;
}
