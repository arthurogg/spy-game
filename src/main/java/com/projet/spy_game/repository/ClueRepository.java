package com.projet.spy_game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.spy_game.model.Clue;
import com.projet.spy_game.model.Round;

public interface ClueRepository extends JpaRepository<Clue,Long> {
    List<Clue> findByRound(Round round);
}