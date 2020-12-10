import java.util.ArrayList;
public class BPawn extends Piece {
    private ArrayList<int[]> paths;
    private char color;
    private String type;
    public BPawn()
    {
        this.color = 'B';
        this.type = "bpawn";
        paths = new ArrayList<int[]>();
        definePaths();
    }
    private void definePaths()
    {
    	//Based on the name, define the paths for the piece
    	//BPawn
        //reg stuff
        paths.add(new int[] {0, 1});
        paths.add(new int[] {0, 2});
        //captures
        paths.add(new int[] {1,1});
        paths.add(new int[] {-1,1});
    }
    public ArrayList<int[]> getPaths()
    {

        return paths;
    }
    public char getColor()
    {
        return color;
    }
    
    public String getType()
    {
        return type;
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
