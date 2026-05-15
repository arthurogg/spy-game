package com.projet.spy_game.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.spy_game.model.Game;
import com.projet.spy_game.model.GameStatus;

public interface GameRepository extends JpaRepository<Game,Long>{
    Optional<Game> findByCode(String code);
    List<Game> findByStatus(GameStatus status);
}
