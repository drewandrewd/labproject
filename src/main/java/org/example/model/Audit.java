package org.example.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Audit {
    private Long id;
    private String action;
    private String username;
    private LocalDateTime timestamp;

    public Audit() {
    }

    public Audit(String action, String username, LocalDateTime timestamp) {
        this.action = action;
        this.username = username;
        this.timestamp = timestamp;
    }

}