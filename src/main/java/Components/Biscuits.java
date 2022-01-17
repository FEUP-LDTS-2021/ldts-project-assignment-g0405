package Components;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Biscuits extends Components {
    private Position position;

    public Biscuits(int x, int y) {
        super(x, y);
        this.position = super.getPosition();
    }


    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#171717"));
        graphics.setForegroundColor(TextColor.Factory.fromString("#ffff00"));
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "c");
    }
}
