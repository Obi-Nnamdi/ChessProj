import java.util.ArrayList;
public class Rook extends Piece {
    private ArrayList<int[]> paths;
    private char color;
    private String type;
    public Rook(char color)
    {
        this.color = color;
        this.type = "rook";
        paths = new ArrayList<int[]>();
        definePaths();
    }
    private void definePaths()
    {
    	//Based on the name, define the paths for the piece
    	//Rook
    	for (int x = 0; x < 8; x++)
        {
            paths.add(new int[]{x, 0});
            paths.add(new int[]{-x, 0});
        }
    	for (int y = 0; y < 8; y++)
        {
            paths.add(new int[]{0, y});
            paths.add(new int[]{0,-y});
        }
    }
    public ArrayList<int[]> getPaths()
    {

        return paths;
    }
    public String getType()
    {
        return type;
    }
    public char getColor()
    {
        return color;
    }
    public String toString()
    {
        String out = "";
        for (int[] path : paths)
        {
            out += String.format("[%s,%s]", path[0], path[1]);
        }
        return out;
    }
}
