package com.projet.spy_game.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.spy_game.model.User;

public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByUsername(String username);

    @SuppressWarnings("unchecked")
    User save(User user);
}
