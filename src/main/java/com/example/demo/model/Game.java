package com.example.demo.model;

import java.util.*;
import java.util.stream.Collectors;

public class Game {

    private UUID gameId;
    private boolean activeGame = true;
    //LinkedHashMap to ensure the order of players.
    private LinkedHashMap<String,String> players = new LinkedHashMap<String,String>();
    private String winner;

    public Game(UUID gameId,
                boolean activeGame,
                LinkedHashMap<String, String> players,
                String winner) {
        this.gameId = gameId;
        this.activeGame = activeGame;
        this.players = players;
        this.winner = winner;
    }

    public Game(UUID gameId){
        this.gameId = gameId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public boolean isActiveGame() {
        return activeGame;
    }

    public void setActiveGame(boolean activeGame) {
        this.activeGame = activeGame;
    }

    public HashMap<String, String> getPlayerMove() {
        if (isActiveGame()){
             getActivePlayers();
        } else {
            return players;
        }
        return null;
    }

    public List<String> getActivePlayers(){
        List<String> playerList = new ArrayList<String>(players.keySet());
        return playerList;
    }

    public void addPlayer(String player){
        players.put(player,null);
    }

    public boolean hasRoom(){
       if(players.keySet().size() < 2){
           return true;
       }
       return false;
    }

    public boolean containsPlayer(String name){
        return players.containsKey(name);
    }

    public void setPlayerMove(String name, String move){
        players.replace(name,move);
    }


    public String getWinner(){
        return winner;
    }

    public void setWinner(String winner){
        this.winner = winner;
    }

    public boolean bothPlayerHasMoved(){
        if(players.containsValue(null)){
            return false;
        }
        return true;
    }

    public void determineWinner(){
        List<String> playerList = new ArrayList<String>(players.keySet());
        String player1 = playerList.get(0);
        String player2 = playerList.get(1);

        if(players.get(player1).equals(players.get(player2))){
            setWinner("Draw! " + player1 + " and " + player2 + " both chose: " + players.get(player1));
            setActiveGame(false);
        } else {
            String rules = "rockpaperscissorsrock";
            if(rules.contains(players.get(player1)+players.get(player2))){
                setWinner(player2 + " won the game!");
                setActiveGame(false);
            } else {
                setWinner(player1 + " won the game!");
                setActiveGame(false);
            }
        }
    }
}