package nl.dke.boardgame.players;

import Jama.Matrix;
import nl.dke.boardgame.exceptions.AlreadyClaimedException;
import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.PieMove;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.HexTile;
import nl.dke.boardgame.game.board.TileState;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;
import java.util.*;

/**
 * Created by Xavier on 1-11-2016.
 */
public class AlphaBetaElectrical extends HexPlayer {

    public AlphaBetaElectrical(TileState state) {
        super(state);
        if(this.claimsAs() == TileState.PLAYER2) counter =2;
        else counter = 1;
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

        maximizer = this.claimsAs();

        BoardPlusScore result = new BoardPlusScore();
        int HOWMANYSEC = 5000;
        double timeNow = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis() - timeNow );
        for(int i = 2; i <= 5; i++){
            System.out.println(i + "th time");
            System.out.println("timedifference: " + (System.currentTimeMillis() - timeNow));
            if((System.currentTimeMillis() - timeNow) <= HOWMANYSEC) {
                result = alphaBeta(i, Integer.MIN_VALUE, Integer.MAX_VALUE, currentBoard, maximizer);
            }
        }
        System.out.println("final board:" + result.score );
        result.board.printBoard();

        //Boardhistory of the board alpha-beta returned
        int [][] history = result.board.getHistory();

//        System.out.println("BoardHistory: ");
//        for(int i = 0; i < history.length; i++){
//            System.out.print("row: " + history[i][0]);
//            System.out.print(" column: " + history[i][1] +"\n");
//        }
        //set the move to be the move alpha-beta wants to make (the first newest move in the boardhistory)
        move.setRow(history[counter - 1][0]);
        move.setColumn(history[counter - 1][1]);


        counter+=2;
        System.out.println("number of leafNodes: " + numberOfLeafNodes);
        numberOfLeafNodes = 0;
    }

    public BoardPlusScore alphaBeta(int depth,double alpha, double beta, Board boardAB, TileState t){
        if(depth == 0 ){
            //creates a new BoardPlusScore object with the current board and the value according to the evaluation function
            //System.out.println("board at leavenode!;");
            //boardAB.printBoard();
            numberOfLeafNodes++;
            return new BoardPlusScore(boardAB, evaluateBoardElectrical(boardAB,maximizer));
        }

        BoardPlusScore justToStoreBoardandScore = null;
        BoardPlusScore bestBoardandScore = null;

        //list of all the possible boards after one move claimed by t
        ArrayList<Board> allPossibleBoards = boardAB.getAllPossibleBoardsAfter1Move(t);

        if(t == maximizer){
            double bestScore = Integer.MIN_VALUE;
            for(Board b : allPossibleBoards){
                justToStoreBoardandScore = alphaBeta(depth-1,alpha,beta,b,switchClaimer(t));
                if(justToStoreBoardandScore == null){
                    //System.out.println("dis bitch is null");
                    //System.out.println("dis bitch is null");
                    justToStoreBoardandScore = new BoardPlusScore(boardAB, Integer.MIN_VALUE);
                    //System.out.println("dis bitch is null");
                }
                //System.out.println("justtofuckscore:" + justToStoreBoardandScore.score);
                if(justToStoreBoardandScore.score > bestScore && justToStoreBoardandScore != null){
                    bestScore = justToStoreBoardandScore.score;
                    bestBoardandScore = justToStoreBoardandScore;
                }
                alpha = Math.max(alpha,bestScore);
                if(beta <= alpha){
                    break;
                }
            }
            //System.out.println("arrived");
            return bestBoardandScore;
        }else{
            double bestScore = Integer.MAX_VALUE;
            for(Board b: allPossibleBoards) {
                justToStoreBoardandScore = alphaBeta(depth-1,alpha,beta,b,switchClaimer(t));
                if(justToStoreBoardandScore == null){
                    //
                    //System.out.println("dis bitch is null2: ");
                    justToStoreBoardandScore = new BoardPlusScore(boardAB, Integer.MAX_VALUE);
                }
                if(justToStoreBoardandScore.score < bestScore && justToStoreBoardandScore != null ){
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
        double score;
        public BoardPlusScore(){}
        public BoardPlusScore(Board board){
            this.board = board;
        }
        public BoardPlusScore(Board board, double score){
            this.board = board;
            this.score = score;
        }
    }

    public void finishPieMove(PieMove move) {

    }

    public PossiblePlayers getTypeOfPlayer() {
        return PossiblePlayers.alphabetaElectrical;
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

    class Edge{
        int nodeID;
        int edgeWeight;
        Edge next;
    }

    public AdjacencyList getAdjacencyGraph(Board board, TileState tileState){
        //System.out.println("THIS IS THE BOARD;");
        //board.printBoard();
        AdjacencyList graph = new AdjacencyList(board.getSize()+2); //2 more for outer nodes
        //System.out.println("sick");
        //System.out.println(graph.getNumberOfVertices());
        int n = board.getHeight();
        int s = board.getSize();
        //first do the edge pieces from top-to-bottom for player 2: BLUE
        if(tileState.equals(TileState.PLAYER2)){
            for(int i = 1; i <= n; i++) {//for the uppernode 0
                int edgeV = getEdgeValue(board, 0, i-1, 0, tileState);
                if(edgeV <=2) {
                    graph.addEdge(0, i, edgeV );
                    graph.addEdge(i, 0, edgeV );
                }
            }
            for(int i = 1; i <= n; i++){//for the lowernode n*n+1
                int edgeV = getEdgeValue(board,n-1,i-1,0,tileState);
                if(edgeV <=2) {
                    graph.addEdge(s + 1, s - n + i, edgeV);
                    graph.addEdge(s - n + i, s + 1, edgeV);
                }
            }
        }
        //then do the edge pieces from top-to-bottom for player 1: RED
        if(tileState.equals(TileState.PLAYER1)){
            for(int i = 1; i <= n; i++){
                int edgeV = getEdgeValue(board,i-1,0,0,tileState);
                if(edgeV <= 2) {
                    graph.addEdge(0, 1 + ((i - 1) * n), edgeV);
                    graph.addEdge(1 + ((i - 1) * n), 0, edgeV);
                }
            }
            for(int i = 1; i <= n; i++){
                int edgeV = getEdgeValue(board,i-1,n-1,0,tileState);
                if(edgeV <= 2) {
                    graph.addEdge(s + 1, i * n, edgeV);
                    graph.addEdge(i * n, s + 1, edgeV);
                }
            }
        }
        //graph.printAdjacencyList();

        //now fill the rest of the graph
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                List<HexTile> neighbourList = board.getNeighbours(i,j);
                for(HexTile neighbour: neighbourList) {
                    int edgeV = getEdgeValue(board, i, j, neighbour.getRow(), neighbour.getColumn(), tileState);
                    if(edgeV <= 2) {
                        graph.addEdge((n * i) + j + 1, (neighbour.getRow() * n) + neighbour.getColumn() + 1, edgeV);
                    }
                }
            }
        }
        //graph.printAdjacencyList();

        return graph;
    }
    public double evaluateBoardElectrical(Board board, TileState claimer){

        //System.out.println("board getting evaluated:");
        //board.printBoard();

        AdjacencyList graphPlayer = getAdjacencyGraph(board, claimer);
        AdjacencyList graphOtherPlayer = getAdjacencyGraph(board, switchClaimer(claimer));

        double elecPlayer = getElectricalResistance(graphPlayer);
        double elecOtherPlayer = getElectricalResistance(graphOtherPlayer);
       // System.out.println("score of player: " + elecPlayer);
        //System.out.println("score of other player: " + elecOtherPlayer);

        double result = elecOtherPlayer / elecPlayer;
       // System.out.println("THE SCORE ITS GETS IS:" + result );
        return result;

    }
    public double getElectricalResistance(AdjacencyList graphPlayer){
      //  System.out.println("AT THE START:");
        //graphPlayer.printAdjacencyList();

        //this is just for the last node, and is the same as the block of code beneath this, but you first have to the the last node so the last node remains the reference node
        LinkedList<Pair<Integer,Integer>> linkList2 = graphPlayer.getListAt(boardSize+1);
        for(int j = 0; j < linkList2.size(); j++){
            if(linkList2.get(j).getValue() == 0 && linkList2.get(j).getKey() == 0){
                System.out.println("WINNING CHAIN BOOY");
                return 0;
            }
            if(linkList2.get(j).getValue() == 0){
                int numberOfNodeInQuestion = linkList2.get(j).getKey();
                LinkedList<Pair<Integer,Integer>> linkListOfNodeInQuestion = graphPlayer.getListAt(numberOfNodeInQuestion);
                for(int k = 0; k < linkListOfNodeInQuestion.size(); k++){
                    if(linkListOfNodeInQuestion.get(k).getKey() != boardSize+1){
                        graphPlayer.addEdge(boardSize+1,linkListOfNodeInQuestion.get(k).getKey(),linkListOfNodeInQuestion.get(k).getValue());
                        graphPlayer.addEdge(linkListOfNodeInQuestion.get(k).getKey(),boardSize+1,linkListOfNodeInQuestion.get(k).getValue());
                        graphPlayer.removeEdge(linkListOfNodeInQuestion.get(k).getKey(),new Pair<Integer,Integer>(numberOfNodeInQuestion,linkListOfNodeInQuestion.get(k).getValue()));
                    }
                }
                while (!linkListOfNodeInQuestion.isEmpty()) {
                    linkListOfNodeInQuestion.removeFirst();
                }
                linkList2.remove(linkList2.get(j));
                //graphPlayer.printAdjacencyList();
                j--;
            }
            //winining bitch
        }
        //System.out.println("AFTER DOING THE REFERENCE NODE:");
        //graphPlayer.printAdjacencyList();

        /*First we change the graphs.
        All the connected nodes with edges of zero, become one node.
        */
        //first loop through the array of lists ( aka the graph )
        for(int i = 0; i <= boardSize; i++){
            //the list at node i
            LinkedList<Pair<Integer,Integer>> linkList = graphPlayer.getListAt(i);
            //the listIterator for node i
            for(int j = 0; j < linkList.size(); j++){
                if(linkList.get(j).getValue() == 0){//if the weight of this edge is zero..
                    //this is the endVertex of that edge with weight of zero, which needs to be deleted.
                    int numberOfNodeInQuestion = linkList.get(j).getKey();
                    //now do an iteration over that node:
                    //the list at nodeInQuestion
                    LinkedList<Pair<Integer,Integer>> linkListOfNodeInQuestion = graphPlayer.getListAt(numberOfNodeInQuestion);

                    for(int k = 0; k < linkListOfNodeInQuestion.size(); k++){
                        if(linkListOfNodeInQuestion.get(k).getKey() != i){
                            graphPlayer.addEdge(i,linkListOfNodeInQuestion.get(k).getKey(),linkListOfNodeInQuestion.get(k).getValue());
                            graphPlayer.addEdge(linkListOfNodeInQuestion.get(k).getKey(),i,linkListOfNodeInQuestion.get(k).getValue());
                            graphPlayer.removeEdge(linkListOfNodeInQuestion.get(k).getKey(),new Pair<Integer,Integer>(numberOfNodeInQuestion,linkListOfNodeInQuestion.get(k).getValue()));
                        }
                    }
                    while (!linkListOfNodeInQuestion.isEmpty()) {
                        linkListOfNodeInQuestion.removeFirst();
                    }
                    linkList.remove(linkList.get(j));
                    //graphPlayer.printAdjacencyList();
                    j--;
                }
            }
        }
       // System.out.println("AFTER DOING ALL THE NODES");
       // graphPlayer.printAdjacencyList();
        //graph is tested and appears to be right/done!

        //making the G matrix:
        //the take the last node to be the reference node, so you delete it
        graphPlayer.removeList(graphPlayer.getNumberOfVertices()-1);
       // System.out.println("AFTER DELETING THE REFERENCE NODE");
        //graphPlayer.printAdjacencyList();

        // you subtract one of the nodes because one node is the reference node.
        ArrayList<ArrayList<Double>> G = new ArrayList<ArrayList<Double>>();

        //fill the G matrix with 99's
        for(int i = 0; i < graphPlayer.getNumberOfVertices(); i++){
            ArrayList<Double> temp = new ArrayList<Double>();
            for(int j = 0; j < graphPlayer.getNumberOfVertices(); j++){
                temp.add(0.0);
            }
            G.add(temp);
        }
        for(int i = 0; i < graphPlayer.getNumberOfVertices(); i++) {
           // System.out.println("i: " + i);

            //for the diagonal numbers.
            double number = 0.0;
            for (int k = 0; k < graphPlayer.getListAt(i).size(); k++) {
                number += (1.0 / (double) graphPlayer.getListAt(i).get(k).getValue());
            }
           // System.out.println("diag number:" + number);
            G.get(i).set(i, number);

            //for the non diagonal numers:
            for (int j = 0; j < graphPlayer.getListAt(i).size(); j++) {//for the non-diagonal numbers
                if (graphPlayer.getListAt(i).get(j).getKey() != boardSize + 1) {
                    double number2 = -1.0 / (double) graphPlayer.getListAt(i).get(j).getValue();
                   // System.out.println("your number: " + number2);
                    G.get(i).set(graphPlayer.getListAt(i).get(j).getKey(), number2);
                }
            }
        }
       // System.out.println("THIS IS G-MATRIX;");
       // printGraph(G);
        c=0;
        for(int i = 0; i < graphPlayer.getNumberOfVertices(); i++){
           // System.out.println("i: " + i);
            //check if empty node
            if(graphPlayer.getListAt(i).isEmpty()){
                //if it is empty, delete the redundant row and column.
                //delete the row
               // System.out.println("WAHTEFUCKIS C? >>>" + c);
                G.remove(i-c);
                //delete the column
                //System.out.println("ihave deleted row number: (iog)"+ i+ "praths"+ (i-c));
                //System.out.println("now g looks like this:");
                //printGraph(G);
                for(int j = 0; j < G.size(); j++){
                    G.get(j).remove(i-c);
                }
                c++;
            }

        }
       // System.out.println("THIS IS G-MATRIX AFTER REMOVING THE REDUNDANT ROWS/COLUMNS");
       // printGraph(G);

        //make the expanded matrix/array, but first add the B,C,D matrices
        double[][] A = new double[G.size()+1][G.size()+1];
        //copy the G into A
        for(int i = 0; i < A.length-1; i++){
            for(int j = 0; j < A[0].length-1; j++){
                A[i][j] = G.get(i).get(j);
            }
        }
        A[A.length-1][0] = 1;
        A[0][A.length-1] = 1;

        //System.out.println("THIS IS FINAL A");
        //printGraph(A);

        double[][] X = new double[A.length][1];
        X[0][0] = 10.0;
        X[A.length-1][0] = 10.0;
        //System.out.println("THIS IS X");
        //printGraph(X);
        Matrix FUA = new Matrix(A);
        Matrix SHI = new Matrix(X);

        Matrix ANSWER = FUA.solve(SHI);
        //System.out.println("YOUR ASNWERERERREERERE");
        //ANSWER.print(2,2);
        //System.out.println("10 / " + ANSWER.get(A.length-2,0) + " = ");
        double score = 10 / ANSWER.get(A.length-2,0);
       // System.out.println("score: " + score);
        return score;
    }
    public static int c;

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

        Then the weights of the edges are   lated by adding the values of the adjacent tiles/vertices.

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
    public void printGraph(ArrayList<ArrayList<Double>> graph) {
        int counter = 0;
        for (int i = 0; i < graph.size(); i++) {
            System.out.print(counter++ + "   ");
            for (int j = 0; j < graph.get(i).size(); j++) {
                System.out.print(graph.get(i).get(j) + "  ");
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

class AdjacencyList {
    //private final LinkedList< Pair<Integer, Integer> >[] adjacencyList;
    private final ArrayList<LinkedList<Pair<Integer,Integer>>> adjacencyList;

        // Constructor
        public AdjacencyList(int vertices) {
            //adjacencyList = (LinkedList< Pair<Integer, Integer> >[]) new LinkedList[vertices];
            adjacencyList = new ArrayList<>();
            for (int i = 0; i < vertices; ++i) {
                adjacencyList.add(i,new LinkedList<Pair<Integer,Integer>>());
            }
        }

        // Appends a new Edge to the linked list
        public void addEdge(int startVertex, int endVertex, int weight) {
            //adjacencyList[startVertex].add(new Pair<>(endVertex, weight));
            adjacencyList.get(startVertex).add(new Pair<>(endVertex,weight));
        }

        // Returns number of vertices
        // Does not change for an object
        public int getNumberOfVertices() {
            return adjacencyList.size();
        }

        //deletes List at 'n'th index
        public void removeList(int n){
            adjacencyList.remove(n);
        }

        // Returns number of outward edges from a vertex
        public int getNumberOfEdgesFromVertex(int startVertex) {
            //return adjacencyList[startVertex].size();
            return adjacencyList.get(startVertex).size();
        }

        public LinkedList<Pair<Integer,Integer>> getListAt(int number){
            //return adjacencyList[number];
            return adjacencyList.get(number);
        }

        // Returns a copy of the Linked List of outward edges from a vertex
        public LinkedList< Pair<Integer, Integer> > getEdgesFromVertex(int startVertex) {
            LinkedList< Pair<Integer, Integer> > edgeList
                    = (LinkedList< Pair<Integer, Integer> >) new LinkedList(adjacencyList.get(startVertex));

            return edgeList;
        }

        // Prints the Adjacency List
        public void printAdjacencyList() {
            int i = 0;

            for (LinkedList< Pair<Integer, Integer> > list : adjacencyList) {
                System.out.print("adjacencyList[" + i + "] -> ");

                for (Pair<Integer, Integer> edge : list) {
                    System.out.print(edge.getKey() + "(" + edge.getValue() + ")");
                }

                ++i;
                System.out.println();
            }
        }

        // Removes an edge and returns true if there
        // was any change in the collection, else false
        public boolean removeEdge(int startVertex, Pair<Integer, Integer> edge) {
            return adjacencyList.get(startVertex).remove(edge);
        }
    }


