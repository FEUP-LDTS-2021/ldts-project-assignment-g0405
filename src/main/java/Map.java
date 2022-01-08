import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {
    private final int width;
    private final int height;

    JackTheSparrow jack;
    Princess princess;

    private List<Borders> borders;
    private List<Biscuits> biscuits;
    private List<Borders> prison;
    private List<Pirates> pirates;
    private Key key;
    private Exit exit;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;

        this.jack = new JackTheSparrow(width/2, height-2);
        this.princess = new Princess(width/2, 2);

        this.borders = createBorders();
        this.prison = createPrison();
        this.biscuits = createBiscuits();
        this.pirates = createPirates();
        this.key = createKey();
        this.exit = null;
    }

    public void draw(TextGraphics graphics) {
            graphics.setBackgroundColor(TextColor.Factory.fromString("#171717"));
            graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');

            jack.draw(graphics);
            princess.draw(graphics);

            for (Borders border : borders) border.draw(graphics);
            for (Biscuits biscuit : biscuits) biscuit.draw(graphics);
            for (Borders border : prison) border.draw(graphics);
            for (Pirates pirate : pirates) pirate.draw(graphics);
            if(key != null) key.draw(graphics);
    }

    private List<Borders> createBorders() {
        List<Borders> borders = new ArrayList<>();

        for (int c = 0; c < width; c++) {
            borders.add(new Borders(c, 0));
            borders.add(new Borders(c, height - 1));
        }

        for (int r = 1; r < height - 1; r++) {
            borders.add(new Borders(0, r));
            borders.add(new Borders(width - 1, r));
        }

        return borders;
    }

    private List<Biscuits> createBiscuits(){
        Random random = new Random();
        List<Biscuits> biscuits = new ArrayList<>();
        Biscuits biscuit;

        for (int i = 0; i < 5; i++) {
            biscuit = new Biscuits(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);
            if(checkPosition(biscuit, biscuits)){
                biscuits.add(biscuit);
            }
            else{
                i--;
            }
        }
        return biscuits;
    }

    private List<Borders> createPrison() {
        List<Borders> prison = new ArrayList<>();

        for (int c = princess.getPosition().getX()-2; c <= princess.getPosition().getX()+2; c++) {
            prison.add(new Borders(c, princess.getPosition().getY()+2));
        }

        for (int r = princess.getPosition().getY()-1; r <= princess.getPosition().getY()+1; r++) {
            prison.add(new Borders(princess.getPosition().getX()-2, r));
            prison.add(new Borders(princess.getPosition().getX()+2, r));
        }

        return prison;
    }

    private Key createKey(){
        Key k;
        do {
            Random random = new Random();
            k = new Key(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);
        } while (!checkPosition(k, biscuits));
        return k;
    }

    private List<Pirates> createPirates(){
        Random random = new Random();
        List<Pirates> pirates = new ArrayList<>();
        Pirates pirate;

        for (int i = 0; i < 8; i++) {
            pirate = new Pirates(random.nextInt(width - 2) + 1, random.nextInt(height - 3) + 1);

            if(checkPosition(pirate, biscuits)){
                for(Pirates p : pirates){
                    if(p.getPosition().equals(pirate.getPosition())){
                        i--;
                    }
                }
                pirates.add(pirate);
            }
            else{
                i--;
            }
        }
        return pirates;
    }

    //verifica se o objeto esta dentro da prisao ou se esta coincidente com as paredes da mesma
    private boolean checkPosition (Components component, List<Biscuits> biscuits){
        for (int i=-1; i<3; i++){
            if (comparePositions(component.getPosition(), princess.getPosition(), -2, i)) return false;
            if (comparePositions(component.getPosition(), princess.getPosition(), -1, i)) return false;
            if (comparePositions(component.getPosition(), princess.getPosition(), 0, i)) return false;
            if (comparePositions(component.getPosition(), princess.getPosition(), +1, i)) return false;
            if (comparePositions(component.getPosition(), princess.getPosition(), +2, i)) return false;
        }
        if(component.getPosition().equals(jack.getPosition())) return false;

        for(Biscuits biscuit : biscuits){
            if(component.getPosition().equals(biscuit.getPosition())) return false;
        }
        return true;
    }

    public void keyStrokes (KeyStroke press){
        princess.setJackPosition(jack.getPosition());
        jack.setJackDirection(press.getKeyType());
        moveJack(press.getKeyType());
    }

    private void moveJack(KeyType press){
        princess.setJackPosition(jack.getPosition());
        jack.move();
        if(jack.canJackMove(borders, prison)){
            switch (press) {
                case ArrowUp:
                    jack.setPosition(jack.getPosition().moveDown());
                    break;
                case ArrowDown:
                    jack.setPosition(jack.getPosition().moveUp());
                    break;
                case ArrowRight:
                    jack.setPosition(jack.getPosition().moveLeft());
                    break;
                case ArrowLeft:
                    jack.setPosition(jack.getPosition().moveRight());
                    break;
            }
        }

        eatBiscuits();
        if(this.key != null) collectKey();
        if(this.exit != null) movePrincess();
        this.openExit();
    }

    private void movePrincess(){
        System.out.println("hello");
        princess.move();
    }

    public boolean movePirate(){
        for (Pirates pirate : pirates){
            pirate.move();
            pirate.canPirateMove(pirate, width);
        }
        checkJackColision();
        return jack.checkIfDead();
    }

    private void eatBiscuits (){
        for (Biscuits biscuit: biscuits){
            if (jack.getPosition().equals(biscuit.getPosition())){
                biscuits.remove(biscuit);
                jack.setPoints();
                break;
            }
        }
    }

    private void checkJackColision (){
        for (Pirates pirate: pirates){
            if (jack.getPosition().equals(pirate.getPosition())){
                jack.setLives();
                System.out.println(jack.lives);
            }
        }
    }

    private void collectKey(){
        if (jack.getPosition().equals(key.getPosition())){
            key = null;
            for (Borders border: prison){
                if (comparePositions(border.getPosition(), princess.getPosition(), 0, 2)){
                    prison.remove(border);
                    break;
                }
            }
        }
    }

    private void openExit(){
        if(comparePositions(jack.getPosition(), princess.getPosition(), 0, 1)){
            for(Borders border: borders){
                if(border.getPosition().getX() == (width/2) && border.getPosition().getY() == height-1){
                    this.exit = new Exit(border.getPosition().getX(), border.getPosition().getY());
                    borders.remove(border);
                    break;
                }
            }
        }
    }

    private boolean comparePositions(Position pos1, Position pos2, int increX, int increY){
        int x = pos2.getX() + increX;
        int y = pos2.getY() + increY;
        Position posAux = new Position(x, y);
        return pos1.equals(posAux);
    }
}
