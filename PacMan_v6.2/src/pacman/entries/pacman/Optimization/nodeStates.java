package pacman.entries.pacman.Optimization;

import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.*;
import pacman.game.Game;

/**
 * Created by Kevin on 10/24/2016.
 */
public class nodeStates {
    private Game currentState;
    private MOVE[] possibleMoves;
    public nodeStates(Game game){
        this.currentState = game;
        this.possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
    }
    public MOVE nextStateMove(){
        int randomMove = (int)Math.floor(Math.random()*5);
        return possibleMoves[randomMove];
    }
}
