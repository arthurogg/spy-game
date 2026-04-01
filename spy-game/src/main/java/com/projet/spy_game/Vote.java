package com.projet.spy_game;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Vote{
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime votedAt;
    @ManyToOne
    private Round round;
}