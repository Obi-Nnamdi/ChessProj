import java.util.ArrayList;
public class King extends Piece {
    private ArrayList<int[]> paths;
    private char color;
    private String type;
    public King(char color)
    {
        this.color = color;
        this.type = "king";
        paths = new ArrayList<int[]>();
        definePaths();
    }
    private void definePaths()
    {
    	//Based on the name, define the paths for the piece
    	//King
        paths.add(new int[]{1, 0});
        paths.add(new int[]{-1, 0});
        paths.add(new int[]{0, 1});
        paths.add(new int[]{0,-1});
        paths.add(new int[]{1, 1});
        paths.add(new int[]{-1,1});
        paths.add(new int[]{1,-1});
        paths.add(new int[]{-1,-1});
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
