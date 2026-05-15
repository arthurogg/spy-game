package com.projet.spy_game.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.spy_game.model.Game;
import com.projet.spy_game.model.Round;

public interface RoundRepository extends JpaRepository<Round, Long>{
    Optional<Round> findTopByGameOrderByRoundNumberDesc(Game game);
}
