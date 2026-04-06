package com.projet.spy_game;

import java.util.Collection;

import jakarta.persistence.*;

@Entity
public class WordPair{
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String category;
    String civilianWord;
    String spyWord;
    @OneToMany(mappedBy = "wordPair")
    private Collection<Round> rounds;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getCivilianWord() {
        return civilianWord;
    }
    public void setCivilianWord(String civilianWord) {
        this.civilianWord = civilianWord;
    }
    public String getSpyWord() {
        return spyWord;
    }
    public void setSpyWord(String spyWord) {
        this.spyWord = spyWord;
    }
    public Collection<Round> getRounds() {
        return rounds;
    }
    public void setRounds(Collection<Round> rounds) {
        this.rounds = rounds;
    }
}