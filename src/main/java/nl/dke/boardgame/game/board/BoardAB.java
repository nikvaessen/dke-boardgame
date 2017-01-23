package nl.dke.boardgame.game.board;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;

/**
 * Created by Xavier on 23-1-2017.
 */
public class BoardAB extends Board{

    int counter = 0;
    int[][] boardHistory = new int[19*19][2];

    public BoardAB(int width, int height){
        super(width,height);
    }

    public void claim(int row, int column, TileState state)
            throws IllegalArgumentException, AlreadyClaimedException
    {
        if (!canAccess(row, column))
        {
            throw new IllegalArgumentException(String.format("row:%d,column:%d" +
                            " is out of bounds. Bounds are row:%d,column:%d",
                    row, column, board.length, board[row].length));
        }
        board[row][column].claim(state);
        boardHistory[counter][0] = row;
        boardHistory[counter][1] = column;
        counter++;
        notifyWatchers();
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
    public Board clone()
    {
        clone = new Board(width, height);
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                TileState state = getTile(i, j).getState();
                if(state != TileState.NEUTRAL)
                {
                    try
                    {
                        clone.getTile(i, j).claim(state);
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
}
