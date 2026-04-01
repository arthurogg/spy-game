package com.projet.spy_game;

import jakarta.persistence.*;

@Entity
public class Player{
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Enumerated(EnumType.STRING)
    PlayerRole role;
    String assignedWord;
    boolean eliminated;
    @ManyToOne
    Game game;
}