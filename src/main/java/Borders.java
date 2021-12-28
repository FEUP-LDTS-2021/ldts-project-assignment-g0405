import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Borders extends Components implements Characters{
    private Position position;

    public Borders(int x, int y){
        super(x, y);
        this.position=super.getPosition();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#B33F40"));
        graphics.setForegroundColor(TextColor.Factory.fromString("#B33F40"));
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "I");
    }
}
