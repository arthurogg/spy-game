package com.projet.spy_game.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.projet.spy_game.dto.ClueRequest;
import com.projet.spy_game.dto.GameDetails;
import com.projet.spy_game.dto.GameLobbyDTO;
import com.projet.spy_game.dto.GameResponse;
import com.projet.spy_game.dto.GlobalResponse;
import com.projet.spy_game.dto.MyRoleResponse;
import com.projet.spy_game.dto.VoteRequest;
import com.projet.spy_game.model.Game;
import com.projet.spy_game.service.GameService;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameResponse> createGame() {
        return ResponseEntity.ok(gameService.createGame());
    }
    @PostMapping("/join")
    public ResponseEntity<GameDetails> joinGame(@RequestParam String code){
        return ResponseEntity.ok(gameService.joinGame(code));
    }
    @PostMapping("/start")
    public ResponseEntity<GameDetails> startGame(@RequestParam String code){
        return ResponseEntity.ok(gameService.startGame(code));
    }
    @GetMapping("get")
    public ResponseEntity<GameDetails> getGame(@RequestParam String code){
        return ResponseEntity.ok(gameService.getGameDetails(code));
    }

    @GetMapping("/my-role")
    public ResponseEntity<MyRoleResponse> getMyRole(@RequestParam String code) {

        return ResponseEntity.ok(
                gameService.getMyRole(code)
        );
    }

    @PostMapping("/send-clue")
    public ResponseEntity<GlobalResponse> sendClue(@RequestBody ClueRequest request){
        return ResponseEntity.ok(
                gameService.sendClue(request)
        );
    }

    @PostMapping("/vote")
    public ResponseEntity<GlobalResponse> vote(@RequestBody VoteRequest request){

        return ResponseEntity.ok(
                gameService.vote(request)
        );
    }

    @GetMapping("/waiting")
    public ResponseEntity<List<GameLobbyDTO>>
    getWaitingGames(){

        return ResponseEntity.ok(
                gameService.getWaitingGames()
        );
    }
}
