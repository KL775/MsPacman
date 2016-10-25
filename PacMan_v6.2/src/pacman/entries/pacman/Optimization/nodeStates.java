package pacman.entries.pacman.Optimization;

import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.*;
import pacman.game.Game;
import pacman.game.internal.Ghost;

import java.util.Random;
/**
 * Created by Kevin on 10/24/2016.
 */
public class nodeStates {
    //rank in priority greatest to least: closestGhostDist, amount of edibleGhosts, score
    private Game currentState;
    private MOVE[] possibleMoves;

    public nodeStates(Game game){
        this.currentState = game;
        this.possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
    }

    public Game getCurrentState(){
        return currentState;
    }
    public MOVE[] getPossibleMoves(){
        return possibleMoves;
    }
    public void setGameState(Game newState){
        currentState = newState;
    }

    public double evaluateCurrentState() {
        int GhostPanicDist = 200;
        int closestGhostDist = Integer.MAX_VALUE;
        int closestScaredGhostDist = Integer.MAX_VALUE;
        int inactiveGhosts = 0;
        for (Constants.GHOST ghosts : Constants.GHOST.values()) {
            int currentDist = currentState.getManhattanDistance(currentState.getPacmanCurrentNodeIndex(), currentState.getGhostCurrentNodeIndex(ghosts));
            if (currentState.getGhostEdibleTime(ghosts) > 0) {
                inactiveGhosts++;
                if (closestGhostDist > currentDist) {
                    closestScaredGhostDist = currentDist;

                } else {
                    closestScaredGhostDist = closestGhostDist;
                }
                break;
            }
            if (closestGhostDist > currentDist) {
                closestGhostDist = currentDist;
            }
        }
        if (inactiveGhosts == 0) {
            closestScaredGhostDist = 0;
        }
        if (inactiveGhosts == 4) {
            closestGhostDist = 0;
        }
        if (closestGhostDist > GhostPanicDist) {
            closestGhostDist = 0;
        }
        System.out.println(closestGhostDist);
        int pillsLeft = currentState.getNumberOfPills() - currentState.getNumberOfActivePills();
        int powPillsLeft = currentState.getNumberOfPowerPills() - currentState.getNumberOfActivePowerPills();
        return currentState.getScore() + -4 * closestGhostDist + 50 * closestScaredGhostDist + -2 * pillsLeft + -3.5 * powPillsLeft;
    }
}
