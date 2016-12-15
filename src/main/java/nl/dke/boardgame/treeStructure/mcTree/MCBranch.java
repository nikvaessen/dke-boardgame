package nl.dke.boardgame.treeStructure.mcTree;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.treeStructure.Branch;

import java.util.List;
import java.util.Random;

/**
 * @author josevelasquez
 */
public class MCBranch extends Branch{

    List<MCBranch> children;

    public static final int MAX_BRANCHING = 30;
    private TileState player;

    public MCBranch(Move move, TileState player){
        super(move);
        this.player = player;
    }

    public MCBranch(Move move, MCBranch parent){
        super(move, parent);
        player = parent.player;
    }

    public void setChildren(List<MCBranch> children){
        this.children = children;
    }

    @Override
    public double evaluate(Board board) {

        return 0;
    }

    /**
     * This should be called from the MCTree to keep track of the number of nodes
     */
    @Override
    public  void expand() throws AlreadyClaimedException {
        super.expand();
    }


}
