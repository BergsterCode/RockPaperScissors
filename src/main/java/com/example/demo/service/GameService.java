package com.example.demo.service;

import com.example.demo.model.Game;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class GameService {

    private HashMap<UUID, Game> gamesMap = new HashMap<>();

    public String newGame(String name){
        Game newGame = new Game(UUID.randomUUID());
        newGame.addPlayer(name);
        gamesMap.put(newGame.getGameId(),newGame);
        return newGame.getGameId().toString();
    }

    public ResponseEntity<String> startNewGame(Map<String, Object> requestBody){
        String playerName = requestBody.get("name").toString().toLowerCase();
        if(!validUsername(playerName)){
            return new ResponseEntity<String>("Missing username",HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<String>("Here is your game id: " + newGame(playerName),
                    HttpStatus.CREATED);
        }
    }

    public ResponseEntity<Game> getGameState(UUID gameId){
        if(doesGameExist(gameId)){
            return new ResponseEntity<Game>(fetchGame(gameId),HttpStatus.FOUND);
        }
        return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> joinGame(Map<String, Object> requestBody, UUID gameId){
        String playerName = requestBody.get("name").toString().toLowerCase();
        if(!validUsername(playerName)){
            return new ResponseEntity<String>("Missing username",HttpStatus.NOT_FOUND);
        } else if (gamesMap.containsKey(gameId)) {
            if(fetchGame(gameId).hasRoom()){
                gamesMap.get(gameId).addPlayer(playerName);
                return new ResponseEntity<String>(playerName + " has joined the game: " + gameId.toString(),
                        HttpStatus.OK);
            }
            return new ResponseEntity<String>("Game is full",HttpStatus.FOUND);
        }
        return new ResponseEntity<String>("No such game exists with id: " + gameId.toString(),
                HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> makeMove(String playerName, UUID gameId, String playerMove){
        Game game = gamesMap.get(gameId);
        if(gamesMap.containsKey(gameId) && game.isActiveGame()){
            if(game.containsPlayer(playerName)){
                game.setPlayerMove(playerName,playerMove);
                if(game.bothPlayerHasMoved()){
                    determineWinner(game);
                    return new ResponseEntity<String>(playerName + " has made move. The game result is: " + game.getWinner(), HttpStatus.OK);
                }
                return new ResponseEntity<String>(playerName + " has made move: "+ playerMove, HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<String>("No player with that name in game",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>("No active game exists with id: " + gameId.toString(),HttpStatus.NOT_FOUND);
    }

    public boolean doesGameExist(UUID gameId){
        if(gamesMap.containsKey(gameId)){
            return true;
        } else {
            return false;
        }
    }

    public Game fetchGame(UUID gameId){
        Game game = gamesMap.get(gameId);
        return gamesMap.get(gameId);
    }

    public boolean validUsername (String username){
        if(username.isEmpty() || username.isBlank()){
            return false;
        }else{
            return true;
        }
    }
    public void determineWinner(Game game){
        List<String> playerList = new ArrayList<String>(game.getPlayers().keySet());
        String player1 = playerList.get(0);
        String player2 = playerList.get(1);
        if(game.getPlayers().get(player1).equals(game.getPlayers().get(player2))){
            game.setWinner("Draw! " + player1 + " and " + player2 + " both chose: " + game.getPlayers().get(player1));
            game.setActiveGame(false);
        } else {
            String rules = "rockpaperscissorsrock";
            if(rules.contains(game.getPlayers().get(player1)+game.getPlayers().get(player2))){
                game.setWinner(player2 + " won the game!");
                game.setActiveGame(false);
            } else {
                game.setWinner(player1 + " won the game!");
                game.setActiveGame(false);
            }
        }
    }

}
