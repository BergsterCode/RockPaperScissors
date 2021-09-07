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

    public ResponseEntity<String> joinGame(String name, UUID gameId){
        if(gamesMap.containsKey(gameId)){
            if(fetchGame(gameId).hasRoom()){
                gamesMap.get(gameId).addPlayer(name);
                return new ResponseEntity<String>(name + " has joined the game: " + gameId.toString(),
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
                    game.determineWinner();
                    return new ResponseEntity<String>(playerName +
                            " has made move. The game result is: " +
                            game.getWinner(), HttpStatus.OK);
                }
                return new ResponseEntity<String>(playerName + " has made move: "+ playerMove, HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<String>("No player with that name in game",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>("No active game exists with id: " + gameId.toString(),
                HttpStatus.NOT_FOUND);
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

        if(game.isActiveGame()){

        }
        return gamesMap.get(gameId);
    }



}
