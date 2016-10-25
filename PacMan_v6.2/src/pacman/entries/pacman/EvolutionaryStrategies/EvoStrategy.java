package pacman.entries.pacman.EvolutionaryStrategies;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Random;


/**
 * Created by Kevin on 10/23/2016.
 */
public class EvoStrategy extends Controller<MOVE> {
    MOVE[] possibleMoves = MOVE.values();
    int populationSizeDefault = 20;
    int generations = 30;

    //Strategy, keep fit individuals, remove unfit individuals,
    //mutate fit individuals by 4 action and add back to priority queue
    //fitness == score
    //action sequence will be 8
    public MOVE getMove(Game game, long timeDue) {
        PriorityQueue<individual> currentPopulation = new PriorityQueue<>();
        ArrayList<individual> mutateMe = new ArrayList<>();
        PriorityQueue<individual> dupePopulation = new PriorityQueue<>();
        //create random individuals
        for (int i = 0; i < populationSizeDefault; ++i) {
            individual newBorn = new individual(game.copy(), 0, generateActions(20));
            currentPopulation.add(newBorn);
        }
        //System.out.println(currentPopulation.size());
        for (int i = 0; i < generations; ++i) {
            //advance generations
            for (individual Individuals : currentPopulation) {
                Game currentGame = Individuals.getGame();
                ArrayList<MOVE> indivMoves = Individuals.getMoveSeq();
                for (MOVE move : indivMoves) {
                    //advance game by intended sequence
                    for (int k = 0; k < 6; k++) {
                        //look ahead 5 moves
                        currentGame.advanceGame(move, new StarterGhosts().getMove(currentGame.copy(), -1));
                    }
                }
                Individuals.setFitness(evaluateCurrentState(currentGame));
                dupePopulation.add(Individuals);
            }
            currentPopulation.clear();
            int newPopulationSize = 0;
            for (int k = 0; k < populationSizeDefault / 4; k++) {
                individual temp = dupePopulation.remove();
                currentPopulation.add(temp);
                mutateMe.add(temp);
                newPopulationSize++;
            }
            while (newPopulationSize < populationSizeDefault) {
                Random randomNum = new Random();
                int randIndex = randomNum.nextInt(mutateMe.size());
                individual mutating = mutateMe.get(randIndex);
                ArrayList<MOVE> unmutatedSeq = mutating.getMoveSeq();
                ArrayList<MOVE> mutatedSeq = mutateSequence(unmutatedSeq);
                individual mutatedPerson = new individual(game.copy(), 0, mutatedSeq);
                currentPopulation.add(mutatedPerson);
                newPopulationSize += 1;
            }
            mutateMe.clear(); //free up space to use mutateMe again
        }
        //return greatest score move sequence
        return currentPopulation.remove().getMoveSeq().remove(0);
    }

    public ArrayList<MOVE> generateActions(int seqLen) {
        ArrayList<MOVE> actionsToTake = new ArrayList<>();
        Random randomNum = new Random();
        for (int i = 0; i < seqLen; ++i) {
            int randomIndex = randomNum.nextInt(5);
            actionsToTake.add(possibleMoves[randomIndex]);
            //create an array with random moves
        }
        return actionsToTake;
    }

    public ArrayList<MOVE> mutateSequence(ArrayList<MOVE> prevActions) {
        ArrayList<MOVE> newActions = new ArrayList<>();
        Random randomNum = new Random();
        int counter = 0;
        for (int i = 0; i < prevActions.size(); ++i) {
            int toMutate = randomNum.nextInt(1); //gives 1 or 0
            if (toMutate == 1) {
                int indexMutated = randomNum.nextInt(5);
                newActions.add(possibleMoves[indexMutated]);

            } else if (toMutate == 0) {
                newActions.add(prevActions.get(i));
            }
        }
        newActions = prevActions;
        return newActions;
    }

    public double evaluateCurrentState(Game currentState) {
        int GhostPanicDist = 200;
        int closestGhostDist = Integer.MAX_VALUE;
        int closestScaredGhostDist = Integer.MAX_VALUE;
        int inactiveGhosts = 0;
        for (Constants.GHOST ghosts : Constants.GHOST.values()) {
            int currentDist = currentState.getManhattanDistance(currentState.getPacmanCurrentNodeIndex(), currentState.getGhostCurrentNodeIndex(ghosts));
            if (currentState.getGhostEdibleTime(ghosts) > 0) {
                if (currentDist < closestScaredGhostDist){
                    closestGhostDist = currentDist;
                }
                break;
            }
            else{
                if (currentDist < closestGhostDist){
                    closestGhostDist = currentDist;
                }
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
        return (currentState.getScore() + -4 * closestGhostDist + 50 * closestScaredGhostDist + -2 * pillsLeft + -3.5 * powPillsLeft)*currentState.getPacmanNumberOfLivesRemaining();
    }
};