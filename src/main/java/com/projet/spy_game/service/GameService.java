package com.projet.spy_game.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.projet.spy_game.dto.GameDetails;
import com.projet.spy_game.dto.GameResponse;
import com.projet.spy_game.dto.GlobalResponse;
import com.projet.spy_game.exception.ApiException;
import com.projet.spy_game.model.*;
import com.projet.spy_game.repository.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final WordPairRepository wordPairRepository;
    private final RoundRepository roundRepository;

    public GameResponse createGame() {

        String username = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();
        // récupération du user en ds
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        // création de la partie
        Game game = new Game();
        game.setCode(generateCode());
        game.setStatus(GameStatus.WAITING);
        game.setHostUser(user);

        game = gameRepository.save(game);
        System.out.println("auth" + SecurityContextHolder.getContext().getAuthentication());

        // création du player qui devient le host de la partie
        Player player = new Player();
        player.setUser(user);
        player.setGame(game);
        player.setEliminated(false);

        playerRepository.save(player);

        return new GameResponse(game.getCode(),username);
    }

    // génération code partie
    private String generateCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }

    public GlobalResponse joinGame(String code){
        String username = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();
        // Récupération de la partie
        Game game = gameRepository.findByCode(code)
                              .orElseThrow(() -> new ApiException("Game not found", HttpStatus.NOT_FOUND));
        if(game.getStatus() != GameStatus.WAITING){
            throw new ApiException("Game already started",HttpStatus.UNAUTHORIZED);
        }
        // Récupération de l'utilisateur
        User user = userRepository.findByUsername(username)
                              .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        // Vérifier si déjà dans la partie
        boolean alreadyInGame = playerRepository
                .findByUserAndGame(user, game)
                .isPresent();

        if (alreadyInGame) {
            throw new ApiException("User is already in game",HttpStatus.UNAUTHORIZED);
        }
        // Création du joueur qui va rejoindre la partie
        Player player = new Player();
        player.setUser(user);
        player.setGame(game);
        player.setEliminated(false);
        playerRepository.save(player);

        return new GlobalResponse("Player joined successfully");
    }

    public GlobalResponse startGame(String code){

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        Game game = gameRepository.findByCode(code)
                .orElseThrow(() -> new ApiException("Game not found", HttpStatus.NOT_FOUND));

        // vérifier host
        if(!game.getHostUser().getId().equals(user.getId())){
            throw new ApiException("This user doesn't host the game", HttpStatus.UNAUTHORIZED);
        }

        if(game.getPlayers().size() < 3){
            throw new ApiException("The number of players is insufficient", HttpStatus.BAD_REQUEST);
        }

        if(game.getStatus() != GameStatus.WAITING){
            throw new ApiException("Game already started", HttpStatus.BAD_REQUEST);
        }

        // récupérer joueurs
        List<Player> players = new ArrayList<>(game.getPlayers());

        // shuffle
        Collections.shuffle(players);

        // récupérer un WordPair aléatoire
        List<WordPair> allPairs = wordPairRepository.findAll();

        if(allPairs.isEmpty()){
            throw new ApiException("No words available", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        WordPair pair = allPairs.get(new Random().nextInt(allPairs.size()));

        String civilianWord = pair.getCivilianWord();
        String spyWord = pair.getSpyWord();

        // nombre d'espions
        int numberOfSpies = (int) (players.size()*0.3);

        // assignation
        for(int i = 0; i < players.size(); i++){
            Player player = players.get(i);

            if(i < numberOfSpies){
                player.setRole(PlayerRole.SPY);
                player.setAssignedWord(spyWord);
            } else {
                player.setRole(PlayerRole.CIVILIAN);
                player.setAssignedWord(civilianWord);
            }

            playerRepository.save(player);
        }

        // créer le premier round
        Round round = new Round();
        round.setGame(game);
        round.setRoundNumber(1);
        round.setPhase(RoundPhase.WORD_DISTRIBUTION);

        roundRepository.save(round);


        // changer statut
        game.setWordPair(pair);
        game.setStatus(GameStatus.IN_PROGRESS);
        gameRepository.save(game);

        return new GlobalResponse("Game started successfully");
    }

    public GameDetails getGameDetails(String code){
        Game game = gameRepository.findByCode(code)
                    .orElseThrow(() -> new ApiException("Game not found", HttpStatus.NOT_FOUND));
        Collection<String> playersNames = new ArrayList<>();
        for(Player p : game.getPlayers()){
            playersNames.add(p.getUser().getUsername());
        }
        GameDetails details = new GameDetails(playersNames,game.getHostUser().getUsername(),game.getPlayers().size(),game.getStatus());
        return details;
    }   
}
