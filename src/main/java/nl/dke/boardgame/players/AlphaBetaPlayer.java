ackage nl.dke.boardgame.players;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.PieMove;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.HexTile;
import nl.dke.boardgame.game.board.TileState;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;
import sun.reflect.generics.tree.Tree;
import java.util.Set;
import java.util.HashSet;
import java.util.*;

/**
 * Created by Xavier on 1-11-2016.
 */
public class AlphaBetaPlayer extends HexPlayer {

    public AlphaBetaPlayer(TileState state) {
        super(state);
        if(this.claimsAs() == TileState.PLAYER2) counter =2;
        else counter =1;
    }

    TileState maximizer;
    Board lol;
    public static Board sickness;
    public static int bitch =0;
    int[][] rootNodeGraph;
    int[][] dummyGraph;
    Board dummyBoard;
    List<HexTile> neighbourList;
    Dijkstra dijkstraObject;
    int counter;
    int numberOfLeafNodes = 0;
    int boardWidth;int boardHeight;int boardSize;

    public void finishMove(Move move) {
        bitch =0;
        Board currentBoard = move.getBoard();

        boardWidth = currentBoard.getWidth();
        boardHeight= currentBoard.getHeight();
        boardSize  = boardWidth*boardHeight;

        dijkstraObject = new Dijkstra();
        maximizer = this.claimsAs();

        //Run alpha-Beta algorithm, returns the boardPlusScore at the leafNode it found
        BoardPlusScore result = alphaBeta(2, Integer.MIN_VALUE, Integer.MAX_VALUE, currentBoard, maximizer);
        System.out.println("final board:" + result.score );
        result.board.printBoard();

        //Boardhistory of the board alpha-beta returned
        int [][] history = result.board.getHistory();
        System.out.println("BoardHistory: ");
        for(int i = 0; i < history.length; i++){
            System.out.print("row: " + history[i][0]);
            System.out.print(" column: " + history[i][1] +"\n");
        }
        //set the move to be the move alpha-beta wants to make (the first newest move in the boardhistory)
        move.setRow(history[counter - 1][0]);
        move.setColumn(history[counter - 1][1]);

        counter+=2;
        System.out.println("number of leafNodes: " + numberOfLeafNodes);
        numberOfLeafNodes = 0;
    }

    public BoardPlusScore alphaBeta(int depth,int alpha, int beta, Board boardAB, TileState t){
        if(depth == 0 ){
            //creates a new BoardPlusScore object with the current board and the value according to the evaluation function
            numberOfLeafNodes++;
            return new BoardPlusScore(boardAB, evaluateBoardDijkstra(boardAB,maximizer));
        }

        BoardPlusScore justToStoreBoardandScore = null;
        BoardPlusScore bestBoardandScore = null;

        //list of all the possible boards after one move claimed by t
        ArrayList<Board> allPossibleBoards = boardAB.getAllPossibleBoardsAfter1Move(t);

        if(t == maximizer){
            int bestScore = Integer.MIN_VALUE;
            for(Board b : allPossibleBoards){
                justToStoreBoardandScore = alphaBeta(depth-1,alpha,beta,b,switchClaimer(t));
                if(justToStoreBoardandScore.score > bestScore){
                    bestScore = justToStoreBoardandScore.score;
                    bestBoardandScore = justToStoreBoardandScore;
                }
                alpha = Math.max(alpha,bestScore);
                if(beta <= alpha){
                    break;
                }
            }
            return bestBoardandScore;
        }else{
            int bestScore = Integer.MAX_VALUE;
            for(Board b: allPossibleBoards) {
                justToStoreBoardandScore = alphaBeta(depth-1,alpha,beta,b,switchClaimer(t));
                if(justToStoreBoardandScore.score < bestScore){
                    bestScore = justToStoreBoardandScore.score;
                    bestBoardandScore = justToStoreBoardandScore;
                }
                beta = Math.min(beta, bestScore);
                if (beta <= alpha) {
                    break;
                }
            }
            return bestBoardandScore;
        }
    }


    class BoardPlusScore{
        Board board;
        int score;
        public BoardPlusScore(Board board){
            this.board = board;
        }
        public BoardPlusScore(Board board, int score){
            this.board = board;
            this.score = score;
        }
    }

    public void finishPieMove(PieMove move) {

    }

    public PossiblePlayers getTypeOfPlayer() {
        return PossiblePlayers.alphabeta;
    }

    public TileState switchClaimer(TileState t) {
        if (t.equals(TileState.PLAYER1)) return TileState.PLAYER2;
        else if (t.equals(TileState.PLAYER2)) return TileState.PLAYER1;
        else return null;
    }

    int[][] clone;

    public int[][] cloneGraph(int[][] graph) {
        clone = new int[graph.length][graph.length];
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                clone[i][j] = graph[i][j];
            }
        }
        return clone;
    }

    public int[][] getGraph(Board board, TileState tilestate) {
        //adjacency matrix of 121x121 + two extra spaces for the edge pieces (edges of the board)
        //fill the matrix with 999's
        //every hexagon is represented as an integer, starting from 1 to 121
        //the number 0 represents the left edge pieces (in case of player1,red) & represents the top edge pieces (in case of player2blue)
        //the number 122 represents the right edge pieces (in case of player1,red) & represents the bottom edge pieces (in case of player2blue)

        /*
        Each hexagon is represented as a vertex, the edges are the connections with its neighbour.
        If a hexagon is already claimed by a player, it gets a value of 0.
        If a hexagon is neutral it gets a value of 1.
        If a hexagon is claimed by an opponent it gets a value of 999.

        Then the weights of the edges are calculated by adding the values of the adjacent tiles/vertices.

        So let's say you have a claimed tile next to a neutral tile, the weight of the edge between would be 0 + 1 = 1.
        */

        int[][] graph = new int[boardSize+2][boardSize+2];
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                graph[i][j] = 999;
            }
        }
        if(tilestate.equals(TileState.PLAYER1)){//edgepieces from left to right--RED
            for(int i = 1; i <= boardWidth; i++){
                graph[0][i + (i-1)*boardWidth-1] = getEdgeValue(board, i-1, 0, 0, tilestate);
                graph[i + (i-1)*boardWidth-1][0] = getEdgeValue(board, i-1, 0, 0, tilestate);

                graph[boardSize+1][i*boardWidth] = getEdgeValue(board,i-1,boardWidth-1,0,tilestate);
                graph[i*boardWidth][boardSize+1] = getEdgeValue(board,i-1,boardWidth-1,0,tilestate);
            }
        }
        if(tilestate.equals(TileState.PLAYER2)){//edgepieces from top to bottom--BLUE
            for(int i = 1; i <= boardHeight; i++){
                graph[0][i] = getEdgeValue(board, 0, i-1, 0, tilestate);
                graph[i][0] = getEdgeValue(board, 0, i-1, 0, tilestate);

                graph[boardSize+1][i+boardSize-boardHeight] = getEdgeValue(board, boardHeight-1, i-1, 0,tilestate);
                graph[i+boardSize-boardHeight][boardSize+1] = getEdgeValue(board, boardHeight-1, i-1, 0,tilestate);
            }
        }

        //populate the matrix
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardHeight; j++) {
                //we need the value of the currentTile & the neighbouring tiles, then add the values & put these in the matrix
                //we also need the 'indexes/locations' of the neighbouring tiles
                List<HexTile> neighbourList = board.getNeighbours(i, j);
                for (HexTile neighbour : neighbourList) {
                    graph[(boardHeight * i) + j + 1][(neighbour.getRow() * boardHeight) + neighbour.getColumn() + 1] = getEdgeValue(board, i, j, neighbour.getRow(), neighbour.getColumn(), tilestate);
                }
            }
        }
        //printGraph(graph);
        return graph;
    }
    public int evaluateBoardElectrical(Board board, TileState claimer){
        int[][] graphPlayerOne = getGraph(board, TileState.PLAYER1);
        int[][] graphPlayerTwo = getGraph(board, TileState.PLAYER2);
        /*
        make Matrix A,x,z
        A consists of: [G, B; C, D]
        x consists of: [v;j]
        z consists of: [i;e]
        */

        /* MAKING MATRIX G:(nxn)
        1.Each element in the diagonal matrix is equal to the sum of the conductance (one over the resistance) of each element connected to
        the corresponding node.  So the first diagonal element is the sum of conductances connected to node 1,
        the second diagonal element is the sum of conductances connected to node 2, and so on.

        2.The off diagonal elements are the negative conductance of the element connected to the pair of corresponding node.
        Therefore a resistor between nodes 1 and 2 goes into the G matrix at location (1,2) and locations (2,1).
        */
        double[][] G = new double[122][122];

        for(int row = 0; row < 122; row++){
            for(int col = 0; col < 122; col++){
                if(row == col){ // fill the diagonal elements of the G matrix.
                    double diagNumber = 0;
                    for(int i = 0; i < graphPlayerOne[row+1].length; i++){//iterate over the graph row
                        if(graphPlayerOne[row+1][i] <=2 && graphPlayerOne[row+1][i] >=1){
                            diagNumber+= (1.0/(double)graphPlayerOne[row+1][i]);
                        }
                    }
                    G[row][col] = diagNumber;
                }
            }
        }
        for(int row = 1; row < 122; row++){
            for(int col = 0; col < 122; col++){
                if(row != col){
                    if(graphPlayerOne[row][col] <=2 && graphPlayerOne[row][col] >=1){
                        G[row][col] = -(1.0 / (double)graphPlayerOne[row][col]);
                    }
                }
            }
        }
        //G DONE (hopefully)
        int[][] B = new int[1][122];
        B[0][121] = 1;
        //B DONE (hopefully)
        int[][] C = new int[122][1];
        C[121][0] = 1;
        //C DONE (hopefully) (C is the transpose of B)
        int[][] D = new int[1][1];
        D[0][0] = 0;
        //D DONE

        return 0;
    }

    public int evaluateBoardDijkstra(Board board, TileState claimer){
        /*
        You make the graphs from both player's perspective, then calculated the shortest path, which would be the chain of tiles that has the most amount of claimed tiles in it.
        Then you count the number of empty pieces in that chain.
        The evaluation value is the difference in empty pieces of the chains from both players.
        */

        int[][] graphPlayerOne = getGraph(board, TileState.PLAYER1);
        int[][] graphPlayerTwo = getGraph(board, TileState.PLAYER2);
        int row;int column;
        int path1Counter = 0;int path2Counter = 0;

        List<Integer> path1 = dijkstraObject.dijkstra(graphPlayerOne,0,boardSize+1);
        for(Integer x: path1){
            if(x == boardSize+1){}
            else {
                row = x / boardHeight;
                column = (x % boardHeight) - 1;
                if(x % boardHeight == 0){
                    row = (x / boardHeight) - 1;
                    column = boardHeight-1;
                }

                if (board.getState(row, column).equals(TileState.NEUTRAL)) {
                    path1Counter++;
                }
            }
        }
        List<Integer> path2 = dijkstraObject.dijkstra(graphPlayerTwo,0,boardSize+1);
        for(Integer x: path2){
            if(x == boardSize+1){}
            else {
                row = x / boardHeight;
                column = (x % boardHeight) - 1;
                if(x % boardHeight == 0){
                    row = (x / boardHeight) - 1;
                    column = boardHeight-1;
                }

                if (board.getState(row, column).equals(TileState.NEUTRAL)) {
                    path2Counter++;
                }
            }
        }

//        System.out.println("empty pieces in player 1's red path: " + path1Counter);
//        System.out.println("empty pieces in player 2's blue path: " + path2Counter);

        int result = 0;
        if(claimer.equals(TileState.PLAYER1)){
            result = path2Counter - path1Counter;
//            System.out.println("so score for player 1 is: " + result);
        }
        if(claimer.equals(TileState.PLAYER2)) {
            result = path1Counter - path2Counter;
//            System.out.println("so score for player 2 is: " + result);
        }
        return result;
    }

    public void printGraph(int[][] graph) {
        int counter = 0;
        for (int i = 0; i < graph.length; i++) {
            System.out.print(counter++ + "   ");
            for (int j = 0; j < graph[0].length; j++) {
                System.out.print(graph[i][j] + "  ");
            }
            System.out.print("\n");
        }
    }
    public void printGraph(double[][] graph) {
        int counter = 1;
        for (int i = 0; i < graph.length; i++) {
            System.out.print(counter++ + "   ");
            for (int j = 0; j < graph[0].length; j++) {
                System.out.print(graph[i][j] + "  ");
            }
            System.out.print("\n");
        }
    }

    //for if you don't know the values of the 2 tiles
    public int getEdgeValue(Board board, int row1, int column1, int row2, int column2, TileState tilestate) {
        int valueTileOne = getVertexValue(tilestate, board, row1, column1);
        int valueTileTwo = getVertexValue(tilestate, board, row2, column2);
        return valueTileOne + valueTileTwo;
    }

    //for if you don't know the value of 1 of the 2 tiles----primarily for edge pieces
    public int getEdgeValue(Board board, int row1, int column1, int value, TileState tilestate) {
        int valueTileOne = getVertexValue(tilestate, board, row1, column1);
        return valueTileOne + value;
    }

    public int getVertexValue(TileState tilestate, Board board, int row, int column) {
        TileState tileOwner = board.getState(row, column);
        if (tileOwner.equals(tilestate)) return 0; //if the tile is of the player who is evaluating the board
        else if (tileOwner.equals(TileState.NEUTRAL)) return 1; // if the tile is neutral
        else return 999; //if tile is of opponent
    }
}

class Data implements Comparable<Data> {
    public final int index;
    public final int priority;

    public Data(int index, int priority) {
        this.index = index;
        this.priority = priority;
    }

    @Override
    public int compareTo(Data other) {
        return Integer.valueOf(priority).compareTo(other.priority);
    }

    public boolean equals(Data other) {
        return priority == other.priority;
    }

    // also implement equals() and hashCode()
}

class Dijkstra {
	/* dijkstra(G,n,i,j)
		Given a weighted adjacency matrix for graph G, returns the shortest path.

		If G[i][j] > 2, there is no edge between vertex i and vertex j
		If G[i][j] <=2, there is an edge between i and j and the value of G[i][j] is its weight.
		No entry of G will be negative.
	*/
    //PATH
    static List<Integer> path = new ArrayList<>();
    public static int stackoverflowerrorcounter = 0;
    static int MAX_VERTS = 50000;

    static List<Integer> dijkstra(int[][] G, int i, int j) {
        path.clear();
        //Get the number of vertices in G
        int n = G.length;
	
	stackoverflowerrorcounter = 0;    
	
        //parent
        int parent[] = new int[G.length];
        parent[0] = -1;

        int[] distance = new int[G.length];
        PriorityQueue<Data> PQ = new PriorityQueue<Data>();
        boolean[] inTree = new boolean[G.length];

        for (int index = 0; index < G.length; index++) {
            if (index == i) {
                distance[index] = 0;
            } else {
                distance[index] = Integer.MAX_VALUE;
                PQ.add(new Data(index, distance[index]));
                inTree[index] = true;
            }
        }

        for (int index = 0; index < G.length; index++) { // for each edge (v,z) do
            if (G[i][index] <= 2) { // There is an edge
                if (distance[i] + G[i][index] < distance[index]) { // if D[v] + w((v,z)) < D[z] then
                    int oldIndex = distance[index];
                    distance[index] = distance[i] + G[i][index]; // D[z] ← D[v] + w((v,z))
                    parent[index] = i;
                    PQ.remove(new Data(index, oldIndex));
                    PQ.add(new Data(index, distance[index])); // update PQ wrt D[z]
                }
            }
        }


        while (PQ.peek() != null) { // If PQ isn't empty
            Data vertex = PQ.poll(); // RemoveMin
            for (int index = 0; index < G.length; index++) { // for each edge (u,z) with z ∈ PQ do
                if (G[vertex.index][index] <= 2 && inTree[index] == true) { // z ∈ PQ
                    if (distance[vertex.index] + G[vertex.index][index] < distance[index]) { // if D[v] + w((v,z)) < D[z] then
                        int oldIndex = distance[index];
                        distance[index] = distance[vertex.index] + G[vertex.index][index]; // D[z] ← D[v] + w((v,z))
                        parent[index] = vertex.index;
                        PQ.remove(new Data(index, oldIndex));
                        PQ.add(new Data(index, distance[index])); // update PQ wrt D[z]
                    }
                }

            }
        }
        makePath(parent, G.length-1);
        printPath(path);

//        if (distance[j] == Integer.MAX_VALUE || distance[j] < 0) {
//            return -1;
//        } else {
//            return distance[j];
//        }
        return path;

    }
    static void printPath(List<Integer> path){
        for(Integer i: path){
            System.out.print(i+" ");
        }System.out.println();
    }
    static void makePath(int[] parent, int j){
        if(parent[j] == -1) return;
	stackoverflowerrorcounter++;

        if(stackoverflowerrorcounter > 100){
            return;
        }
        makePath(parent, parent[j]);

        path.add(j);
    }
}

