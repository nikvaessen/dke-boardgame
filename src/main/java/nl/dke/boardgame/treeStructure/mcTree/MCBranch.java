package nl.dke.boardgame.treeStructure.mcTree;

import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.treeStructure.Branch;

import java.util.Random;

/**
 * @author josevelasquez
 */
public class MCBranch extends Branch{

    public static final int MAX_BRANCHING = 30;
    private TileState player;

    public MCBranch(Move move, TileState player){
        super(move);
        this.player = player;
    }

    public MCBranch(Move move,MCBranch parent){
        super(move, parent);
        player = parent.player;
    }

    @Override
    public int evaluate(Board board) {
        return 0;
    }

    /**
     * This should be called from the MCTree to keep track of the number of nodes
     */
    @Override
    public  void expand(){
        super.expand();
        selectionAndExpansion();

    }

    /**
     * In this part we expand the this branch with random valid moves
     * Maybe implementing a list of the neutral tiles and randomly select from the list
     *      might be a better option
     */
    public void selectionAndExpansion(){

        for(int i = 0; i< MAX_BRANCHING; i++){

            Board nBoard = move.getBoard().clone();
            Random r = new Random();
            TileState t = player;
            boolean played = false;
            while (!played){

                int x = r.nextInt(11);
                int y = r.nextInt(11);

                /**
                 * this part is support to do create new branches to further the simulation
                 */
                if(nBoard.getState(x,y) == TileState.NEUTRAL){
                    //TODO: can't really make the move work
                    //nBoard.claim(x, y, t);
                    //branches.add(new MCBranch(nBoard, t));
                    // I dont understand why it wont let me do this^^
                }

            }
        }
    }




}
