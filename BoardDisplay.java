import javafx.application.Platform;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
/*
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
*/
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.canvas.*;
import javafx.scene.input.DragEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.util.ArrayList;
import javafx.scene.text.*;
import javafx.scene.control.Label;
import javafx.stage.WindowEvent;
import javafx.beans.property.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
public class BoardDisplay extends Application
{
	//Grid of Squares inbound
	int xPos = 0;
	int yPos = 0;
	private int fontSize;
	private int textYOffset;
	ArrayList<double[]> mice = new ArrayList<>();
	double[][] miceArr = new double[2][2];
	boolean hasClicked;
	private Board chessBoard;
	private Text[][] display;
	private boolean debugBoxOut;
	public BoardDisplay()
	{
		hasClicked = false;
		chessBoard = new Board();
		display = new Text[8][8];
		fontSize = 30;
		textYOffset = 48;
		debugBoxOut = false;
   	}

   	@Override
    	public void init()
    	{
   	}

   	@Override
    public void start(Stage primary)
    {
		StackPane root = new StackPane();
		Canvas canvas = new Canvas(600, 600);
		BorderPane bp = new BorderPane();
		bp.setCenter(canvas);
		Scene scene = new Scene(bp);
		primary.setScene(scene);
		GraphicsContext pen = canvas.getGraphicsContext2D();
		update(pen);
		Label turnNum = new Label(String.format("Turn Number: %s", chessBoard.getTurnNum()));
		Label whosTurn = new Label(chessBoard.getTurnNum() % 2 == 1 ? "White's Turn" : "Black's Turn");
		Label blackInCheck = new Label(String.format("Black in Check: %s", chessBoard.blackInCheck()));
		Label blackInCheckmate = new Label(String.format("Black in Checkmate: %s", chessBoard.blackInCheck()));
		Label whiteInCheck = new Label(String.format("White in Check: %s", chessBoard.whiteInCheck()));
		Label whiteInCheckmate = new Label(String.format("White in Checkmate: %s", chessBoard.whiteInCheckmate()));
		TextArea movesLog = new TextArea(String.format("Moves:\n %s", decodeMoves()));
		movesLog.setEditable(false);
		scene.setOnKeyPressed( e -> {
			if(e.getCode() == KeyCode.ESCAPE)
			{
				Platform.exit();
			}
			if (e.getCode() == KeyCode.I && !debugBoxOut)
			{
				Stage debugBox = new Stage();
				debugBoxOut = true;
                BorderPane border = new BorderPane();
                VBox box = new VBox();
				//IntegerProperty turnNumWatcher = IntegerProperty.integerProperty(chessBoard.getTurnNum());

                box.getChildren().addAll(turnNum, whosTurn, blackInCheck, blackInCheckmate, whiteInCheck, whiteInCheckmate, movesLog);
				//.addListener((observable, oldValue, newValue) -> {
				//	turnNum.setText("" + oldValue.intValue());
				//});
                border.setTop(box);
                debugBox.setScene(new Scene(border, 400, 290));
                debugBox.show();
				debugBox.setOnCloseRequest(new EventHandler<WindowEvent>()
				{
					public void handle(WindowEvent we)
					{
						debugBoxOut = false;
					}
				});

			}
		});
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event){
					if (!hasClicked)
					{
						miceArr[0] = (new double[] {event.getSceneY(), event.getSceneX()});
						int[] temp = new int[]{(int) (miceArr[0][0] / 75), (int) (miceArr[0][1] / 75)};
						updateSelection(pen, temp[0], temp[1], chessBoard.getBoard());
						hasClicked = true;
					}
					else
					{
						miceArr[1] = (new double[] {event.getSceneY(), event.getSceneX()});
						int[] temp = new int[]{(int) (miceArr[0][0] / 75), (int) (miceArr[0][1] / 75), (int)(miceArr[1][0] / 75), (int)(miceArr[1][1] / 75)};
						chessBoard.move(temp[0],temp[1],temp[2],temp[3]);
						promotionRunner(pen);
						update(pen);
						//updating labels
						turnNum.setText(String.format("Turn Number: %s", chessBoard.getTurnNum()));
						whosTurn.setText(chessBoard.getTurnNum() % 2 == 1 ? "White's Turn" : "Black's Turn");
						blackInCheck.setText(String.format("Black in Check: %s", chessBoard.blackInCheck()));
						blackInCheckmate.setText(String.format("Black in Checkmate: %s", chessBoard.blackInCheckmate()));
						whiteInCheck.setText(String.format("White in Check: %s", chessBoard.whiteInCheck()));
						whiteInCheckmate.setText(String.format("White in Checkmate: %s", chessBoard.whiteInCheckmate()));
						movesLog.setText(String.format("Moves:\n%s", decodeMoves()));
						movesLog.appendText("");
						//resetting the clicking mechanism
						hasClicked = false;
					}
				}
			});
		primary.show();
    }
	//make a menubar that'll show you the different variables that are at play
	public void update(GraphicsContext pen)
	{
		pen.setFill(Color.WHITE);
		pen.fillRect(0, 0, 600, 600);
		pen.setFill(Color.LIGHTBLUE);

		for(int j = 0; j < 9; j+=2)
		{
			for(int i = 0; i < 8; i += 2)
			{
				pen.fillRect((xPos+75) + i*75, yPos, 75, 75);
			}
			yPos += 75;
			for(int i = 0; i < 8; i += 2)
			{
				pen.fillRect(xPos + i * 75, yPos, 75, 75);
			}
			yPos += 75;
		}
		xPos = 0;
		yPos = 0;
		pen.setFill(Color.BLACK);
		pen.setTextAlign(TextAlignment.CENTER);
		pen.setFont(new Font(fontSize));
		for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
				Piece temp = chessBoard.getBoard()[i][j];
				if (temp != null)
				{
					String out = "";
					if (temp.getType().equals("knight"))
					{
						out = "N";
					}
					else if (temp.getType().equals("bpawn") || temp.getType().equals("wpawn"))
					{
						out = "P";
					}
					else
					{
						char[] tempArr = temp.getType().toCharArray();
						char tempChar = tempArr[0];
						out = (tempChar + "").toUpperCase();
					}
					if (temp.getColor() == 'W')
					{
						pen.setFill(Color.BLACK);
						pen.fillText(out,(j) * 75 + 37.5, (i) * 75 + (textYOffset));
					}
					else
					{
						pen.setFill(Color.BLACK);
						pen.fillRect(j * 75 + 10, i * 75 + 10, 55, 55);
						pen.setFill(Color.WHITE);
						pen.fillText(out,(j) * 75 + 37.5, (i) * 75 + (textYOffset));
					}
				}
					//add a rectangle that
            }
        }
		//add a seperate stage that displays the variables

	}
	public void updateSelection(GraphicsContext pen, int y, int x, Piece[][] boardArr)
	{
		pen.setFill(Color.WHITE);
		pen.fillRect(0, 0, 600, 600);
		pen.setFill(Color.LIGHTBLUE);

		for(int j = 0; j < 9; j+=2)
		{
			for(int i = 0; i < 8; i += 2)
			{
				pen.fillRect((xPos+75) + i*75, yPos, 75, 75);
			}
			yPos += 75;
			for(int i = 0; i < 8; i += 2)
			{
				pen.fillRect(xPos + i * 75, yPos, 75, 75);
			}
			yPos += 75;
		}
		xPos = 0;
		yPos = 0;
		pen.setFill(Color.YELLOW);
		if ((x) % 2 == 1 && (y) % 2 == 1)
			pen.setFill(Color.YELLOW);
		else if ((x) % 2 == 0 && (y) % 2 == 0)
			pen.setFill(Color.YELLOW);
		else if ((x) % 2 == 1 && (y) % 2 == 0)
			pen.setFill(Color.YELLOW.darker());
		else if ((x) % 2 == 0 && (y) % 2 == 1)
			pen.setFill(Color.YELLOW.darker());
		pen.fillRect((x) * 75, (y) * 75, 75, 75);
		for (int[] path : chessBoard.possiblePaths(y, x))
		{
			//pen.setFill(Color.BLACK);
			//pen.fillRect((x + path[0]) * 75, (y + path[1]) * 75, 75, 75);
			if ((x + path[0]) % 2 == 1 && (y + path[1]) % 2 == 1)
				pen.setFill(Color.YELLOW);
			else if ((x + path[0]) % 2 == 0 && (y + path[1]) % 2 == 0)
				pen.setFill(Color.YELLOW);
			else if ((x + path[0]) % 2 == 1 && (y + path[1]) % 2 == 0)
				pen.setFill(Color.YELLOW.darker());
			else if ((x + path[0]) % 2 == 0 && (y + path[1]) % 2 == 1)
				pen.setFill(Color.YELLOW.darker());
			pen.fillRect((x + path[0]) * 75, (y + path[1]) * 75, 75, 75);
		}
		pen.setFill(Color.BLACK);
		pen.setTextAlign(TextAlignment.CENTER);
		pen.setFont(new Font(fontSize));
		for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
				Piece temp = boardArr[i][j];
				if (temp != null)
				{
					String out = "";
					if (temp.getType().equals("knight"))
					{
						out = "N";
					}
					else if (temp.getType().equals("bpawn") || temp.getType().equals("wpawn"))
					{
						out = "P";
					}
					else
					{
						char[] tempArr = temp.getType().toCharArray();
						char tempChar = tempArr[0];
						out = (tempChar + "").toUpperCase();
					}
					if (temp.getColor() == 'W')
					{
						pen.setFill(Color.BLACK);
						pen.fillText(out,(j) * 75 + 37.5, (i) * 75 + (textYOffset));
					}
					else
					{
						pen.setFill(Color.BLACK);
						pen.fillRect(j * 75 + 10, i * 75 + 10, 55, 55);
						pen.setFill(Color.WHITE);
						pen.fillText(out,(j) * 75 + 37.5, (i) * 75 + (textYOffset));
					}
				}
					//add a rectangle that
            }
        }

	}
	public String decodeMoves()
	{
		String out = "";
		for (Object[] move: chessBoard.getMoves())
		{
			Piece newPiece = (Piece) move[0];
			int oldY = Integer.parseInt((String)move[1]);
			int oldX = Integer.parseInt((String)move[2]);
			int newY = Integer.parseInt((String)move[3]);
			int newX = Integer.parseInt((String)move[4]);
			Piece prevPiece = (Piece) move[5];
			int turn = Integer.parseInt((String)move[6]);
			//System.out.printf("oldX: %s, oldY: %s, newX: %s, newY: %s", oldX,oldY,newX,newY);
			if (prevPiece != null)
			{
				out += String.format("Turn %s: %s(%s) from %s%s to %s%s, captured %s(%s)\n",
				 turn, newPiece.getType(), newPiece.getColor(), (char)((oldX) + 65), (7- oldY) + 1, (char)((newX) + 65), (7 - newY) + 1, prevPiece.getType(), prevPiece.getColor());
			}
			else
				out += String.format("Turn %s: %s(%s) from %s%s to %s%s\n", turn, newPiece.getType(), newPiece.getColor(), (char)((oldX) + 65), (7 - oldY) + 1, (char)((newX) + 65), (7 - newY) + 1);
		}
		out +="--------------------------------------------------\n";
		for (Object[] move: chessBoard.getMoves())
		{
			Piece newPiece = (Piece) move[0];
			int oldY = Integer.parseInt((String)move[1]);
			int oldX = Integer.parseInt((String)move[2]);
			int newY = Integer.parseInt((String)move[3]);
			int newX = Integer.parseInt((String)move[4]);
			Piece prevPiece = (Piece) move[5];
			int turn = Integer.parseInt((String)move[6]);
			//System.out.printf("oldX: %s, oldY: %s, newX: %s, newY: %s", oldX,oldY,newX,newY);
			out += String.format("move(%s, %s, %s, %s);\n", oldY, oldX, newY, newX);
		}
		return out;
	}
	public void promotionRunner(GraphicsContext pen)
	{
		boolean promotionAvailiable = false;
		ArrayList<int[]> pawnsToPromote = new ArrayList<>();
		for(int i = 0; i < 8; i++)
		{
			if (chessBoard.getBoard()[0][i] != null && chessBoard.getBoard()[0][i].getType().equals("wpawn"))
				pawnsToPromote.add(new int[]{0, i});//has a white pawn, then promote with user input
			else if (chessBoard.getBoard()[7][i] != null && chessBoard.getBoard()[7][i].getType().equals("bpawn"))
			 	pawnsToPromote.add(new int[]{7, i});//has a black pawn, then promote with user input via a stage and buttons
		}
		for (int[] pawnPos : pawnsToPromote)
		{
			Stage promotionStage = new Stage();
	        BorderPane bp = new BorderPane();
	        HBox hbox = new HBox();
			VBox vbox = new VBox();
	        Label promoPrompt = new Label(String.format("What would you like to promote the pawn at %s%s to?", (char)(pawnPos[1] + 65), 8 - pawnPos[0]));
			String input = "";
			Button queenPromo = new Button("Queen");
	        queenPromo.setOnAction(e ->
			{
				chessBoard.promote(pawnPos[0],pawnPos[1], "queen");
				promotionStage.hide();
				update(pen);
			});
			Button knightPromo = new Button("Knight");
	        knightPromo.setOnAction(e ->
			{
				chessBoard.promote(pawnPos[0],pawnPos[1], "knight");
				promotionStage.hide();
				update(pen);
			});
			Button bishopPromo = new Button("Bishop");
	        bishopPromo.setOnAction(e ->
			{
				chessBoard.promote(pawnPos[0],pawnPos[1], "bishop");
				promotionStage.hide();
				update(pen);
			});
			Button rookPromo = new Button("Rook");
	        rookPromo.setOnAction(e ->
			{
				chessBoard.promote(pawnPos[0],pawnPos[1], "rook");
				promotionStage.hide();
				update(pen);
			});
			hbox.getChildren().addAll(promoPrompt);
	        vbox.getChildren().addAll(queenPromo, bishopPromo, rookPromo, knightPromo);
			bp.setTop(hbox);
	        bp.setCenter(vbox);
	        promotionStage.setScene(new Scene(bp, 300, 200));
	        promotionStage.show();
		}
	}
   	@Override
   	public void stop()
    {
    }
}
