package pacman.entries.pacman.Uninformed;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

import java.util.Stack;

/**
 * Created by Kevin on 10/17/2016.
 */
public class DFS extends Controller<Constants.MOVE> {
    private int depthSearched = 10;
    private int bestScore = 0;
    private Stack<nodes> tree = new Stack<>();

    public MOVE getMove(Game game, long timeDue) {
        int iterations = depthSearched;
        nodes root = new nodes(game.copy(), null, null, 0);
        nodes best = root;
        tree.add(root);
        Constants.MOVE[] moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
        while(iterations > 0) {
            nodes temp = tree.peek();
            //get node on top of stack aka most recently added node
            for (MOVE move : moves) {
                Game tempGame = temp.getGame().copy();
                tempGame.advanceGame(move, new StarterGhosts().getMove(tempGame.copy(), -1));
                nodes child = new nodes(tempGame, move, temp, temp.getDepth() + 1);
                int currentScore = tempGame.getScore();
                if (currentScore > bestScore){
                    bestScore = currentScore;
                    best = child;
                }
                tree.push(child);
            }
            iterations--;
            //decrement iterations
        }
        //nodes myMove = tree.pop();
        nodes myMove = best;
        while (myMove.getDepth() > 1){
            myMove = myMove.getParent();
        }
        return myMove.getMove();
    };
}
