package nl.dke.boardgame.game.board;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.util.Watchable;
import nl.dke.boardgame.util.Watcher;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a GameBoard of the boardgame Hex
 * <p>
 * This board consists of rows hexagonal tiles. Each hexagon tile lies against
 * the bottom right side of the tile above it.
 * <p>
 * Currently it's only tested for 11 x 11, which should? be correctly initialised
 *
 * @author josevelasquez on 9/12/16.
 * @author Nik
 */
public class Board
        implements Watchable
{
    int counter = 0;
    int[][] boardHistory = new int[19*19][2];
    /**
     * The 2D array of Tiles which together make the game board
     */
    public HexTile[][] board;

    /**
     * List of watchers to notify when the board changes
     */
    private List<Watcher> watchers;

    /**
     * The width of the board
     */
    public int width;

    /**
     * The height of the board
     */
    public int height;

    /**
     * Constructs the Hex game board
     *
     * @param width  the width of the board
     * @param height the height of the board
     */
    public Board(int width, int height)
        throws IllegalArgumentException
    {
        if(width <= 0 || height <= 0)
        {
            throw new IllegalArgumentException("A board cannot have a width " +
                    "and height smaller than or equal to 0");
        }
        this.width = width;
        this.height = height;
        this.watchers = new ArrayList<Watcher>();
        initBoard(width, height);
    }
    public ArrayList<Board> getAllPossibleBoardsAfter1Move(TileState t){

        ArrayList<Board> allPossibleBoards = new ArrayList<Board>();

        for(int i = 0; i < height; i ++){
            for(int j = 0; j < width; j ++){
                //if the tile is neutral, clone the board, but with this tile as claimed as player.claimsAs()
                if(this.getState(i,j) == TileState.NEUTRAL){

                    Board newBoard = this.clone(true);
                    try
                    {
                        newBoard.claim(i,j,t);
                        allPossibleBoards.add(newBoard);
                    }
                    catch (AlreadyClaimedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return allPossibleBoards;
    }
    public void printBoard(){
        String spaces = " ";
        for(int i = 0; i < width; i++){
            System.out.print(spaces);
            for(int j = 0; j < height; j++){

                if(this.getState(i,j) == TileState.NEUTRAL){
                    System.out.print("- ");
                }else if(this.getState(i,j) == TileState.PLAYER1){
                    System.out.print("R ");
                }else{
                    System.out.print("B ");
                }
            }
            spaces = spaces + " ";
            System.out.print("\n");
        }System.out.print("\n");
    }
    /**
     * Return the width of the board, or the amount of columns in each row
     * @return the width of the board as an integer
     */
    public int getWidth()
    {
    	return width;
    }

    /**
     * returns the height of the board, or the total amount of rows
     * @return the height of the board, as an integer
     */
    public int getHeight() 
    {
    	return height;
    }

    public int getSize()
    {
        return height*width;
    }

    /**
     * Creates and fills a board of hextiles, and the connections from each tile
     * @param width the width of the board to create
     * @param height the height of the board to create
     */
    private void initBoard(int width, int height)
    {
        //create the 2d array
        board = new HexTile[height][width];

        //populate it with HexTiles
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                board[i][j] = new HexTile(i, j);
            }
        }

        //Give each HexTile the correct neighbour
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                try
                {
                    HexTile tile = board[i][j];

                    // first off, the left and right tile on the same row
                    // are always neighbours
                    if (j - 1 >= 0)
                    {
                        // left neighbour is side 4
                        tile.addNeighbors(4, getTile(i, j - 1));
                    }
                    if (j + 1 < height)
                    {
                        // right neighbour is side 1
                        tile.addNeighbors(1, getTile(i, j + 1));
                    }
                    // Note: the rules for ABOVE and BELOW are dependent on
                    // the rotation of the whole board
                    //
                    // for the row ABOVE, the neighbours are always in the same
                    // column and one column TO THE RIGHT
                    if (i - 1 >= 0)
                    {
                        //up "left"(same column) neighbour is side 5
                        tile.addNeighbors(5, getTile(i - 1, j));
                        //up "right" column (to the right) neighbour is side 0
                        if (j + 1 < width)
                        {
                            tile.addNeighbors(0, getTile(i - 1, j + 1));
                        }
                    }
                    // for the row BELOW, the neighbours are always in the same
                    // column and one column TO THE LEFT
                    if (i + 1 < height)
                    {
                        //down "left"(to the left) neighbour is side 3
                        if (j - 1 >= 0)
                        {
                            tile.addNeighbors(3, getTile(i + 1, j - 1));
                        }
                        //down "right"(same column) is side 2
                        tile.addNeighbors(2, getTile(i + 1, j));
                    }
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get the tile of the specified location in the board
     *
     * @param row the row of the Tile, starting at the top
     * @param column the column of the Tile, starting at the left side
     * @return the state of the HexTile at the given position
     * @throws IllegalArgumentException when the given location is not valid
     */
    public HexTile getTile(int row, int column)
            throws IllegalArgumentException
    {
        if (!canAccess(row, column))
        {
            throw new IllegalArgumentException(String.format("row:%d,column:%d" +
                            " is out of bounds. Bounds are row:%d,column:%d",
                    row, column, board.length, board[row].length));
        }
        return board[row][column];
    }

    /**
     * A string with where each tile is one line, along with all it's neighbours
     *
     * @return the String of all tiles of the board
     */
    public String toString()
    {
        String st = "";

        for (HexTile[] tiles : board)
        {
            for (HexTile t : tiles)
            {
                st += t.toString() + ": " + t.printNeighbors() + " \n";
            }
        }
        return st;
    }

    /**
     * Get the state of the specified location in the board
     *
     * @param row the row of the Tile, starting at the top
     * @param column the column of the Tile, starting at the left side
     * @return the state of the HexTile at the given position
     * @throws IllegalArgumentException when the given location is not valid
     */
    public TileState getState(int row, int column)
            throws IllegalArgumentException
    {
        if (!canAccess(row, column))
        {
            throw new IllegalArgumentException(String.format("row:%d,column:%d" +
                            " is out of bounds. Bounds are row:%d,column:%d",
                    row, column, board.length, board[row].length));
        }
        return board[row][column].getState();
    }

    /**
     * gets all the coordinates(row and column) of the neighbours of the specified tile.
     * If there is none, the values will be -1 instead
     * @param row the row of the tile whose neighbours are requested
     * @param column the column of the tile which neighbours are requested
     * @return an array of length 6 with array of size 2 for every element,
     * where the first element is the row and
     * the second the column
     */
    public List<HexTile> getNeighbours(int row, int column)
        throws IllegalArgumentException
    {
        return getTile(row, column).getNeighbours();
    }

    /**
     * claim the given location in the board for a player
     *
     * @param row the row of the Tile, starting at the top
     * @param column the column of the Tile, starting at the left side
     * @throws IllegalArgumentException when the given location is not valid
     */
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

    /**
     * claim the given location for the opponent player.
     * @param row the row to switch
     * @param column the column to switch
     */
    public void overwrite(int row, int column)
    {
        if(!canAccess(row, column))
        {
            throw new IllegalArgumentException(String.format("row:%d,column:%d" +
                            " is out of bounds. Bounds are row:%d,column:%d",
                    row, column, board.length, board[row].length));
        }
        HexTile tile = board[row][column];
        TileState owner = tile.getState();
        tile.reset();
        try
        {
            if(owner == TileState.PLAYER1)
            {
                tile.claim(TileState.PLAYER2);
            }
            else
            {
                tile.claim(TileState.PLAYER1);
            }
        }
        catch (AlreadyClaimedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether the given row and column are inside the dimensions of the
     * board
     * @param row the row to check
     * @param column the column to check
     * @return true if both are in the dimensions, false otherwise
     */
    public boolean canAccess(int row, int column)
    {
        return canAccessRow(row) && canAccessColumn(column);
    }

    /**
     * Checks whether the given row number is in the span of the rows
     * @param row the row to check
     * @return true is the row exists, false otherwise
     */
    public boolean canAccessRow(int row)
    {
        return row > -1 && row < board.length;
    }

    /**
     * Checks whether the given column number is in the span of the columns
     * @param column the column to check
     * @return true if the column exists, false otherwise
     */
    public boolean canAccessColumn(int column)
    {
        return column > -1 && column < board[0].length;
    }

    /**
     * Loops over all tiles and sets the states to NEUTRAL
     */
    public void resetTiles()
    {
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[i].length; j++)
            {
                board[i][j].reset();
            }
        }
        notifyWatchers();
    }

    /**
     * Clones the board
     * @return an identical Board class with the same claimed tiles
     */
    Board clone;
    public Board clone(boolean bool)
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
        if(bool == true){
        clone.boardHistory = cloneHistory();
        clone.counter = counter;
    }
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
    /**
     * Add a watcher to the list so that they get notified when a Tile changes
     * its state
     * @param watcher
     */
    public void attachWatcher(Watcher watcher)
    {
       watchers.add(watcher);
    }

    /**
     * Remove a watcher from the list so that it no longer gets notified
     * @param watcher
     */
    public void detachWatcher(Watcher watcher)
    {
        if(watchers.contains(watcher))
        {
            watchers.remove(watcher);
        }
    }

    /**
     * Call the update method on each Watcher so that they know a Tile changed
     * its state
     */
    public void notifyWatchers()
    {
        for(Watcher watcher: watchers)
        {
            watcher.update();
        }
    }

}
