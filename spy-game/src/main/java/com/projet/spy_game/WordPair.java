package com.projet.spy_game;

import java.util.Collection;

import jakarta.persistence.*;

@Entity
public class WordPair{
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String category;
    String civilianWord;
    String spyWord;
    @OneToMany(mappedBy = "wordPair")
    private Collection<Round> rounds;
}