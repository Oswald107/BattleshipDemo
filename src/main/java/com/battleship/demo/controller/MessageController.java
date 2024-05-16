package com.battleship.demo.controller;

import com.battleship.demo.manager.BattleshipManager;
import com.battleship.demo.model.dto.BattleshipMessage;
import com.battleship.demo.model.dto.JoinMessage;
import com.battleship.demo.model.dto.PlayerMessage;
import com.battleship.demo.model.Battleship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.battleship.demo.enumeration.GameState;


@Controller
public class MessageController {

    /**
     * Template for sending messages to clients through the message broker.
     */
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Manager for the Battleship games.
     */
    private final BattleshipManager BattleshipManager = new BattleshipManager();

    /**
     * Handles a request from a client to join a Battleship game.
     * If a game is available and the player is successfully added to the game,
     * the current state of the game is sent to all subscribers of the game's topic.
     *
     * @param message the message from the client containing the player's name
     * @return the current state of the game, or an error message if the player was unable to join
     */
    @MessageMapping("/game.join")
    @SendTo("/topic/game.state")
    public Object joinGame(@Payload JoinMessage message, SimpMessageHeaderAccessor headerAccessor) {
        Battleship game = BattleshipManager.joinGame(message.getPlayer());
        if (game == null) {
            BattleshipMessage errorMessage = new BattleshipMessage();
            errorMessage.setType("error");
            errorMessage.setContent("Failed to join game");
            return errorMessage;
        }

        String player = message.getPlayer();

        headerAccessor.getSessionAttributes().put("gameId", game.getGameId());
        headerAccessor.getSessionAttributes().put("player", player);

        BattleshipMessage gameMessage = gameToMessage(game, player);
        gameMessage.setType("game.joined");
        return gameMessage;
    }

    /**
     * Handles a request from a client to leave a Battleship game.
     * If the player is successfully removed from the game, a message is sent to subscribers
     * of the game's topic indicating that the player has left.
     *
     * @param message the message from the client containing the player's name
     */
    @MessageMapping("/game.leave")
    public void leaveGame(@Payload PlayerMessage message) {
        Battleship game = BattleshipManager.leaveGame(message.getPlayer());
        if (game != null) {
            BattleshipMessage gameMessage = gameToMessage(game, message.getPlayer());
            gameMessage.setType("game.left");
            messagingTemplate.convertAndSend("/topic/game." + game.getGameId(), gameMessage);
        }
    }

    /**
     * Handles a request from a client to make a move in a Battleship game.
     * If the move is valid, the game state is updated and sent to all subscribers of the game's topic.
     * If the game is over, a message is sent indicating the result of the game.
     *
     * @param message the message from the client containing the player's name, game ID, and move
     */
    @MessageMapping("/game.move")
    public void makeMove(@Payload BattleshipMessage message) {
        String player = message.getSender();
        String gameId = message.getGameId();
        int move = message.getMove();
        Battleship game = BattleshipManager.getGame(gameId);

        if (game == null || game.isGameOver()) {
            BattleshipMessage errorMessage = new BattleshipMessage();
            errorMessage.setType("error");
            errorMessage.setContent("Game not found or is already over.");
            this.messagingTemplate.convertAndSend("/topic/game." + gameId, errorMessage);
            return;
        }

        if (game.getGameState().equals(GameState.WAITING_FOR_PLAYER)) {
            BattleshipMessage errorMessage = new BattleshipMessage();
            errorMessage.setType("error");
            errorMessage.setContent("Game is waiting for another player to join.");
            this.messagingTemplate.convertAndSend("/topic/game." + gameId, errorMessage);
            return;
        }

        if (game.getTurn().equals(player)) {
            game.makeMove(player, move);

            BattleshipMessage gameStateMessage = new BattleshipMessage(game, game.getPlayer1());
            gameStateMessage.setType("game.move");
            this.messagingTemplate.convertAndSend("/topic/game." + gameId + "." + game.getPlayer1(), gameStateMessage);

            BattleshipMessage gameStateMessage2 = new BattleshipMessage(game, game.getPlayer2());
            gameStateMessage2.setType("game.move");
            this.messagingTemplate.convertAndSend("/topic/game." + gameId + "." + game.getPlayer2(), gameStateMessage2);          

            if (game.isGameOver()) {
                BattleshipMessage gameOverMessage = gameToMessage(game, game.getPlayer1());
                gameOverMessage.setType("game.gameOver");
                this.messagingTemplate.convertAndSend("/topic/game." + gameId, gameOverMessage);
                BattleshipManager.removeGame(gameId);
            }
        }
    }

    @EventListener
    public void SessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String gameId = headerAccessor.getSessionAttributes().get("gameId").toString();
        String player = headerAccessor.getSessionAttributes().get("player").toString();
        Battleship game = BattleshipManager.getGame(gameId);
        if (game != null) {
            if (game.getPlayer1().equals(player)) {
                game.setPlayer1(null);
                if (game.getPlayer2() != null) {
                    game.setGameState(GameState.PLAYER2_WON);
                    game.setWinner(game.getPlayer2());
                } else {
                    BattleshipManager.removeGame(gameId);
                }
            } else if (game.getPlayer2() != null && game.getPlayer2().equals(player)) {
                game.setPlayer2(null);
                if (game.getPlayer1() != null) {
                    game.setGameState(GameState.PLAYER1_WON);
                    game.setWinner(game.getPlayer1());
                } else {
                    BattleshipManager.removeGame(gameId);
                }
            }
            BattleshipMessage gameMessage = gameToMessage(game, game.getPlayer1());
            gameMessage.setType("game.gameOver");
            messagingTemplate.convertAndSend("/topic/game." + gameId + "." + game.getPlayer1(), gameMessage);

            BattleshipMessage gameMessage2 = gameToMessage(game, game.getPlayer2());
            gameMessage2.setType("game.gameOver");
            messagingTemplate.convertAndSend("/topic/game." + gameId + "." + game.getPlayer2(), gameMessage2);
            BattleshipManager.removeGame(gameId);
        }
    }

    private BattleshipMessage gameToMessage(Battleship game, String player) {
        BattleshipMessage message = new BattleshipMessage();
        message.setGameId(game.getGameId());
        message.setPlayer1(game.getPlayer1());
        message.setPlayer2(game.getPlayer2());
        message.setBoard(game.getBoard(player));
        message.setEnemyBoard(game.getEnemyBoard(player));
        message.setTurn(game.getTurn());
        message.setGameState(game.getGameState());
        message.setWinner(game.getWinner());
        message.setSender(player);
        return message;
    }
}