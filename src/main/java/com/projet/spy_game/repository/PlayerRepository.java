package com.projet.spy_game.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.spy_game.model.Game;
import com.projet.spy_game.model.Player;
import com.projet.spy_game.model.User;

public interface PlayerRepository extends JpaRepository<Player,Long>{
    Player save(Player player);
    Optional<Player> findByUserAndGame(User user, Game game);
}
