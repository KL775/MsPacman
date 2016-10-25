package pacman.entries.pacman.Optimization;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.Random;

/**
 * Created by Kevin on 10/23/2016.
 *Always returns neutral for some reason
 */
public class simulatedAnnealing extends Controller<MOVE> {
    private double Temperature = 10000;
    private double coolingRate = 0.03;
    public MOVE getMove(Game game, long timeDue) {
        return simulatedAnnealing(game);
    }

    private MOVE simulatedAnnealing(Game initState){
        //values selected at random temporarily
        Random randomGen = new Random();
        nodeStates currentState = new nodeStates(initState);
        int randIndex = randomGen.nextInt(currentState.getPossibleMoves().length);
        MOVE randMove = currentState.getPossibleMoves()[randIndex];
        Game advState = currentState.getCurrentState().copy();
        for (int i = 0; i < 4; ++i){
            advState.advanceGame(randMove, new StarterGhosts().getMove(currentState.getCurrentState().copy(), -1));
        }
        nodeStates nextState = new nodeStates(advState);
        double energy =  currentState.evaluateCurrentState();
        double newEnergy = nextState.evaluateCurrentState();
        double prob = acceptanceProbability(energy, newEnergy,Temperature);
        System.out.println("Current Energy " + energy);
        System.out.println("New Energy " + newEnergy);
        System.out.println("Evaluation probability " + prob);
        Temperature *= 1-coolingRate;
        if (prob > Math.random()){
            return nextState.getCurrentState().getPacmanLastMoveMade();
        }
        return MOVE.NEUTRAL;
    }

    private double acceptanceProbability(double oldEnergy, double newEnergy, double currentTemp){
        //higher energy is better
        if (newEnergy > oldEnergy){
            return 1.0; //always perform this action
        }
        return Math.exp((oldEnergy-newEnergy)/currentTemp); //give probability
    }
}
