package com.g0405.game;

import com.googlecode.lanterna.input.KeyType;
import com.g0405.elements.components.characters.JackTheSparrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMoveJackTheSparrow {


    JackTheSparrow jack1;

    @BeforeEach
    public void jackConstructor(){
        jack1 = new JackTheSparrow(10,20);
    }


    @Test
    public void jackMove(){

        jack1.setJackDirection(KeyType.ArrowUp);
        jack1.move();
        assertEquals(19, jack1.getPosition().getY());

        jack1.setJackDirection(KeyType.ArrowDown);
        jack1.move();
        assertEquals(20, jack1.getPosition().getY());

        jack1.setJackDirection(KeyType.ArrowRight);
        jack1.move();
        assertEquals(11, jack1.getPosition().getX());

        jack1.setJackDirection(KeyType.ArrowLeft);
        jack1.move();
        assertEquals(10, jack1.getPosition().getX());

    }
}
