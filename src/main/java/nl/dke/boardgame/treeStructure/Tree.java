package nl.dke.boardgame.treeStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * @author josevelasquez
 */
public class Tree {

    private List<Branch> branches;

    public Tree(Branch root){
        branches = new ArrayList<>();
        branches.add(root);
    }

    public void expandBranch(int index){
        Branch branch = branches.get(index);
        branch.expand();
        branches.addAll(branch.getBranches());
    }

    public List<Branch> getBranches() {
        return branches;
    }

}
