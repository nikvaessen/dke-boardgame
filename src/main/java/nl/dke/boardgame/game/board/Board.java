package nl.dke.boardgame.game.board;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
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
    /**
     * The 2D array of Tiles which together make the game board
     */
    private HexTile[][] board;

    /**
     * List of watchers to notify when the board changes
     */
    private List<Watcher> watchers;

    /**
     * The width of the board
     */
    private int width;

    /**
     * The height of the board
     */
    private int height;

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

    /**
     * Constructs the 2d array of HexTiles. Each tile in a new row will connect
     * to the down right side of the row above it. This will cause a shift like:
     * r1: #####
     * r2:  #####
     * ect.
     *
     * @param width  the length of each row
     * @param height the amount of rows
     */
    
    public int getWidth() 
    {
    	return width;
    }
    
    public int getHeight() 
    {
    	return height;
    }
    /*
     * Returns the height and width of the board 
     */
    private void initBoard(int width, int height)
    {
        //create the 2d array
        board = new HexTile[width][height];

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
                    if (j - 1 > 0)
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
                    if (i - 1 > 0)
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
                        if (j - 1 > 0)
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
    private HexTile getTile(int row, int column)
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
     * claim the given location in the board for a player
     *
     * @param row the row of the Tile, starting at the top
     * @param column the column of the Tile, starting at the left side
     * @return the state of the HexTile at the given position
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
        notifyWatchers();
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
    public Board clone()
    {
        Board clone = new Board(width, height);
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
            watcher.update(clone());
        }
    }
}
