package com.projet.spy_game.model;

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
    @ManyToOne
    private Player receiver;
    @ManyToOne
    private Player sender;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public boolean isPrivate() {
        return isPrivate;
    }
    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
    public LocalDateTime getSentAt() {
        return sentAt;
    }
    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public Player getReceiver() {
        return receiver;
    }
    public void setReceiver(Player receiver) {
        this.receiver = receiver;
    }
    public Player getSender() {
        return sender;
    }
    public void setSender(Player sender) {
        this.sender = sender;
    }
}