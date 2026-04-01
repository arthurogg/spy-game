package com.projet.spy_game;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Message{
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    private boolean isPrivate;
    private LocalDateTime sentAt;
    @ManyToOne
    private Game game;
}