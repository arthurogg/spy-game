package com.projet.spy_game.dto;

import java.util.List;
import java.util.Map;

import com.projet.spy_game.model.RoundPhase;

public class GameStateDTO {

    private String gameCode;

    private RoundPhase phase;

    private List<String> alivePlayers;

    private Map<String,String> clues;

    // username -> nb votes reçus
    private Map<String,Integer> votes;

    private int roundNumber;

    // joueur éliminé au dernier vote
    private String eliminatedPlayer;

    // CIVILIANS / SPIES
    private String winner;

    public GameStateDTO(
            String gameCode,
            RoundPhase phase,
            List<String> alivePlayers,
            Map<String, String> clues,
            Map<String, Integer> votes,
            int roundNumber,
            String eliminatedPlayer,
            String winner
    ) {

        this.gameCode = gameCode;
        this.phase = phase;
        this.alivePlayers = alivePlayers;
        this.clues = clues;
        this.votes = votes;
        this.roundNumber = roundNumber;
        this.eliminatedPlayer = eliminatedPlayer;
        this.winner = winner;
    }

    public String getGameCode() {
        return gameCode;
    }

    public RoundPhase getPhase() {
        return phase;
    }

    public List<String> getAlivePlayers() {
        return alivePlayers;
    }

    public Map<String, String> getClues() {
        return clues;
    }

    public Map<String, Integer> getVotes() {
        return votes;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public String getEliminatedPlayer() {
        return eliminatedPlayer;
    }

    public String getWinner() {
        return winner;
    }
}