package nl.dke.boardgame.board;

import java.util.IllegalFormatCodePointException;

/**
 * Created by josevelasquez on 9/12/16.
 */
public class Board {

    //11 * 11

    public HexTile[][] board;


    public Board(int width, int height){
        initBoard(width,height);
    }

    private void initBoard(int width, int height){
        board = new HexTile[width][height];

        for(int i = 0; i<width; i++){
            for (int j = 0; j< height; j++){
                board[i][j] = new HexTile(i, j);
            }
        }

        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                try
                {
                    HexTile tile = board[i][j];

                    // first off, the left and right tile on the same row
                    // are always neighbours
                    if(j - 1 > 0){
                        // left neighbour is side 4
                        tile.addNeighbors(4, getTile(i, j - 1));
                    }
                    if(j + 1 < height){
                        //right neighbour is side 1
                        tile.addNeighbors(1, getTile(i, j + 1));
                    }
                    // Note: the rules for ABOVE and BELOW are dependent on
                    // the rotation of the whole board
                    //
                    // for the row ABOVE, the neighbour is always in the same
                    // column and one column TO THE RIGHT
                    if(i - 1 > 0){
                        //up "left"(same column) neighbour is side 5
                        tile.addNeighbors(5, getTile(i - 1, j));
                        //up "right" column (to the right) neighbour is side 0
                        if(j + 1 < width){
                            tile.addNeighbors(0, getTile(i - 1, j + 1));
                        }
                    }
                    // for the row BELOW, the neighbour is always in the same
                    // column and one column TO THE LEFT
                    if(i + 1 < height){
                        //down "left"(to the left) neighbour is side 3
                        if(j - 1 < 0) {
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
        /*
        for(int i = 0; i<width; i++){
            for (int j = 0; j< height; j++){
                if (j!=0){
                    board[i][j].addNeighbors(0,board[i][j-1]);
                }
                if (i != width-1){
                    board[i][j].addNeighbors(1,board[i+1][j]);
                }
                if (i != width-1 && j!= height-1){
                    board[i][j].addNeighbors(2,board[i+1][j+1]);
                }
                if (j!= height-1){
                    board[i][j].addNeighbors(3, board[i][j+1]);
                }
                if (i!=0){
                    board[i][j].addNeighbors(4, board[i-1][j]);
                }
                if (i != 0 && j != 0){
                    board[i][j].addNeighbors(5, board[i-1][j-1]);
                }
            }
        }*/
    }

    public String toString(){
        String st ="";

        for (HexTile[] tiles: board){
            for (HexTile t: tiles){
                st += t.toString() + ": " +t.printNeighbors()+ " \n";
            }
        }
        return st;
    }


    public HexTile getTile(int i, int j)
        throws IllegalArgumentException
    {
        if(i < 0 || i >= board.length || j < 0 || j >= board[i].length) {
                throw new IllegalArgumentException(String.format("i:%d,j:%d" +
                        " is out of bounds. Bounds are i:%d,j%d",
                        i, j, board.length, board[i].length));
        }
        return board[i][j];
    }


}
