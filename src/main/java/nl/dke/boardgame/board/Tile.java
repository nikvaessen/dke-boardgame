package nl.dke.boardgame.board;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by josevelasquez on 9/12/16.
 */
public class Tile {

    List<Bridge> neighbors;//to link neighboring tiles, this can be improved
    String name;// x and y coordinates in a String "x,y"
    Color color;//To check if what player or if empty

    public Tile(String name){
        this.name = name;
        neighbors = new ArrayList<Bridge>();
        color = Color.WHITE;
        initBridges();
    }


    public void addNeighbors(int side, Tile newNeighbor) {
        neighbors.get(side).addTile(newNeighbor);
    }

    public String toString(){
        return "{(" + name  + ")" + colorString() + "}";
    }

    public String printNeighbors(){
        String st= "";
        for(Bridge b: neighbors){
            if(b.b == null)
                st+= "";
            else
                st+= b.b.toString() + " ";
        }
        return st;
    }

    private String colorString(){
        if(color == Color.blue)
            return "blue";
        else if(color == Color.red)
            return "red";
        else
            return "white";
    }

    private void initBridges(){
        for(int i = 0; i<6; i++ ){
            neighbors.add(new Bridge(this));
        }
    }

    private class Bridge{

        public Tile a,b;
        private boolean complete;


        Bridge(Tile a){
            this.a =a;
            complete = false;
        }

        public void addTile(Tile tile){
            b = tile;
            complete =true;
        }

        public boolean isComplete(){
            return complete;
        }
    }
}
