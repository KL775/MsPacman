package pacman.entries.pacman.Uninformed;

import pacman.game.Constants;
import pacman.game.Game;

/**
 * Created by Kevin on 10/17/2016.
 */

//Class of nodes created to be used for BFS, DFS, and Iterative Deepening
public class nodes implements Comparable<nodes> {
    private Game game;
    private Constants.MOVE move;
    private nodes parentNode;
    private int depth;

    //constructor
    public nodes(Game game, Constants.MOVE move, nodes parents, int nodeDepth) {
        this.game = game;
        this.move = move;
        this.parentNode = parents;
        this.depth = nodeDepth;
    }

    //sort nodes by depth
    public int compareTo(nodes other) {
        return Integer.compare(this.depth, other.depth);
    }

    public Game getGame() {
        return game;
    }

    public Constants.MOVE getMove() {
        return move;
    }

    public nodes getParent() {
        return parentNode;
    }

    public int getDepth() {
        return depth;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setMove(Constants.MOVE move) {
        this.move = move;
    }

    public void setParent(nodes parents) {
        this.parentNode = parents;
    }

    public void setDepth(int nodeDepth) {
        this.depth = nodeDepth;
    }
}
