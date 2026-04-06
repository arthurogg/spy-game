package com.projet.spy_game;

import java.util.Collection;

import jakarta.persistence.*;

@Entity
public class Game{
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String code;
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    @OneToMany(mappedBy = "game")
    private Collection<Player> players;
    @OneToMany(mappedBy = "game")
    private Collection<Message> messages;
    @OneToMany(mappedBy = "game")
    private Collection<Round> rounds;
    @ManyToOne
    private User hostUser;
    public Long getId() {
        return id;
    }
    public Collection<Message> getMessages() {
        return messages;
    }
    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }
    public Collection<Round> getRounds() {
        return rounds;
    }
    public void setRounds(Collection<Round> rounds) {
        this.rounds = rounds;
    }
    public User getHostUser() {
        return hostUser;
    }
    public void setHostUser(User hostUser) {
        this.hostUser = hostUser;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public GameStatus getStatus() {
        return status;
    }
    public void setStatus(GameStatus status) {
        this.status = status;
    }
    public Collection<Player> getPlayers() {
        return players;
    }
    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }
}