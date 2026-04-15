package com.projet.spy_game.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.projet.spy_game.model.Game;
import com.projet.spy_game.service.GameService;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/create")
    public Game createGame() {
        return gameService.createGame();
    }
}
