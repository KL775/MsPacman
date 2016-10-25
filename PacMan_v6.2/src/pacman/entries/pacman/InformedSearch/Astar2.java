package pacman.entries.pacman.InformedSearch;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.Collections;
import java.util.PriorityQueue;


/**
 * Created by Kevin on 10/18/2016.
 */
public class Astar2 extends Controller<MOVE> {
    PriorityQueue<nodes> evaluatedtree = new PriorityQueue<>();
    private int repeatLoop = 2;

    public double evaluateCurrentState(Game currentState) {
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

                }
                else {
                    closestScaredGhostDist = closestGhostDist;
                }
                break;
            }
            if ((closestGhostDist > currentDist)){
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
        int pillsLeft = currentState.getNumberOfPills() - currentState.getNumberOfActivePills();
        int powPillsLeft = currentState.getNumberOfPowerPills() - currentState.getNumberOfActivePowerPills();
        double benefit = currentState.getScore() + -4 * closestGhostDist + 3 * closestScaredGhostDist + -2 * pillsLeft + -3.5 * powPillsLeft;
        return -1*benefit;
    }

    public MOVE getMove(Game game, long timeDue) {
        int repeats = 0;
        nodes root = new nodes(game.copy(), null, null, 0, 0);
        evaluatedtree.add(root);
        while (repeats < repeatLoop){
            nodes parent = null;
            nodes currentBestPath = evaluatedtree.remove();
            repeats++;
            for (MOVE moves : MOVE.values()) {
                Game tempGame = game.copy();
                for (int j = 0; j < 4; j++) {
                    tempGame.advanceGame(moves, new StarterGhosts().getMove(game.copy(), -1));
                }
                double costOfMove = evaluateCurrentState(tempGame);
                if (repeats != 0) {
                    parent = currentBestPath;
                }
                nodes child = new nodes(tempGame, moves, currentBestPath, currentBestPath.getCost()+costOfMove, repeats);
                evaluatedtree.add(child);
            }
        }
        nodes bestPath = evaluatedtree.remove();
        while (bestPath.getDepth() != repeatLoop) {
           bestPath = evaluatedtree.remove();
        }
        for (int i = 0; i < repeatLoop - 1; i++) {
            bestPath = bestPath.getParent();
        }
        return bestPath.getMove();
    }
};
