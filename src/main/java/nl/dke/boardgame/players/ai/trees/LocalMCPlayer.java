package nl.dke.boardgame.players.ai.trees;

import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.PieMove;
import nl.dke.boardgame.game.board.HexTile;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.players.PossiblePlayers;
import nl.dke.boardgame.players.ai.AIHexPlayer;

import java.util.ArrayList;
import java.util.Random;

/**
 *@author josevelasquez
 */
public class LocalMCPlayer extends AIHexPlayer{


    public LocalMCPlayer(TileState claimer) throws IllegalArgumentException {
        super(claimer);
    }

    @Override
    public void finishMove(Move move) {

        Pair shot = makeMove(move.getBoard().getBoard());

        try
        {
            move.setRow(shot.y);
            move.setColumn(shot.x);
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }

    }


    public Pair makeMove(HexTile[][] board) {

        Pair pair = null;

        TileState opp = TileState.PLAYER1;
        if(claimsAs() == TileState.PLAYER1)
            opp = TileState.PLAYER2;

        ArrayList<HexTile> oppTiles = new ArrayList<>();

        if(opp == TileState.PLAYER1){

            for(HexTile[] tiles: board){
                for (HexTile tile : tiles){
                    if(tile.getState()==TileState.PLAYER2)
                        oppTiles.add(tile);
                }
            }

            for (int i = 0; i<oppTiles.size()-1; i++){
                for (int j = i+1; j<oppTiles.size();j++){
                    if (intersects(oppTiles.get(i), oppTiles.get(j))){
                        HexTile intersect = getIntersection(oppTiles.get(i), oppTiles.get(j));
                        pair = new Pair(intersect);
                        return pair;
                    }
                }
            }



            if(oppTiles.size() > 0) {

                ArrayList<HexTile> empties = new ArrayList<>();
                Random r = new Random();

                for (HexTile tile : oppTiles) {
                    ArrayList<HexTile> n = (ArrayList<HexTile>) tile.getNeighbours();
                    for (HexTile t : n) {
                        if (t.getState() == TileState.NEUTRAL) {
                            empties.add(t);
                        }
                    }
                }

                int s = empties.size();
                pair = new Pair(empties.get(r.nextInt(s)));
                return pair;
            }

        } else {

            for(HexTile[] tiles: board){
                for (HexTile tile : tiles){
                    if(tile.getState()==TileState.PLAYER1)
                        oppTiles.add(tile);
                }
            }

            for (int i = 0; i<oppTiles.size()-1; i++){
                for (int j = i+1; j<oppTiles.size();j++){
                    if (intersects(oppTiles.get(i), oppTiles.get(j))){
                        HexTile intersect = getIntersection(oppTiles.get(i), oppTiles.get(j));
                        pair = new Pair(intersect);
                        return pair;
                    }
                }
            }

            ArrayList<HexTile> empties = new ArrayList<>();
            Random r = new Random();

            if(oppTiles.size() > 0) {

                for (HexTile tile : oppTiles) {
                    ArrayList<HexTile> n = (ArrayList<HexTile>) tile.getNeighbours();
                    for (HexTile t : n) {
                        if (t.getState() == TileState.NEUTRAL) {
                            empties.add(t);
                        }
                    }
                }

                int s = empties.size();
                pair = new Pair(empties.get(r.nextInt(s)));
                return pair;
            }

        }

        ArrayList<HexTile> neut = new ArrayList<>();

        for(HexTile[] tiles: board){
            for (HexTile tile : tiles){
                if(tile.getState()==TileState.NEUTRAL)
                    neut.add(tile);
            }
        }

        Random r= new Random();
        return new Pair(neut.get(r.nextInt(neut.size())));
    }

    private boolean intersects(HexTile a, HexTile b){

        for(HexTile ta: a.getNeighbours()){
            for(HexTile tb: b.getNeighbours()){
                if(ta.equals(tb) && ta.getState() == TileState.NEUTRAL){
                    return true;
                }
            }
        }
        return false;
    }

    private HexTile getIntersection(HexTile a, HexTile b){

        ArrayList<HexTile> intersections = new ArrayList<>();

        for(HexTile ta: a.getNeighbours()){
            for(HexTile tb: b.getNeighbours()){
                if(ta.equals(tb) && ta.getState() == TileState.NEUTRAL){
                    intersections.add(ta);
                }
            }
        }
        int s = intersections.size();
        Random r = new Random();
        return intersections.get(r.nextInt(s));
    }

    @Override
    public void finishPieMove(PieMove move) {
        Pair shot = makeMove(move.getBoard().getBoard());

        try
        {
            move.setRow(shot.x);
            move.setColumn(shot.y);
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public PossiblePlayers getTypeOfPlayer() {
        return PossiblePlayers.localmc;
    }

    private class Pair{
        public int x,y;

        public Pair(int x, int y){
            this.x = x;
            this.y = y;
        }

        public Pair(HexTile tile){
            x = tile.getColumn();
            y = tile.getRow();
        }
    }


}
