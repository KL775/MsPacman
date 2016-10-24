package pacman.entries.pacman.Optimization;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Created by Kevin on 10/23/2016.
 *Always returns neutral for some reason
 */
public class simulatedAnnealing extends Controller<MOVE> {
    public MOVE getMove(Game game, long timeDue) {
        return simulatedAnnealing(game);
    }
    private MOVE simulatedAnnealing(Game initState){
        //values selected at random temporarily
        double Temperature = 10000;
        double coolingRate = 0.03;
        MOVE[] possibleMoves = MOVE.values();
        Game currentState = initState;
        Game nextState;
        int highScore = currentState.getScore();
        int currScore;
        MOVE bestMove = MOVE.NEUTRAL;
        while (true) {
            if (Temperature < 1){
                return bestMove;
            }
            int randomAdvanceMove = (int)Math.floor(Math.random()*5);
            nextState = currentState;
            nextState.advanceGame(possibleMoves[randomAdvanceMove], new StarterGhosts().getMove(currentState.copy(), -1));
            currScore = nextState.getScore();
            if (acceptanceProbability(highScore, currScore, Temperature) > Math.random()) {
                bestMove = possibleMoves[randomAdvanceMove];
                //System.out.println(bestMove+" made.");
                highScore = currScore;
                currentState = nextState; //advance state and prepare for testing again
            }
            Temperature *= 1-coolingRate;
        }
    }

    private double acceptanceProbability(int oldScore, int newScore, double currentTemp){
        //higher score = better
        if (newScore > oldScore){
            return 1.0; //always perform this action
        }
        return Math.exp((newScore-oldScore)/currentTemp); //give probability
    }
}
