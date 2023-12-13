package background;

public class Background {
    public enum Color {
        RED, BLUE
    }

    public Color color;

    public Background(Color color) {
        this.color = color;
    }
}
