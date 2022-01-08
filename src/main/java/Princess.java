import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyType;

public class Princess extends Components implements Characters{
    private Position position;
    private KeyType direction;

    public Princess(int x, int y) {
        super(x, y);
        this.position = getPosition();
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#171717"));
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "A");
    }

    public void move() {
        switch (direction) {
            case ArrowUp -> setPosition(position.moveUp());
            case ArrowDown -> setPosition(position.moveDown());
            case ArrowRight -> setPosition(position.moveRight());
            case ArrowLeft -> setPosition(position.moveLeft());
        }
    }

    public void setPrincessDirection(KeyType press){
        this.direction = press;
    }
}
