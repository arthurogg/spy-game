package com.projet.spy_game.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.spy_game.model.Player;

public interface PlayerRepository extends JpaRepository<Player,Long>{
    Player save(Player player);
}
