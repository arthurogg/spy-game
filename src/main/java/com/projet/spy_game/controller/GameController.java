package com.projet.spy_game.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.projet.spy_game.dto.GameDetails;
import com.projet.spy_game.dto.GameResponse;
import com.projet.spy_game.dto.GlobalResponse;
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
}
