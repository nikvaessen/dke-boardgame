package nl.dke.boardgame.game.board;

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
{

    /**
     * The 2D array of Tiles which together make the game board
     */
    private HexTile[][] board;

    /**
     * Constructs the Hex game board
     *
     * @param width  the width of the board
     * @param height the height of the board
     */
    public Board(int width, int height)
    {
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
    private void initBoard(int width, int height)
    {
        board = new HexTile[width][height];

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                board[i][j] = new HexTile(i, j);
            }
        }

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
     * Get the HexTile of the specified location in the board
     *
     * @param i the row of the Tile, starting at the top
     * @param j the column of the Tile, starting at the left side
     * @return the HexTile at the given position
     * @throws IllegalArgumentException when the given location is not valid
     */
    public HexTile getTile(int i, int j)
            throws IllegalArgumentException
    {
        if (i < 0 || i >= board.length || j < 0 || j >= board[i].length)
        {
            throw new IllegalArgumentException(String.format("i:%d,j:%d" +
                            " is out of bounds. Bounds are i:%d,j%d",
                    i, j, board.length, board[i].length));
        }
        return board[i][j];
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
    }

}
