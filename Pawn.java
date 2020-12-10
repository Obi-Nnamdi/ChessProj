import java.util.ArrayList;
public class Pawn extends Piece {
    private ArrayList<int[]> paths;
    private char color;
    private String type;
    public Pawn(char color)
    {
        this.color = color;
        this.type = "pawn";
        paths = new ArrayList<int[]>();
        definePaths();
    }
    private void definePaths()
    {
    	//Based on the name, define the paths for the piece
    	//Pawn
        //reg stuff
        paths.add(new int[] {0, -1});
        paths.add(new int[] {0, -2});
        paths.add(new int[] {0, 1});
        paths.add(new int[] {0, 2});
        //captures
        paths.add(new int[] {1,1});
        paths.add(new int[] {-1,1});
        paths.add(new int[] {1,-1});
        paths.add(new int[] {-1,-1});
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
