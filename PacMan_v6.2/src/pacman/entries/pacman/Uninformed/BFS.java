package pacman.entries.pacman.Uninformed;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.PriorityQueue;

import static pacman.game.Constants.GHOST;
import static pacman.game.Constants.MOVE;

/**
 * Created by Kevin on 10/17/2016.
 */
public class BFS extends Controller<MOVE>{
    private int depthLimit = 10;
    private PriorityQueue<nodes> tree = new PriorityQueue<>();
    private ArrayList<nodes> indexedNodes = new ArrayList<>();

    public BFS(){}

    public MOVE getMove(Game game, long timeDue) {
        int iteration = depthLimit;
        nodes root = new nodes(game.copy(), null, null, 0);
        tree.add(root);
        MOVE[] moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
        indexedNodes.add(root);
        for (int i = 0; i < iteration; i++) {
            nodes temp = indexedNodes.get(i);
            for (MOVE move : moves) {
                Game tmpGame = temp.getGame();
                //create a temporary instance of game to use to predict future
                for (int iters = 0; iters < 8; iters++) {
                    tmpGame.advanceGame(move, new StarterGhosts().getMove(tmpGame.copy(), -1));
                }
                nodes child = new nodes(tmpGame, move, temp, temp.getDepth() + 1);
                //expand node
                tree.add(child);
                indexedNodes.add(child);
                //push moves to queue and list
            }
        }

        tree.remove(); // get rid of root node
        nodes foundMove = tree.remove();
        while (foundMove.getDepth() != 1) {
            foundMove = foundMove.getParent();
        }
        return foundMove.getMove();

    }
}

