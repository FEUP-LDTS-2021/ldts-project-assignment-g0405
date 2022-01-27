package com.g0405.game.menu;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.g0405.game.Button;
import com.g0405.game.Game;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Menu {
    private final Game game;
    private final List<Button> buttons;
    private int selector;

    public Menu(Game game){
        this.game = game;


        this.buttons = Arrays.asList(new Button(game.getWidth(), "NEW GAME", "#ffff00", (game.getHeight()/2)-5),
                                     new Button(game.getWidth(), "INSTRUCTIONS", "#FFFFFF", game.getHeight()/2),
                                     new Button(game.getWidth(), "QUIT GAME", "#FFFFFF", (game.getHeight()/2)+5));


        this.selector = 0;
    }

    public void draw(TextGraphics graphics){
        graphics.setBackgroundColor(TextColor.Factory.fromString("#171717"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(game.getWidth(), game.getHeight()+1), ' ');
        for(Button button: buttons){
            button.draw(graphics);
        }
    }

    public int menuRun(TerminalScreen screen) throws IOException {
        int helper_guy = 0;
        do {
            screen.clear();
            draw(screen.newTextGraphics());
            screen.refresh();

            buttons.get(this.selector).changeColor("#ffffff");

            KeyStroke press = screen.readInput();
            switch (press.getKeyType()) {
                case ArrowUp:
                    if (this.selector != 0) setSelector(-1);
                    break;
                case ArrowDown:
                    if (this.selector != 2) setSelector(1);
                    break;
                case Enter:
                    helper_guy = -1;
                    break;
                case EOF:
                    screen.close();
                    return 2;
            }

            buttons.get(this.selector).changeColor("#ffff00");
        } while (helper_guy != -1);

        return this.selector;
    }

    private void setSelector(int incre){ this.selector += incre; }
}
