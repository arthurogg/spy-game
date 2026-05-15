package com.projet.spy_game.dto;

public class GameLobbyDTO {

    private String code;

    private String host;

    private int players;

    public GameLobbyDTO(
            String code,
            String host,
            int players
    ) {

        this.code = code;
        this.host = host;
        this.players = players;
    }

    public String getCode() {
        return code;
    }

    public String getHost() {
        return host;
    }

    public int getPlayers() {
        return players;
    }
}
