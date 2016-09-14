package nl.dke.boardgame.board;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
import nl.dke.boardgame.exceptions.NoNeighbourException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a HexTile on a Hex game board.
 * Every tile can be either neutral, owned by player1 or owned by player2
 *
 * A HexTile has 6 neighbours. The sides are represented as integer values
 * between 0 and 5. The integers correspond with the following sides:
 *
 *           5   0
 *          4  #  1     # = this tile
 *           3   2
 *
 * @author josevelasquez on 9/12/16.
 * @author Nik
 */
public class HexTile {

    /**
     * List of the neighbouring tiles
     */
    private List<Bridge> neighbors;

    /**
     * String which represents the co-ordinates of this tile in a 2d array
     * x and y coordinates in a String "x,y", where each number has 2 chars
     */
    private String coordString;

    /**
     * Holds who owns the tile (player1, player2, or no one)
     */
    private TileState state;

    /**
     * Construct a HexTile. The co-ordinates are purely for representation
     * reasons
     * @param i the y or height co-ordinate of this tile in a 2d array
     * @param j the x or width  co-ordinate of this tile in a 2d array
     */
    public HexTile(int i, int j){
        coordString = String.format("%2d,%2d", i, j);
        neighbors = new ArrayList<Bridge>(6);
        state = TileState.NEUTRAL;
        initBridges();
    }

    /**
     * Claim the tile for either player1 or player2
     * @param claimer who is claiming the tile
     * @throws AlreadyClaimedException when this tile is already owned
     * @throws IllegalArgumentException when the claimer is the neutral value
     */
    public void claim(TileState claimer)
        throws AlreadyClaimedException, IllegalArgumentException
    {
        if(state != TileState.NEUTRAL)
        {
            throw new AlreadyClaimedException("Tile has already been claimed!");
        }
        if(claimer == TileState.NEUTRAL)
        {
            throw new IllegalArgumentException("Tile cannot be claimed by neutral");
        }
        state = claimer;
    }

    /**
     * Get who owns this tile
     * @return the owner of this tile
     */
    public TileState getState() {
        return state;
    }

    /**
     * Reset the tile so the state is neutral again and nobody owns it
     */
    public void reset() {
        this.state = TileState.NEUTRAL;
    }

    /**
     * Add a neighbour to this tile
     * @param side on which side to add the neighbour
     * @param newNeighbor the Tile instance who is the neighbour
     * @throws IllegalArgumentException when side is not between 0 and 5
     * or a neighbour is already added
     */
    public void addNeighbors(int side, HexTile newNeighbor)
        throws IllegalArgumentException
    {
        if(side < 0 || side > 5)
        {
            throw new IllegalArgumentException("HexTile only has 6 sides");
        }
        if(neighbors.get(side).isComplete())
        {
            throw new IllegalArgumentException("Trying to add a neighbour to" +
                    "side " + side + " but this side already has one");
        }
        neighbors.get(side).addTile(newNeighbor);
    }

    /**
     * Get the neighbour at the specified side of the Tile
     * @param side at which side the neighbour need to be retrieved
     * @throws IllegalArgumentException when the side is not between 0 and 5
     * @throws NoNeighbourException when that side does not have a neighbour
     */
    public HexTile getNeighbour(int side)
        throws IllegalArgumentException, NoNeighbourException
    {
        if(side < 0 || side > 5){
            throw new IllegalArgumentException("HexTile only has 6 tiles");
        }
        if(!neighbors.get(side).isComplete())
        {
            throw new NoNeighbourException("Tile does not have a neighbour at" +
                    "side " + side);
        }
        return neighbors.get(side).neighbour;
    }

    /**
     * String representation of the tile
     * @return the tile as "{(x,y);state}"
     */
    public String toString(){
        return String.format("{(%s);%s}", coordString, state);
    }

    /**
     * print all the neighbours
     * @return a string with the all the neighbours string representation
     */
    public String printNeighbors(){
        String st = "";
        Bridge b;

        // the maximal length of a String of a Tile:
        // The coordinates will take up "( x, y);" = 8 chars
        // the state can take up 7 chars
        // so total is 8 + 7 = 15

        for(int i = 0; i < neighbors.size(); i++){
            st += i + ":";
            b = neighbors.get(i);
            if(!b.isComplete())
                st += String.format(" %-16s ", "NO NEIGHBOUR");
            else
                // "{} are already included in the toString() method
                st += String.format("%15s ", b.neighbour);
        }
        return st;
    }

    /**
     * Populate the List of Bridges with for now empty relationships
     */
    private void initBridges(){
        for(int i = 0; i<6; i++ ){
            neighbors.add(new Bridge(this));
        }
    }

    /**
     * The bridge class connects this HexTile to another one
     */
    private class Bridge{

        public HexTile me, neighbour;

        public Bridge(HexTile a){
            this.me = a;
        }

        public void addTile(HexTile tile){
            neighbour = tile;
        }

        public boolean isComplete(){
            return neighbour != null;
        }
    }
}
