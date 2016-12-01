package nl.dke.boardgame.treeStructure.mcTree;

import nl.dke.boardgame.game.GameState;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.treeStructure.Tree;

/**
 * Created by josevelasquez on 12/1/16.
 */
public class MCTree extends Tree {

    Move move;
    GameState state;

    public MCTree(MCBranch root, GameState state){
        super(root);
        move = root.getMove();
        this.state = state;
    }




}
