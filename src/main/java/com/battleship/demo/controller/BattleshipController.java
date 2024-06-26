package com.battleship.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Controller
@RequestMapping("/")
public class BattleshipController {

    /**
     * Renders Battleship game page with empty boards.
     *
     * @return the model and view for Battleship game page
     */
    @GetMapping
    public ModelAndView index() {
        return battleship();
    }

    @RequestMapping("/index")
    public ModelAndView battleship() {
        ModelAndView modelAndView = new ModelAndView("index");
        char[][] enemy = new char[10][10];
        char[][] board = new char[10][10];
        Arrays.stream(enemy).forEach(row -> Arrays.fill(row, ' '));
        modelAndView.addObject("enemy", enemy);
        Arrays.stream(board).forEach(row -> Arrays.fill(row, ' '));
        modelAndView.addObject("board", board);
        return modelAndView;
    }
}