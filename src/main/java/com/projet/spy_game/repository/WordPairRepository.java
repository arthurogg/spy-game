package com.projet.spy_game.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.spy_game.model.WordPair;

public interface WordPairRepository extends JpaRepository<WordPair,Long>{
    
}
