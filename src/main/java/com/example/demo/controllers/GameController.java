package com.example.demo.controllers;


import com.example.demo.model.Game;
import com.example.demo.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck(){return new ResponseEntity<>("OK im alive", HttpStatus.OK);}

    @PostMapping("/games")
    public ResponseEntity<String> startNewGame(@RequestBody Map<String, Object> requestBody){
        return gameService.startNewGame(requestBody);
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<Game> getGameStatus(@PathVariable UUID id){
        return gameService.getGameState(id);
    }

    @PostMapping("/games/{id}/join")
    public ResponseEntity<String> joinGame(@PathVariable UUID id,
                                           @RequestBody Map<String, Object> requestBody){
      return gameService.joinGame(requestBody,id);
    }

    //Could have taken out logic from this controller and put into service layer.
    @PostMapping("/games/{id}/move")
    public ResponseEntity<String> makeMove(@PathVariable UUID id, @RequestBody Map<String, Object> requestBody){
        String playerName = requestBody.get("name").toString().toLowerCase();
        String playerMove = requestBody.get("move").toString().toLowerCase();
        List<String> allowedMovesList = Arrays.asList("rock","scissors","paper");
        if(!allowedMovesList.contains(playerMove)){
            return new ResponseEntity<String>("Move: "+ playerMove +
                    ", is not a valid move (Allowed moves are: rock, paper, scissors)",
                    HttpStatus.NOT_ACCEPTABLE);
        } else if(!gameService.validUsername(playerName)){
            return new ResponseEntity<String>("Missing username",HttpStatus.NOT_FOUND);
        }
        return gameService.makeMove(playerName,id,playerMove);
    }


}
