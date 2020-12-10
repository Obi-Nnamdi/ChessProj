import java.util.ArrayList;
public class Bishop extends Piece {
    private ArrayList<int[]> paths;
    private char color;
    private String type;
    public Bishop(char color)
    {
        this.color = color;
        this.type = "bishop";
        paths = new ArrayList<int[]>();
        definePaths();
    }
    private void definePaths()
    {
    	//Based on the name, define the paths for the piece
    	//Bishop
        for(int k = 0; k < 8; k++)
        {
            paths.add(new int[] {k,k});
            paths.add(new int[] {-k,k});
            paths.add(new int[] {k,-k});
            paths.add(new int[] {-k,-k});
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
