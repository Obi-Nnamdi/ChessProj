import java.util.ArrayList;
import java.util.Arrays;
public class Board
{
	private Piece[][] board;
	private int length;
	private int width;
    private int turnNum;
    private boolean bKingMoved;
    private boolean wKingMoved;
	private int wKingX;
	private int wKingY;
	private int bKingX;
	private int bKingY;
	private boolean lookingForCheck;
	private ArrayList<Object[]> moves;
	public Board()
    {
        length = 8;
        width = 8;
        turnNum = 1;
        bKingMoved = false;
        wKingMoved = false;
		bKingX = 4;
		bKingY = 0;
		wKingX = 4;
		wKingY = 7;
		lookingForCheck = false;
        board = new Piece [8][8];
		moves = new ArrayList<>();
        makeStandardBoard();
    }
    private void makeStandardBoard()
    {
        //black
        board[0][0] = new Rook('B');
        board[0][1] = new Knight('B');
        board[0][2] = new Bishop('B');
        board[0][3] = new Queen('B');
        board[0][4] = new King('B');
        board[0][5] = new Bishop('B');
        board[0][6] = new Knight('B');
        board[0][7] = new Rook('B');
        board[1][0] = new BPawn();
        board[1][1] = new BPawn();
        board[1][2] = new BPawn();
        board[1][3] = new BPawn();
        board[1][4] = new BPawn();
        board[1][5] = new BPawn();
        board[1][6] = new BPawn();
        board[1][7] = new BPawn();

        //white
        board[7][0] = new Rook('W');
        board[7][1] = new Knight('W');
        board[7][2] = new Bishop('W');
        board[7][3] = new Queen('W');
        board[7][4] = new King('W');
        board[7][5] = new Bishop('W');
        board[7][6] = new Knight('W');
        board[7][7] = new Rook('W');
        board[6][0] = new WPawn();
        board[6][1] = new WPawn();
        board[6][2] = new WPawn();
        board[6][3] = new WPawn();
        board[6][4] = new WPawn();
        board[6][5] = new WPawn();
        board[6][6] = new WPawn();
        board[6][7] = new WPawn();

    }
    //Create white and black sides
    //prob make a canMove method, and then the move method will actually move the piece
    public void move(int oldY, int oldX, int newY, int newX)
    {
        if (canMove(oldY, oldX, newY, newX))
        {
			Piece oldPiece = board[oldY][oldX];
			moves.add(new Object[]{board[oldY][oldX], oldY + "", oldX + "", newY + "", newX + "", board[newY][newX], turnNum + ""});//change this when castling is added
			board[newY][newX] = board[oldY][oldX];
			board[oldY][oldX] = null;
			Piece temp = board[newY][newX];
			if (temp.getType().equals("king"))
			{
				if(temp.getColor() == 'W')
				{
					wKingMoved = true;
					wKingX = newX;
					wKingY = newY;
				}
				else if(temp.getColor() == 'B')
				{
					bKingMoved = true;
					bKingX = newX;
					bKingY = newY;
				}
				//if the king tries to move somewhere that he'll be in check, he won't be allowed to castle
			}
			if(temp.getColor() == 'W' && whiteInCheck())
			{
				System.out.println("This move puts the white king in check!");
				//if doing this move puts the king in check, reverse it and quit
				if (moves.size() > 0)
				{
					Object[] tempArr = moves.remove(turnNum - 1); //removing the failed move
					board[oldY][oldX] = (Piece)tempArr[0];
					board[newY][newX] = (Piece) tempArr[5];
					if (((Piece)tempArr[0]).getType().equals("king"))
					{
						wKingX = oldX;
						wKingY = oldY;
					}
					return;
				}
			}
			else if(temp.getColor() == 'B' && blackInCheck())
			{
				System.out.println("This move puts the black king in check!");
				if (moves.size() > 0)
				{
					Object[] tempArr = moves.remove(turnNum -1); //removing the failed move
					board[oldY][oldX] = (Piece)tempArr[0];
					board[newY][newX] = (Piece) tempArr[5];
					if (((Piece)tempArr[0]).getType().equals("king"))
					{
						bKingX = oldX;
						bKingY = oldY;
					}
					return;
				}

			}
			System.out.println(toString());
			turnNum++;
        }
    }
	public boolean advancedCheckMove(int oldY, int oldX, int newY, int newX, boolean check)
    {
		lookingForCheck = check;
        if (canMove(oldY, oldX, newY, newX))
        {
			lookingForCheck = false;
			Piece oldPiece = board[newY][newX];
			board[newY][newX] = board[oldY][oldX];
			board[oldY][oldX] = null;

			Piece temp = board[newY][newX];
			if (temp.getType().equals("king"))
			{
				if(temp.getColor() == 'W')
				{
					wKingX = newX;
					wKingY = newY;
				}
				else if(temp.getColor() == 'B')
				{
					bKingX = newX;
					bKingY = newY;
				}
				//if the king tries to move somewhere that he'll be in check, he won't be allowed to castle
			}
			if(temp.getColor() == 'W' && whiteInCheck())
			{
				//if doing this move puts the king in check, reverse it and quit
				if (moves.size() > 0)
				{
					 //removing the move
					board[newY][newX] = oldPiece;
					board[oldY][oldX] = temp;
					if (temp.getType().equals("king"))
					{
						wKingX = oldX;
						wKingY = oldY;
					}
					return false;
				}
			}
			else if(temp.getColor() == 'B' && blackInCheck())
			{
				board[newY][newX] = oldPiece;
				board[oldY][oldX] = temp;
				if (temp.getType().equals("king"))
				{
					bKingX = oldX;
					bKingY = oldY;
				}
				return false;

			}
			if (temp.getColor() == 'B' && temp.getType().equals("king"))
			{
				bKingX = oldX;
				bKingY = oldY;
			}
			else if (temp.getColor() == 'W' && temp.getType().equals("king"))
			{
				wKingX = oldX;
				wKingY = oldY;
			}
			board[oldY][oldX] = temp;
			board[newY][newX] = oldPiece;
			return true;
        }
		lookingForCheck = false;
		return false;
    }
    public boolean canMove(int oldY, int oldX, int newY, int newX)
    {
    	//if newX and newY = oldX and oldY return false
        if (newX == oldX && newY == oldY)
            return false;
    	//if newX and newY are greater than 7 or less than 0, return false
        if (newX < 0 || newX > 7 || newY < 0 || newY > 7)
            return false;
		Piece temp = board[oldY][oldX];
		Piece newPiece = board[newY][newX];
        if (temp == null)
            return false;

		if ((turnNum % 2 == 0 && temp.getColor() == 'W' ) || (turnNum % 2 == 1 && temp.getColor() == 'B'))
		{
			if (!lookingForCheck)
				return false;
		}
		if (newPiece != null && newPiece.getColor() == temp.getColor())
			return false;
        int[] path = new int[]{newX - oldX, newY - oldY};
        //make sure it's a valid path
        if (isValidPath(temp, path, oldY))
		{
			//if the piece is either a black or white pawn, make sure to check that if a piece is in the way,
			//an actual capturing move is being performed and not a movement one
			if(temp.getType().equals("bpawn"))
			{
				if (Arrays.equals(path, new int[]{1, 1}) || Arrays.equals(path, new int[]{-1, 1}))
				{
					if(newPiece != null && newPiece.getColor() != temp.getColor())
						return true;
					else
						return false;
				}
				else if (Arrays.equals(path, new int[]{0,1}))
				{
					if(newPiece == null)
						return true;
					else
						return false;
				}
				else
					return true;
			}
			else if(temp.getType().equals("wpawn"))
			{
				if (Arrays.equals(path, new int[]{-1, -1}) || Arrays.equals(path, new int[]{1, -1}))
				{
					if(newPiece != null && newPiece.getColor() != temp.getColor())
						return true;
					else
						return false;
				}
				else if (Arrays.equals(path, new int[]{0,-1}))
				{
					if(newPiece == null)
						return true;
					else
						return false;
				}
				else
					return true;
			}
			else if (temp.getType().equals("knight"))
				return true;
			else if (temp.getType().equals("king"))
			{
				return true;
			}
				//add clause that prevents him from moving if he'd be in danger
			else //make sure that pieces can't jump over each other, so prevent them from sliding through other pieces
			{
				if (Math.abs(path[0]) == Math.abs(path[1]))
				{
				    if (path[1] > 0 && path[0] > 0)
				    {
				        for (int i = 1; i < Math.abs(path[0]); i++)
					    {
						    if (board[oldY + i][oldX + i] != null)
							    return false;
					    }
				    }
				    else if (path[1] > 0 && path[0] < 0)
				    {
				        for (int i = 1; i < Math.abs(path[0]); i++)
					    {
						    if (board[oldY + i][oldX - i] != null)
							    return false;
					    }
				    }
				    else if (path[1] < 0 && path[0] < 0)
				    {
				        for (int i = 1; i < Math.abs(path[0]); i++)
					    {
						    if (board[oldY - i][oldX - i] != null)
							    return false;
					    }
				    }
				    else
				    {
				        for (int i = 1; i < Math.abs(path[0]); i++)
					    {
						    if (board[oldY - i][oldX + i] != null)
							    return false;
					    }
				    }
					return true;
				}
				else if (path[0] != path[1] && path[0] == 0)
				{
				    if (path[1] > 0)
				    {
				        for (int i = 1; i < path[1]; i++)
				        {
				            if (board[oldY + i][oldX] != null)
								return false;
				        }
				    }
				    else
				    {
				        for (int i = -1; i > path[1]; i--)
				        {
				            if (board[oldY + i][oldX] != null)
								return false;
				        }
				    }
					return true;
				}
				else if (path[0] != path[1] && path[1] == 0)
				{
					if (path[0] > 0)
				    {
				        for (int i = 1; i < path[0]; i++)
				        {
				            if (board[oldY][oldX + i] != null)
							return false;
				        }
				    }
				    else
				    {
				        for (int i = -1; i > path[0]; i--)
				        {
				            if (board[oldY][oldX + i] != null)
							return false;
				        }
				    }
					return true;
				}
				else
					return false;
			}
		}
		else
			return false;
             //fix up later
            //do everything elses
    	//if there’s a piece at oldX then do the rest of the function, if not return false:
    	//if they’re on the opposite side of their board (black - 7, white - 0) - promote them
    	//promotion()

    	//king can’t move anywhere where he’d be in danger
    }
	public boolean inDanger(int row, int col)
	{
		lookingForCheck = true;
		for (int i = 0; i < length; i++)
        {
            for (int j = 0; j < width; j++)
            {
                if (board[i][j] != null)
                    if (canMove(i, j, row, col))
						return true;
            }
        }
		lookingForCheck = false;
		return false;
	}
	public boolean blackInCheck()
	{
		return inDanger(bKingY, bKingX);
	}
	public boolean blackInCheck(int kingY, int kingX)
	{
		return inDanger(kingY, kingX);
	}
	public boolean whiteInCheck(int kingY, int kingX)
	{
		return inDanger(kingY, kingX);
	}
	public boolean blackInCheckmate()
	{
		if(blackInCheck())
		{
			for (int i  = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++)
				{
					if (board[i][j] == null)
						continue;
					if (this.possiblePaths(i, j).size() > 0)
						return false;
						//since the move function defines additional conditions for being able to move,
						// as long as a piece is able to move the king isn't in checkmate
				}
			}
			return true;
		}
		return false;
	}
	public boolean whiteInCheck()
	{
		return inDanger(wKingY, wKingX);
	}
	public boolean whiteInCheckmate()
	{
		if(whiteInCheck())
		{
			for (int i  = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++)
				{
					if (board[i][j] == null)
						continue;
					if (this.possiblePaths(i, j).size() > 0)
						return false;
						//since the move function defines additional conditions for being able to move,
						// as long as a piece is able to move the king isn't in checkmate
				}
			}
			return true;
		}
		return false;
	}
    //does a proposed path work for this piece
    private boolean isValidPath(Piece piece, int[] wantedPath, int oldY)
    {
        if (piece.getType().equals("bpawn") && Arrays.equals(wantedPath, new int[]{0, 2}) && oldY != 1)
			return false;
		else if(piece.getType().equals("wpawn") && Arrays.equals(wantedPath, new int[]{0, -2}) && oldY != 6)
            return false;
        for (int[] path : piece.getPaths())
        {
            if (Arrays.equals(wantedPath, path))
                return true;
        }
        return false;
    }
	//an arrayList of all valid paths a piece can take
	public ArrayList<int[]> possiblePaths(int row, int col)
	{
		ArrayList<int[]> out = new ArrayList<>();
		if (board[row][col] != null)
		{
			for (int[] path : board[row][col].getPaths())
			{
				if (advancedCheckMove(row, col, row + path[1], col + path[0], false))
				{
					out.add(path);
				}

			}
		}
		return out;
	}
    public String toString()
    {
        String out = "";
        out += String.format("Turn Number: %s, bKingMoved: %s, wKingMoved: %s, wKingPos (row, column):[%s, %s], bKingPos[%s, %s], blackInCheck: %s, blackInCheckmate: %s, whiteInCheck: %s, whiteInCheckmate: %s \n\n",
		 turnNum, bKingMoved, wKingMoved, wKingY, wKingX, bKingY, bKingX, blackInCheck(), blackInCheckmate(), whiteInCheck(), whiteInCheckmate());
        for (Piece[] row : board)
        {
            for (Piece piece : row)
            {
                if (piece != null)
                    out += piece.getType() + piece.getColor() +  "  ";
                else
                    out += "N       ";
            }
            out += "\n";
        }
		out += "------------------------------------------------------------------\n";
        return out;
    }
	public int getTurnNum()
	{
		return turnNum;
	}

	public Piece[][] getBoard()
	{
		return board;
	}
	public ArrayList<Object[]> getMoves()
	{
		return moves;
	}
	public void promote(int row, int col, String type)
	{
		if (board[row][col] == null)
			return;
		// if (!(board[row][col].getType().equals("bpawn") || board[row][col].getType().equals("wpawn")))
		// 	return; don't really need it
		if (type.equals("queen"))
		{
			board[row][col] = new Queen(board[row][col].getColor());
		}
		else if (type.equals("knight"))
		{
			board[row][col] = new Knight(board[row][col].getColor());
		}
		else if (type.equals("bishop"))
		{
			board[row][col] = new Bishop(board[row][col].getColor());
		}
		else if (type.equals("rook"))
		{
			board[row][col] = new Rook(board[row][col].getColor());
		}
		else
			return;
	}
    public static void main(String args[])
    {
        Board test = new Board();
		//scholar's mate
        System.out.println(test);
		// test.move(6, 4, 4, 4); //format: (oldRow, oldColumn, newRow, newColumn)
		// test.move(1, 4, 3, 4);
		// test.move(7, 5, 4, 2);
		// test.move(0, 5, 3, 2);
		// test.move(7, 3, 3, 7);
		// test.move(0, 6, 2, 5);
		// test.move(3, 7, 1, 5);
		// test.move(1, 0, 2, 0);
// 		move(6, 3, 4, 3);
// move(1, 2, 3, 2);
// move(6, 4, 4, 4);
// move(0, 3, 3, 0);
		// test.move(6, 1, 4, 1);
		// test.move(1, 2, 3, 2);
		// test.move(4, 1, 3, 2);

    }
}
