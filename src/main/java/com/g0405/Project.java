package com.g0405;

import com.g0405.game.Game;

import java.awt.*;
import java.io.IOException;

public class Project {
    public static void main(String[] args) {
        try {
            new Game(30, 20).run();
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }
}
