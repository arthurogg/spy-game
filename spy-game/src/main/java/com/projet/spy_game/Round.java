package com.projet.spy_game;

import java.util.Collection;

import jakarta.persistence.*;

@Entity
public class Round{
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int roundNumber;
    @Enumerated(EnumType.STRING)
    private RoundPhase phase;
    @ManyToOne
    private Game game;
    @ManyToOne
    private WordPair wordPair;
    @OneToMany(mappedBy = "round")
    private Collection<Vote> votes;
}