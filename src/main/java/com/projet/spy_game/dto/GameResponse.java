package com.projet.spy_game.dto;

public class GameResponse {
    public String code;
    public String hostUserName;
    public GameResponse(String code, String hostUserName) {
        this.code = code;
        this.hostUserName = hostUserName;
    }
}
