package com.example.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApprovalResponse {
    private String message;
    private boolean success;
    private LocalDateTime timeStamp;
}
