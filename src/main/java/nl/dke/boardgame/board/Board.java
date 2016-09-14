package nl.dke.boardgame.board;

/**
 * Created by josevelasquez on 9/12/16.
 */
public class Board {

    //11 * 11

    public Tile[][] board;


    public Board(int width, int height){
        initBoard(width,height);
    }

    private void initBoard(int width, int height){
        board = new Tile[width][height];

        for(int i = 0; i<width; i++){
            for (int j = 0; j< height; j++){
                board[i][j] = new Tile(Integer.toString(i) + ", " + Integer.toString(j));
            }
        }

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
        }
    }

    public String toString(){
        String st ="";

        for (Tile[] tiles: board){
            for (Tile t: tiles){
                st += t.toString() + ": " +t.printNeighbors()+ " \n";
            }

        }
        return st;
    }


}
