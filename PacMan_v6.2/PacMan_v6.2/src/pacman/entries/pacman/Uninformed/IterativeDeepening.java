package pacman.entries.pacman.Uninformed;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Game;

import java.util.Stack;

import static pacman.game.Constants.MOVE;

/**
 * Created by Kevin on 10/17/2016.
 */
public class IterativeDeepening extends Controller<MOVE> {
    //optimize based on score
    private int iteration = 10;
    private nodes bestPath = null;
    private int currentBestScore = 0;
    private int currentDepth = 0;
    private Stack<nodes> tree = new Stack<>();

    private void depthLimited(Game game, int currentDepth, int limit) {
        MOVE[] moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
        for (int i = currentDepth; i < limit; i++) {
            //expand tree!!
            nodes temp = tree.peek(); //fetch most recently pushed in node
            for (MOVE ways : moves) {
                Game tempGame = temp.getGame().copy();
                for (int iters = 0; iters < 8; iters++) {
                    tempGame.advanceGame(ways, new StarterGhosts().getMove(tempGame.copy(), -1));
                }
                int currentScore = tempGame.getScore();
                nodes child = new nodes(tempGame, ways, temp, temp.getDepth() + 1);
                if ((tempGame.getNumberOfActivePills() == 0) && (tempGame.getNumberOfActivePowerPills() == 0)) {
                    //if game over
                    if (currentScore > currentBestScore) {
                        currentBestScore = currentScore;
                        bestPath = child;
                    }
                }
                if (currentScore > currentBestScore) {
                    currentBestScore = currentScore;
                    bestPath = child;
                }
                tree.add(child);
            }
        }
    }

    public MOVE getMove(Game game, long timeDue) {
        nodes root = new nodes(game.copy(), null, null, 0);
        bestPath = root;
        int maxIter = iteration;
        int currentIter = 0;
        tree.add(root);
        for (;currentIter < maxIter; ++currentIter) {
            depthLimited(game, currentDepth, currentIter);
            currentDepth = currentIter;
        }
        while (bestPath.getDepth() > 1){
            bestPath = bestPath.getParent();
        }
        return bestPath.getMove();
    }

};
