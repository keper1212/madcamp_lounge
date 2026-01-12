package com.example.madcamp_lounge.config;

import com.example.madcamp_lounge.dto.WebSocketErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {
    private final ObjectMapper objectMapper;

    public WebSocketErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(
        Message<byte[]> clientMessage,
        Throwable ex
    ) {
        Throwable root = getRootCause(ex);
        ErrorInfo errorInfo = toErrorInfo(root);
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setMessage(errorInfo.message);
        accessor.setNativeHeader("code", errorInfo.code);
        accessor.setNativeHeader("message", errorInfo.message);
        accessor.setLeaveMutable(true);

        byte[] payload = toPayload(errorInfo);
        MessageHeaders headers = accessor.getMessageHeaders();
        return MessageBuilder.createMessage(payload, headers);
    }

    private Throwable getRootCause(Throwable ex) {
        Throwable result = ex;
        while (result.getCause() != null && result.getCause() != result) {
            result = result.getCause();
        }
        return result;
    }

    private ErrorInfo toErrorInfo(Throwable ex) {
        if (ex instanceof IllegalArgumentException) {
            return new ErrorInfo("BAD_REQUEST", ex.getMessage());
        }
        if (ex instanceof IllegalStateException) {
            return new ErrorInfo("FORBIDDEN", ex.getMessage());
        }
        return new ErrorInfo("INTERNAL_ERROR", "internal error");
    }

    private byte[] toPayload(ErrorInfo errorInfo) {
        try {
            return objectMapper.writeValueAsBytes(
                new WebSocketErrorResponse(errorInfo.code, errorInfo.message)
            );
        } catch (JsonProcessingException jsonEx) {
            return ("{\"code\":\"" + errorInfo.code + "\",\"message\":\""
                + errorInfo.message + "\"}").getBytes();
        }
    }

    private static class ErrorInfo {
        private final String code;
        private final String message;

        private ErrorInfo(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
