package com.projet.spy_game.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.spy_game.model.Round;

public interface RoundRepository extends JpaRepository<Round, Long>{
    
}
