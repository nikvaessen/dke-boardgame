package nl.dke.boardgame.game.board;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;

import java.util.ArrayList;

/**
 * Created by nik and xavier on 24/01/17.
 */
public class AbBoard
        extends Board
{
    public int counter;
    public int[][] boardHistory;

    public AbBoard(int width, int height) throws IllegalArgumentException
    {
        super(width, height);
        counter = 0;
        boardHistory = new int[width * height][2];
    }

    public AbBoard(Board board) throws IllegalArgumentException
    {
        super(board.getWidth(), board.getHeight());
        counter = 0;
        boardHistory = new int[super.getWidth() * super.getHeight()][2];

        //make this board equal to the given board
        for(int i = 0; i < super.getHeight(); i++)
        {
            for(int j = 0; j < super.getWidth(); j++)
            {
                try
                {
                    TileState state = board.getState(i, j);
                    if(state != TileState.NEUTRAL)
                    {
                        this.claim(i, j, board.getState(i, j));
                    }
                }
                catch(AlreadyClaimedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<AbBoard> getAllPossibleBoardsAfter1Move(TileState t)
    {

        ArrayList<AbBoard> allPossibleBoards = new ArrayList<>();

        for(int i = 0; i < super.getHeight(); i++)
        {
            for(int j = 0; j < super.getWidth(); j++)
            {
                //if the tile is neutral, clone the board, but with this tile as claimed as player.claimsAs()
                if(this.getState(i, j) == TileState.NEUTRAL)
                {
                    AbBoard newBoard = this.clone();
                    try
                    {
                        newBoard.claim(i, j, t);
                        allPossibleBoards.add(newBoard);
                    }
                    catch(AlreadyClaimedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return allPossibleBoards;
    }

    public void printBoard()
    {
        String spaces = " ";
        for(int i = 0; i < super.getWidth(); i++)
        {
            System.out.print(spaces);
            for(int j = 0; j < super.getHeight(); j++)
            {

                if(this.getState(i, j) == TileState.NEUTRAL)
                {
                    System.out.print("- ");
                }
                else if(this.getState(i, j) == TileState.PLAYER1)
                {
                    System.out.print("R ");
                }
                else
                {
                    System.out.print("B ");
                }
            }
            spaces = spaces + " ";
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public int getSize()
    {
        return super.getHeight()* super.getWidth();
    }

    @Override
    public void claim(int row, int column, TileState state)
            throws IllegalArgumentException, AlreadyClaimedException
    {
        super.claim(row, column, state);
        boardHistory[counter][0] = row;
        boardHistory[counter][1] = column;
        counter++;
    }

    /**
     * Clones the board
     * @return an identical Board class with the same claimed tiles
     */
    @Override
    public AbBoard clone()
    {
        AbBoard clone = new AbBoard(super.getWidth(), super.getHeight());
        for(int i = 0; i < super.getHeight(); i++)
        {
            for(int j = 0; j < super.getWidth(); j++)
            {

                TileState state = super.getState(i, j);
                if(state != TileState.NEUTRAL)
                {
                    try
                    {
                        clone.claim(i, j, state);
                    }
                    catch (AlreadyClaimedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        clone.boardHistory = cloneHistory();
        clone.counter = counter;

        return clone;
    }

    public int[][] getHistory(){
        return boardHistory;
    }

    public int[][] cloneHistory(){
        int[][] clone = new int[boardHistory.length][boardHistory[0].length];
        for(int i = 0; i < boardHistory.length; i++){
            for(int j = 0; j < boardHistory[0].length; j++){
                clone[i][j] = boardHistory[i][j];
            }
        }
        return clone;
    }

}
