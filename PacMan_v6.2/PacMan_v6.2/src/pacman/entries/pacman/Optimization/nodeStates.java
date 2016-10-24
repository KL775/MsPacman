package pacman.entries.pacman.Optimization;

import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.*;
import pacman.game.Game;

/**
 * Created by Kevin on 10/24/2016.
 */
public class nodeStates {
    private Game currentState;
    private MOVE[] movements;
    public nodeStates(Game game){
        this.currentState = game;
        this.movements = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
    }
    public nodeStates getNeighborState(){
        nodeStates nextNeighborState = new nodeStates(this);
        int randomIndex = Random.next(nextNeighborState.movements.length);
        int newMove = nextNeighborState.getPossibleMoves()[nextNeighborState.movements.length];
        nextNeighborState.movements[randomIndex] = newMove;
        return nextNeighborState;
    }
    public MOVE[] getPossibleMoves(){
        return possibleMoves;
    }
}
