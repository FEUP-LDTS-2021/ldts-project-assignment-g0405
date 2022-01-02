import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.Random;


public class Pirates extends Components implements Characters{
    private int state;
    private Position position;

    public Pirates(int x, int y) {
        super(x, y);
        this.position = super.getPosition();
        this.state= new Random().nextInt(2);
    }

    public void draw(TextGraphics graphics) {
        graphics.setForegroundColor(TextColor.Factory.fromString("#B00000"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "P");
    }

    public void move() {
        switch (state) {
            case 0:
                setPosition(position.moveRight());
                break;

            case 1:
                setPosition(position.moveLeft());
                break;
        }
    }
}

