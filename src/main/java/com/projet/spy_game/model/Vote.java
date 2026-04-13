package com.projet.spy_game.model;

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
    @ManyToOne
    private Player playerVoting;
    @ManyToOne
    private Player targetPlayer;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDateTime getVotedAt() {
        return votedAt;
    }
    public void setVotedAt(LocalDateTime votedAt) {
        this.votedAt = votedAt;
    }
    public Round getRound() {
        return round;
    }
    public void setRound(Round round) {
        this.round = round;
    }
    public Player getPlayerVoting() {
        return playerVoting;
    }
    public void setPlayerVoting(Player playerVoting) {
        this.playerVoting = playerVoting;
    }
    public Player getTargetPlayer() {
        return targetPlayer;
    }
    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }
}