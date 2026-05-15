package com.projet.spy_game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.spy_game.model.Player;
import com.projet.spy_game.model.Round;
import com.projet.spy_game.model.Vote;

public interface VoteRepository extends JpaRepository<Vote,Long> {

    List<Vote> findByRound(Round round);

    boolean existsByPlayerVotingAndRound(
            Player playerVoting,
            Round round
    );
}
