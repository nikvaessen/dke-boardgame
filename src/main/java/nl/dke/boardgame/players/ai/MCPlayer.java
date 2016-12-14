package nl.dke.boardgame.players.ai;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.PieMove;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.HexTile;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.players.PossiblePlayers;
import nl.dke.boardgame.treeStructure.mcTree.MCBranch;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author josevelasquez
 */

public class MCPlayer extends AIHexPlayer {

    Move lastMove;
    private double minLen = 121;
    public int diffLevel = 5;

    /**
     * Create the HexPlayer
     *
     * @param claimer if the player is player1 or player2
     * @throws IllegalArgumentException when neutral is given as argument
     */
    public MCPlayer(TileState claimer) throws IllegalArgumentException {
        super(claimer);
    }

    @Override
    public void finishMove(Move move) {

        move = makeMove(move);

    }

    /**
     * @param lastMove
     * @return
     */
    private Move makeMove(Move lastMove){
        long t = System.currentTimeMillis();

        this.lastMove = lastMove;
        ArrayList<MCBranch> tree = buildTree(new MCBranch(lastMove, claimsAs()), lastMove, 1);

        double best =-9999;

        Move bestMove = tree.get(0).getMove();
        
        for (int i=0; i<tree.size(); i++) {
        	System.out.println(i);
            double score = calcWinPercent(tree.get(i).getMove().getBoard());

            if (score>best) {
                best=score;
                bestMove = tree.get(i).getMove();
            }
        }

        System.out.println(System.currentTimeMillis()-t);
        return bestMove;
    }

    private boolean playRandomGame(Board b){

        Board bo = b.clone();
        HexTile[][] board = b.getBoard().clone();

        // reference to empty tiles
        ArrayList<HexTile> ref = new ArrayList<>();
        for(HexTile[] tiles: board){
            for(HexTile tile: tiles){
                if (tile.getState() == TileState.NEUTRAL)
                    ref.add(tile);
            }
        }

        TileState currPlayer = TileState.PLAYER1;
        if(claimsAs() == TileState.PLAYER1)
            currPlayer = TileState.PLAYER2;

        while (ref.size()>0){
            Random r = new Random();
            int ran = r.nextInt(ref.size());
            HexTile tile = ref.remove(ran);

            int x = tile.getColumn();
            int y = tile.getRow();

            /*
            while (board[y][x].getState() != TileState.NEUTRAL) {
                x=(int)(Math.random()*board.length);
                y=(int)(Math.random()*board.length);
            }*/

            try{
                bo.claim(x,y, currPlayer);
            } catch (AlreadyClaimedException e){
                continue;
            }

            if(currPlayer == TileState.PLAYER2){
                currPlayer = TileState.PLAYER1;
            }

            if(currPlayer== TileState.PLAYER1){
                currPlayer = TileState.PLAYER2;
            }

        }

        return calcVal(bo) > Math.pow(board.length, 2);
    }

    private boolean hasEmpty(HexTile[][] b){

        for(HexTile[] tiles: b){
            for(HexTile tile: tiles){
                if (tile.getState() == TileState.NEUTRAL)
                    return true;
            }
        }

        return false;

    }

    private double calcWinPercent(Board b) {

        HexTile[][] board = b.getBoard();

        int winCount = 0;

        for (int i=0; i<diffLevel; i++) {
            if (playRandomGame(b)) {
                winCount++;
            }
        }

        return ((double)winCount)/((double)diffLevel);
    }

    private ArrayList<MCBranch> buildTree(MCBranch parent, Move move, int depth){

        ArrayList<MCBranch> nodes=new ArrayList<>();
        HexTile[][] board = move.getBoard().getBoard();

        for (int y=0; y<board.length; y++) {
            for (int x=0; x<board[y].length; x++) {
                if (board[y][x].getState() == TileState.NEUTRAL) {

                    MCBranch node = new MCBranch(lastMove, parent);

                    Board newBoard = lastMove.getBoard().clone();

                    try{
                        newBoard.claim(x,y,claimsAs());
                    }  catch (AlreadyClaimedException e){
                        continue;
                    }

                    Move nMove = new Move(newBoard, claimsAs());
                    nMove.setColumn(x);
                    nMove.setRow(y);
                    if (depth>1) {
                        buildTree(node, nMove, depth-1);
                    }
                    nodes.add(node);
                }
            }
        }

        if (parent!=null) {
            parent.setChildren(nodes);
        }





        return nodes;
    }

    @Override
    public void finishPieMove(PieMove move) {
        finishMove(move);
    }

    @Override
    public PossiblePlayers getTypeOfPlayer() {
        return PossiblePlayers.montecarlo;
    }

    public double calcVal(Board b){

        HexTile[][] board = b.getBoard();

        TileState opp = TileState.PLAYER1;
        if(claimsAs() == TileState.PLAYER1)
            opp = TileState.PLAYER2;

        minLen=49;
        double maxno=999;
        double minnp=999;


        for(int i = 0; i <board.length; i++){

            if(board[i][0].getState() != opp){

                int initCountP = 1;
                int initCountO = 1;

                if(board[i][0].getState()== claimsAs()){
                    initCountP = 0;
                }
                if(board[i][0].getState() == opp){
                    initCountO = 0;
                }

                HexTile pTile, oTile;

                if(claimsAs() == TileState.PLAYER1){
                    pTile = new HexTile(i,0);
                    oTile = new HexTile(0,i);
                } else {
                    pTile = new HexTile(0,i);
                    oTile = new HexTile(i,0);
                }

                double no = calcN(b, opp, oTile, new ArrayList<HexTile>(), initCountO);
                minLen = 121;

                if(no<maxno){
                    maxno = no;
                }
            }
        }
        return maxno;
    }


    private double calcN(Board b, TileState player, HexTile l, ArrayList<HexTile> visited, double count){

        HexTile[][] board = b.getBoard();

        if(count < minLen && ((player == TileState.PLAYER1 && l.getColumn() == 10) ||
                (player == TileState.PLAYER2 && l.getRow() == 10))){
            minLen=count;
            return count;
        } else if((player == TileState.PLAYER1 && l.getColumn() == 10) ||
                (player == TileState.PLAYER2 && l.getRow() == 10)){
            return count;
        }

        if(player == TileState.PLAYER1 && count +(10-l.getRow()) >= minLen){
            return 999;
        } else if (player == TileState.PLAYER2 && count +(10-l.getColumn()) >= minLen){
            return 999;
        }

        ArrayList<HexTile>  adj = new ArrayList<>(l.getNeighbours());
        ArrayList<HexTile> v = cloneAL(visited);

        v.add(new HexTile(l.getColumn(), l.getRow()));

        double min = -999;

        for(HexTile tile: adj){
            if(!ALContains(v, tile) && board[tile.getColumn()][tile.getRow()].getState() == player ||
                    board[tile.getColumn()][tile.getRow()].getState() == TileState.NEUTRAL){

                double val=999;

                if(board[tile.getColumn()][tile.getRow()].getState() == player) {
                    val = calcN(b, player, tile, v, count);
                } else {
                    val=calcN(b, player, tile, v, count+1);
                }

                if (val<min) {
                    min=val;
                }
            }
        }

        return min;
    }

    private ArrayList<HexTile> cloneAL(ArrayList<HexTile> tiles){
        ArrayList<HexTile> clone = new ArrayList<>();
        for(HexTile tile: tiles){
            clone.add(tile);
        }
        return clone;
    }

    private boolean ALContains(ArrayList<HexTile> visited, HexTile l){
        for (HexTile tile: visited) {
            if (tile.equals(l)) {
                return true;
            }
        }

        return false;
    }

}