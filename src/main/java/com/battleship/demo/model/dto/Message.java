package com.battleship.demo.model.dto;

public interface Message {
    String getType();
    String getGameId();
    String getContent();
}