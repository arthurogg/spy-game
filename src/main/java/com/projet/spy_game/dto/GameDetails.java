package com.projet.spy_game.dto;

import java.util.Collection;

import com.projet.spy_game.model.GameStatus;

public class GameDetails {
    public String hostname;
    public int numberOfPlayers;
    public GameStatus status;
    public Collection<String> players;
    public GameDetails(Collection<String> players, String hostname, int numberOfPlayers, GameStatus status) {
        this.hostname = hostname;
        this.numberOfPlayers = numberOfPlayers;
        this.status = status;
        this.players = players;
    }
    
}
