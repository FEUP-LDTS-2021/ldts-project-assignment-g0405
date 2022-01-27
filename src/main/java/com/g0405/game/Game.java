package com.g0405.game;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFrame;
import com.g0405.game.menu.Instructions;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import com.g0405.game.menu.Menu;

public class Game {
    private final int width;
    private final int height;
    private final TerminalScreen screen;
    public Map map;

    private final Menu menu;
    private int menuChoice;

    private final Instructions instruction;

    Timer timer;
    TimerTask moving;

    private boolean gameIsOver = false;

    class ControlPirates extends TimerTask {
        public void run()
        {
            //pirates only move if Jack is alive
            if(map.moveEnemies()) {
                try{
                    setGameIsOver();
                    endGame("GAME OVER!");
                    return;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                draw();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Game(int width, int height) throws IOException, FontFormatException {
        this.width = width;
        this.height = height;

        AWTTerminalFontConfiguration font = loadFont();
        Terminal terminal = createTerminal(width, height, font);

        this.screen = createScreen(terminal);
        map = new Map(width, height);

        timer = new Timer();
        moving = new ControlPirates();


        menu = new Menu(this);
        this.menuChoice = -1;

        instruction = new Instructions(this);
    }

    public TerminalScreen createScreen(Terminal terminal) throws IOException{
        TerminalScreen terminalScreen = new TerminalScreen(terminal);

        terminalScreen.setCursorPosition(null);   // we don't need a cursor
        terminalScreen.startScreen();             // screens must be started
        terminalScreen.doResizeIfNecessary();     // resize screen if necessary
        return terminalScreen;
    }

    public Terminal createTerminal(int width, int height, AWTTerminalFontConfiguration font) throws IOException{
        TerminalSize terminalSize = new TerminalSize(width, height+1);
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                .setInitialTerminalSize(terminalSize)
                .setTerminalEmulatorFontConfiguration(font)
                .setForceAWTOverSwing(true)
                .setTerminalEmulatorTitle("Cross the Map");

        Terminal terminal = terminalFactory.createTerminal();
        ((AWTTerminalFrame)terminal).setResizable(false);
        return terminal;
    }

    public void draw() throws IOException {
        screen.clear();
        map.draw(screen.newTextGraphics());
        screen.refresh();
    }

    public void run() throws IOException {
        setMenuChoice(menu.menuRun(screen));

        if (this.menuChoice == 0) {
            timer.scheduleAtFixedRate(moving, 0, 250);
            newGame();
        }
        else if(this.menuChoice == 1) {
            instructions();
        }
        else if(this.menuChoice == 2) {
            timer.cancel();
            timer.purge();
            screen.close();
        }
    }

    private void newGame() throws IOException {
        while (true) {
            draw();

            KeyStroke press = screen.readInput();

            if ((press.getKeyType() == KeyType.Character && press.getCharacter() == 'q') || press.getKeyType() == KeyType.EOF || gameIsOver) {
                timer.cancel();
                timer.purge();
                screen.close();
                break;
            }
            else if(map.checkJackOnExitDoor()){
                endGame("VICTORY!");
                break;
            }

            map.keyStrokes(press);
        }
    }

    private void endGame(String msg) throws IOException {
        timer.cancel();
        timer.purge();
        screen.clear();
        new EndGameMsg(this, msg).run(screen);
        screen.close();
    }

    private void instructions() throws IOException {
        int helperGuy = instruction.run(screen);

        if(helperGuy == -1){
            timer.cancel();
            timer.purge();
            screen.close();
        }
        else if(helperGuy == 1) run();
    }

    public AWTTerminalFontConfiguration loadFont() throws FontFormatException, IOException {
        File fontFile = new File("src/main/resources/Courier-changed3.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);

        Font loadedFont = font.deriveFont(Font.PLAIN, 25);
        return AWTTerminalFontConfiguration.newInstance(loadedFont);
    }

    public int getWidth(){ return this.width; }

    public int getHeight(){ return this.height; }

    private void setMenuChoice(int menuChoice){ this.menuChoice = menuChoice; }

    private void setGameIsOver(){ gameIsOver = true; }
}
