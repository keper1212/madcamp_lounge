package com.example.madcamp_lounge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WebSocketErrorResponse {
    private String code;
    private String message;
}
