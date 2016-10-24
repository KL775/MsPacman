package pacman.entries.pacman.InformedSearch;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.PriorityQueue;


/**
 * Created by Kevin on 10/18/2016.
 */
public class Astar2 extends Controller<MOVE> {
    PriorityQueue<nodes> evaluatedtree = new PriorityQueue<>();


    private int evaluateCurrentState(Game game) {
        //calculate cost of path
        int[] pillIndex = game.getActivePillsIndices();
        int[] powPillIndex = game.getActivePillsIndices();
        int currentPos = game.getPacmanCurrentNodeIndex();
        PriorityQueue<Integer> distToPills = new PriorityQueue<>();
        //prioritize power pills over pills
        return 0;
    }

    public MOVE getMove(Game game, long timeDue) {
        int currIter = 0;
        nodes root = new nodes(game.copy(), null, null, 0, 0);
        evaluatedtree.add(root);
        //4 is max iterations
        for (; currIter < 4; ++currIter) {
            nodes nodeParent = null;
            nodes bestMove = evaluatedtree.remove();
            for (MOVE move : MOVE.values()) {
                Game temp = game.copy();
                temp.advanceGame(move, new StarterGhosts().getMove(temp, -1));
                int currCost = evaluateCurrentState(temp);
                if (currIter != 0) {
                    nodeParent = bestMove; //if we're not at root, make the most recent value on queue the parent
                }
                nodes tempNode = new nodes(temp, move, nodeParent, bestMove.getCost() + currCost, currIter);
                evaluatedtree.add(tempNode);
            }
        }
        nodes trueBest = evaluatedtree.remove(); //since queue is sorted, remove minimum cost node
        while (trueBest.getDepth() > 1) {
            trueBest = trueBest.getParent();
        }
        return trueBest.getMove();
    }
};
