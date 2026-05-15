package com.projet.spy_game.model;

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

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getRoundNumber() {
        return roundNumber;
    }
    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }
    public RoundPhase getPhase() {
        return phase;
    }
    public void setPhase(RoundPhase phase) {
        this.phase = phase;
    }
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public Collection<Vote> getVotes() {
        return votes;
    }
    public void setVotes(Collection<Vote> votes) {
        this.votes = votes;
    }
    public WordPair getWordPair() {
        return wordPair;
    }
    public void setWordPair(WordPair wordPair) {
        this.wordPair = wordPair;
    }
}