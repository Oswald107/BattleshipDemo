package com.battleship.demo.store.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "USER")
public class User {

    @Id
    // @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name="USERNAME")
    private String username;

    @Column(name="WINS")
    private int wins;

    @Column(name="LOSSES")
    private int losses;

    // Default constructor
    public User() {
    }

    public User(String username) {
        this.username = username;
        wins = 0;
        losses = 0;
    }

    public String toString() {
        return username + "\t" + wins + "\t" + losses;
    }
    public void incWins() {
        wins++;
    }
    public void incLosses() {
        losses++;
    }
    // getters and setters
}