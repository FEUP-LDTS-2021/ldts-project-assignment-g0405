package com.g0405.game.menu;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.g0405.elements.Position;
import com.g0405.game.Button;
import com.g0405.game.Game;

import java.io.IOException;

public class Instructions {
    private final Button button;
    private final Game game;
    private final String text;

    public Instructions(Game game){
        this.game = game;
        this.text = "INSTRUCTIONS";

        button = new Button(game.getWidth(), "GO BACK", "#ffff00", 18);
    }

    public void draw(TextGraphics graphics){
        drawText(new Position(game.getWidth()/2-text.length()/2, 1),text, graphics);

        drawText(new Position(2,4),"1.MOVE WITH ARROWS", graphics);
        drawText(new Position(2,6), "2.ESCAPE PIRATES", graphics);
        drawText(new Position(2,8),"3.CATCH BISCUITS", graphics);
        drawText(new Position(2,10),"4.COLLECT KEY", graphics);
        drawText(new Position(2,12),"5.SAVE PRINCESS", graphics);
        drawText(new Position(2,14), "6.EXIT THE MAP TO WIN", graphics);
        drawText(new Position(2,16),"7.HAVE FUN ", graphics);

        button.draw(graphics);
    }

    private void drawText(Position position, String text, TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#000000"));
        drawText1(graphics, position, text);
    }


    private void drawText1(TextGraphics textGraphics, Position position, String text) {
        textGraphics.setForegroundColor(TextColor.Factory.fromString("#FFFFFF"));
        textGraphics.enableModifiers(SGR.BOLD);
        textGraphics.putString(position.getX(),position.getY(),text);
    }

    public int run(TerminalScreen screen) throws IOException {
        int helper_guy = 0;
        int res = 0;

        screen.clear();
        draw(screen.newTextGraphics());
        screen.refresh();

        do {
            KeyStroke press = screen.readInput();
            switch (press.getKeyType()) {
                case Enter:
                    res = 1;
                    helper_guy = -1;
                    break;
                case EOF:
                    res = -1;
                    helper_guy = -1;
                    break;
            }
        } while (helper_guy != -1);

        return res;
    }
}
