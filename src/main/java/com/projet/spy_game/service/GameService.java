package com.projet.spy_game.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.projet.spy_game.model.*;
import com.projet.spy_game.repository.*;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    public Game createGame() {

        String username = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();
        // récupération du user en ds
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // création de la partie
        Game game = new Game();
        game.setCode(generateCode());
        game.setStatus(GameStatus.WAITING);
        game.setHostUser(user);

        game = gameRepository.save(game);
        System.out.println("auth" + SecurityContextHolder.getContext().getAuthentication());

        // création du player qui devient le host de la partie
        Player player = new Player();
        player.setUser(user);
        player.setGame(game);
        player.setEliminated(false);

        playerRepository.save(player);

        return game;
    }

    // génération code partie
    private String generateCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }
}
