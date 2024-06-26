package com.battleship.demo.store.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "GAMEMATCH")
public class GameMatch {

    @Id
    @Column(name="ID")
    private int id;

    @Column(name="PLAYER1ID")
    private int player1Id;

    @Column(name="PLAYER2ID")
    private int player2Id;

    @Column(name="WINNER1")
    private int winner1;

    // getters and setters
}