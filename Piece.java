import java.util.ArrayList;
public abstract class Piece {
    private ArrayList<int[]> paths;
    private char color;
    private String type;
    abstract ArrayList<int[]> getPaths();
    abstract String getType();
    abstract char getColor();
}
