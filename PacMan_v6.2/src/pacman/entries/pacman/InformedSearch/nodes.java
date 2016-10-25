package pacman.entries.pacman.InformedSearch;

import pacman.game.Constants;
import pacman.game.Game;
/**
 * Created by Kevin on 10/18/2016.
 */
public class nodes implements Comparable<nodes>{
    private int depth;
    private double cost = 0;
    private nodes parent;
    private Game game;
    private Constants.MOVE move;

    public nodes(Game game, Constants.MOVE move, nodes parent, double cost, int depth){
        this.game = game;
        this.move = move;
        this.parent = parent;
        this.cost = cost;
        this.depth = depth;
    }

    public int compareTo(nodes other) {
        return Double.compare(this.cost, other.cost);
    }
    //our comparison will be based on nodes cost, used for priority queue

    public double getCost(){
        return cost;
    }
    public int getDepth(){
        return depth;
    }
    public Game getGame() {
        return game;
    }
    public Constants.MOVE getMove(){
        return move;
    }
    public nodes getParent(){
        return parent;
    }

}
