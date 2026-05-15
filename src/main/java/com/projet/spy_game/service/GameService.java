package com.projet.spy_game.service;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.projet.spy_game.dto.ClueRequest;
import com.projet.spy_game.dto.GameDetails;
import com.projet.spy_game.dto.GameResponse;
import com.projet.spy_game.dto.GameStateDTO;
import com.projet.spy_game.dto.GlobalResponse;
import com.projet.spy_game.dto.MyRoleResponse;
import com.projet.spy_game.dto.VoteRequest;
import com.projet.spy_game.exception.ApiException;
import com.projet.spy_game.model.*;
import com.projet.spy_game.repository.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final WordPairRepository wordPairRepository;
    private final ClueRepository clueRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RoundRepository roundRepository;
    private final VoteRepository voteRepository;

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

    public GameDetails joinGame(String code){
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
        Collection<String> playersNames = new ArrayList<>();
        for(Player p : game.getPlayers()){
            playersNames.add(p.getUser().getUsername());
        }
        GameDetails details = new GameDetails(playersNames,game.getHostUser().getUsername(),game.getPlayers().size(),game.getStatus());

        return details;
    }

    public GameDetails startGame(String code){

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
        round.setPhase(RoundPhase.DESCRIPTION);

        roundRepository.save(round);


        // changer statut
        game.setWordPair(pair);
        game.setStatus(GameStatus.IN_PROGRESS);
        gameRepository.save(game);
        Collection<String> playersNames = new ArrayList<>();
        for(Player p : game.getPlayers()){
            playersNames.add(p.getUser().getUsername());
        }
        GameDetails details = new GameDetails(playersNames,game.getHostUser().getUsername(),game.getPlayers().size(),game.getStatus());

        return details;
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
 
    public MyRoleResponse getMyRole(String code) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // récupérer user connecté
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ApiException(
                                "User not found",
                                HttpStatus.NOT_FOUND
                        ));

        // récupérer game
        Game game = gameRepository.findByCode(code)
                .orElseThrow(() ->
                        new ApiException(
                                "Game not found",
                                HttpStatus.NOT_FOUND
                        ));

        // récupérer player
        Player player = playerRepository
                .findByUserAndGame(user, game)
                .orElseThrow(() ->
                        new ApiException(
                                "Player not in game",
                                HttpStatus.UNAUTHORIZED
                        ));

        return new MyRoleResponse(
                player.getRole().name(),
                player.getAssignedWord()
        );
    }
  

    public GlobalResponse sendClue(ClueRequest request) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ApiException(
                                "User not found",
                                HttpStatus.NOT_FOUND
                        ));

        // game
        Game game = gameRepository
                .findByCode(request.gameCode)
                .orElseThrow(() ->
                        new ApiException(
                                "Game not found",
                                HttpStatus.NOT_FOUND
                        ));

        // player
        Player player = playerRepository
                .findByUserAndGame(user, game)
                .orElseThrow(() ->
                        new ApiException(
                                "Player not found",
                                HttpStatus.NOT_FOUND
                        ));

        // round
        Round round = roundRepository
                .findByGame(game)
                .orElseThrow(() ->
                        new ApiException(
                                "Round not found",
                                HttpStatus.NOT_FOUND
                        ));

        // vérifier phase
        if(round.getPhase() != RoundPhase.DESCRIPTION && round.getPhase() != RoundPhase.WORD_DISTRIBUTION){
            throw new ApiException(
                    "Not description phase",
                    HttpStatus.BAD_REQUEST
            );
        }

        // vérifier éliminé
        if(player.isEliminated()){
            throw new ApiException(
                    "Player eliminated",
                    HttpStatus.BAD_REQUEST
            );
        }

        // empêcher double clue
        List<Clue> clues = clueRepository.findByRound(round);

        boolean alreadyPlayed = clues.stream()
                .anyMatch(c ->
                        c.getPlayer().getId().equals(player.getId())
                );

        if(alreadyPlayed){
            throw new ApiException(
                    "Player already sent clue",
                    HttpStatus.BAD_REQUEST
            );
        }

        // sauvegarder clue
        Clue clue = new Clue();

        clue.setContent(request.content);

        clue.setPlayer(player);

        clue.setRound(round);

        clueRepository.save(clue);

        // joueurs vivants
        List<Player> alivePlayers = game.getPlayers()
                .stream()
                .filter(p -> !p.isEliminated())
                .toList();

        // tous les joueurs ont joué ?
        if(clues.size() + 1 >= alivePlayers.size()){

            round.setPhase(RoundPhase.VOTING);

            roundRepository.save(round);
        }

        // construire état
        GameStateDTO gameState = buildGameState(game);
        System.out.println(gameState);
        // websocket update
        messagingTemplate.convertAndSend(
                "/topic/game/" + game.getCode() + "/state",
                gameState
        );

        return new GlobalResponse(
                "Clue sent successfully"
        );
    }
    // Méthode utilisée pour construire l'état de jeu et l'envoyer en websockets
    private GameStateDTO buildGameState(Game game){

        // round courant
        Round round = roundRepository
                .findByGame(game)
                .orElseThrow();

        // joueurs vivants
        List<Player> alivePlayersList = game.getPlayers()
                .stream()
                .filter(p -> !p.isEliminated())
                .toList();

        // =========================
        // CLUES
        // =========================

        List<Clue> clues = clueRepository.findByRound(round);

        Map<String,String> clueMap = new HashMap<>();

        for(Clue clue : clues){

            clueMap.put(
                    clue.getPlayer()
                            .getUser()
                            .getUsername(),

                    clue.getContent()
            );
        }

        // =========================
        // VOTES
        // =========================

        List<Vote> votes = voteRepository.findByRound(round);

        Map<String,Integer> voteMap = new HashMap<>();

        for(Vote vote : votes){

            String targetUsername = vote.getTargetPlayer()
                    .getUser()
                    .getUsername();

            voteMap.put(
                    targetUsername,
                    voteMap.getOrDefault(
                            targetUsername,
                            0
                    ) + 1
            );
        }

        String eliminatedPlayer = null;

        if(round.getPhase() == RoundPhase.RESULT){

            List<Player> eliminatedPlayers = game.getPlayers()
                    .stream()
                    .filter(Player::isEliminated)
                    .toList();

            if(!eliminatedPlayers.isEmpty()){

                Player lastEliminated = eliminatedPlayers
                        .get(eliminatedPlayers.size() - 1);

                eliminatedPlayer = lastEliminated
                        .getUser()
                        .getUsername();
            }
        }

        // WINNER
        String winner = null;

        long aliveSpies = alivePlayersList
                .stream()
                .filter(p ->
                        p.getRole() == PlayerRole.SPY
                )
                .count();

        long aliveCivilians = alivePlayersList
                .stream()
                .filter(p ->
                        p.getRole() == PlayerRole.CIVILIAN
                )
                .count();

        // civils gagnent
        if(aliveSpies == 0){

            winner = "CIVILIANS";
        }

        // espions gagnent
        else if(aliveSpies >= aliveCivilians){

            winner = "SPIES";
        }

        // DTO FINAL

        return new GameStateDTO(
                game.getCode(),
                round.getPhase(),
                alivePlayersList
                        .stream()
                        .map(p ->
                                p.getUser()
                                        .getUsername()
                        )
                        .toList(),
                clueMap,
                voteMap,
                round.getRoundNumber(),
                eliminatedPlayer,
                winner
        );
    }
    public GlobalResponse vote(VoteRequest request){

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // USER
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new ApiException(
                                "User not found",
                                HttpStatus.NOT_FOUND
                        ));

        // GAME
        Game game = gameRepository
                .findByCode(request.gameCode)
                .orElseThrow(() ->
                        new ApiException(
                                "Game not found",
                                HttpStatus.NOT_FOUND
                        ));

        // PLAYER
        Player voter = playerRepository
                .findByUserAndGame(user, game)
                .orElseThrow(() ->
                        new ApiException(
                                "Player not found",
                                HttpStatus.NOT_FOUND
                        ));

        // ROUND
        Round round = roundRepository
                .findByGame(game)
                .orElseThrow(() ->
                        new ApiException(
                                "Round not found",
                                HttpStatus.NOT_FOUND
                        ));

        // phase
        if(round.getPhase() != RoundPhase.VOTING){

            throw new ApiException(
                    "Not voting phase",
                    HttpStatus.BAD_REQUEST
            );
        }

        // éliminé
        if(voter.isEliminated()){

            throw new ApiException(
                    "Player eliminated",
                    HttpStatus.BAD_REQUEST
            );
        }

        // double vote
        boolean alreadyVoted = voteRepository
                .existsByPlayerVotingAndRound(
                        voter,
                        round
                );

        if(alreadyVoted){

            throw new ApiException(
                    "Player already voted",
                    HttpStatus.BAD_REQUEST
            );
        }

        // target
        Player target = playerRepository
                .findById(request.targetPlayerId)
                .orElseThrow(() ->
                        new ApiException(
                                "Target not found",
                                HttpStatus.NOT_FOUND
                        ));

        // target éliminé
        if(target.isEliminated()){

            throw new ApiException(
                    "Target already eliminated",
                    HttpStatus.BAD_REQUEST
            );
        }

        // save vote
        Vote vote = new Vote();

        vote.setPlayerVoting(voter);

        vote.setTargetPlayer(target);

        vote.setRound(round);

        voteRepository.save(vote);

        // tous les votes
        List<Vote> votes = voteRepository
                .findByRound(round);

        // joueurs vivants
        List<Player> alivePlayers = game.getPlayers()
                .stream()
                .filter(p -> !p.isEliminated())
                .toList();

        // tous les joueurs ont voté ?
        if(votes.size() >= alivePlayers.size()){

            eliminateMostVotedPlayer(
                    votes,
                    game
            );
        }

        // websocket state update
        GameStateDTO state = buildGameState(game);

        messagingTemplate.convertAndSend(
                "/topic/game/" + game.getCode() + "/state",
                state
        );

        return new GlobalResponse(
                "Vote sent successfully"
        );
    }

    private void eliminateMostVotedPlayer(List<Vote> votes,Game game){

        Map<Player,Integer> counts = new HashMap<>();

        // compter votes
        for(Vote vote : votes){

            Player target = vote.getTargetPlayer();

            counts.put(
                    target,
                    counts.getOrDefault(target,0) + 1
            );
        }

        // max voted
        Player eliminated = counts.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();

        // éliminer
        eliminated.setEliminated(true);

        playerRepository.save(eliminated);

        // vérifier victoire
        checkVictory(game);
    }
    private void checkVictory(Game game){

        List<Player> alivePlayers = game.getPlayers()
                .stream()
                .filter(p -> !p.isEliminated())
                .toList();

        long aliveSpies = alivePlayers
                .stream()
                .filter(p ->
                        p.getRole() == PlayerRole.SPY
                )
                .count();

        long aliveCivilians = alivePlayers
                .stream()
                .filter(p ->
                        p.getRole() == PlayerRole.CIVILIAN
                )
                .count();

        Round round = roundRepository
                .findByGame(game)
                .orElseThrow();

        // civils gagnent
        if(aliveSpies == 0){

            round.setPhase(RoundPhase.RESULT);

            roundRepository.save(round);

            return;
        }

        // espions gagnent
        if(aliveSpies >= aliveCivilians){

            round.setPhase(RoundPhase.RESULT);

            roundRepository.save(round);

            return;
        }

        // continuer partie
        round.setPhase(RoundPhase.DESCRIPTION);

        roundRepository.save(round);
    }
}
