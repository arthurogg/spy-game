package com.projet.spy_game.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.spy_game.model.Game;

public interface GameRepository extends JpaRepository<Game,Long>{
    Optional<Game> findByCode(String code);
}
