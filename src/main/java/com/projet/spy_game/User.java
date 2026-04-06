package com.projet.spy_game;

import java.util.Collection;

import jakarta.persistence.*;

@Entity
public class User{
    public Collection<Game> getHostedGames() {
        return hostedGames;
    }
    public void setHostedGames(Collection<Game> hostedGames) {
        this.hostedGames = hostedGames;
    }
    public Collection<Player> getPlayers() {
        return players;
    }
    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String username;
    String password;
    @OneToMany(mappedBy = "hostUser")
    private Collection<Game> hostedGames;
    @OneToMany(mappedBy = "user")
    private Collection<Player> players;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}