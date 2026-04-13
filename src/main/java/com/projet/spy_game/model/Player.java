package com.projet.spy_game.model;

import java.util.Collection;

import jakarta.persistence.*;

@Entity
public class Player{
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PlayerRole role;
    private String assignedWord;
    private boolean eliminated;
    @ManyToOne
    private Game game;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "receiver")
    private Collection<Message> receivedMessages;
    @OneToMany(mappedBy = "sender")
    private Collection<Message> sentMessages;
    @OneToMany
    private Collection<Vote> votes;
    @OneToMany
    private Collection<Vote> votingTargets;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public PlayerRole getRole() {
        return role;
    }
    public void setRole(PlayerRole role) {
        this.role = role;
    }
    public String getAssignedWord() {
        return assignedWord;
    }
    public void setAssignedWord(String assignedWord) {
        this.assignedWord = assignedWord;
    }
    public boolean isEliminated() {
        return eliminated;
    }
    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Collection<Message> getReceivedMessages() {
        return receivedMessages;
    }
    public void setReceivedMessages(Collection<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }
    public Collection<Message> getSentMessages() {
        return sentMessages;
    }
    public void setSentMessages(Collection<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }
    public Collection<Vote> getVotes() {
        return votes;
    }
    public void setVotes(Collection<Vote> votes) {
        this.votes = votes;
    }
    public Collection<Vote> getVotingTargets() {
        return votingTargets;
    }
    public void setVotingTargets(Collection<Vote> votingTargets) {
        this.votingTargets = votingTargets;
    }
    
}