package pacman.entries.pacman.Optimization;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Created by Kevin on 10/22/2016.
 */
public class climbingHills extends Controller<MOVE> {
    public MOVE getMove(Game game, long timeDue) {
        Game[] possibleGames = new Game[5]; //array of games to hold each game state
        MOVE[] possibleMoves = new MOVE[5]; //array of possible moves
        int iteration = 0; //times we iterated
        for (MOVE move : MOVE.values()) {
            possibleMoves[iteration] = move;
            iteration++;
            //populate move array with all possible moves
        }
        while (iteration != 0) {
            possibleGames[iteration - 1] = game.copy();
            --iteration;
            //create a copy of game
        }
        int highScore = -1;
        int bestIndex = 0;
        int currScore;

        for (int i = 0; i < 5; i++) {
            for (int iters = 0; iters < 32; iters++) {
                //advance each state in games arr by 16
                possibleGames[i].advanceGame(possibleMoves[i], new StarterGhosts().getMove(possibleGames[i].copy(), -1));
            }
            currScore = possibleGames[i].getScore();
            if (highScore < currScore) {
                highScore = currScore;
                bestIndex = i;
            }
        }
        // return move that got highest score
        return possibleMoves[bestIndex];
    }
}

