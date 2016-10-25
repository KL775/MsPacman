package pacman.entries.pacman.EvolutionaryStrategies;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Created by Kevin on 10/24/2016.
 */
public class GeneticCrossover extends Controller<MOVE> {
    private MOVE[] possibleMoves = MOVE.values();
    private int populationSizeDefault = 20;
    private int generations = 20;

    //Strategy, keep fit individuals, remove unfit individuals,
    //mate most fit individuals and create offspring
    //do so by combining half of first sequence and half of second sequence and then mutate each index randomly
    //remove unfit individuals
    //fitness == score
    //action sequence will be 10
    public MOVE getMove(Game game, long timeDue) {
        Random randNum = new Random();
        PriorityQueue<individual> currentPopulation = new PriorityQueue<>();
        PriorityQueue<individual> newPopulation = new PriorityQueue<>();
        ArrayList<individual> babyMakers = new ArrayList<>();
        for (int i = 0; i < populationSizeDefault; ++i) {
            individual newBorn = new individual(game.copy(), 0, generateActions(10));
            currentPopulation.add(newBorn);
        }

        for (int i = 0; i < generations; ++i) {
            //advance generations and set up individual fitness
            for (individual Individuals : currentPopulation) {
                Game currentGame = Individuals.getGame();
                ArrayList<MOVE> indivMoves = Individuals.getMoveSeq();
                for (MOVE move : indivMoves) {
                    //advance game by intended sequence
                    for (int j = 0; j < 5; j++) {
                        //advance by 4
                        currentGame.advanceGame(move, new StarterGhosts().getMove(currentGame.copy(), -1));
                    }
                }
                Individuals.setFitness(evaluateCurrentState(currentGame));
                newPopulation.add(Individuals);
            }
            currentPopulation.clear();
            int currentPopulationSize = 0;
            for (int k = 0; k < populationSizeDefault / 2; k++){
                individual nextToGo = newPopulation.remove();
                currentPopulation.add(nextToGo);
                babyMakers.add(nextToGo);
                currentPopulationSize++;
            }

            while(currentPopulationSize < populationSizeDefault){
                int randOne = randNum.nextInt(babyMakers.size());
                int randTwo = randNum.nextInt(babyMakers.size());
                while (randOne == randTwo){
                    randOne = randNum.nextInt(babyMakers.size());
                }
                individual dad = babyMakers.get(randOne);
                individual mom = babyMakers.get(randTwo);
                ArrayList <MOVE> kidAndTheirMoves = mateSequence(dad.getMoveSeq(), mom.getMoveSeq());
                individual kiddo = new individual(game.copy(), 0, kidAndTheirMoves);
                currentPopulationSize++;
            }
            //clear out for future use
            babyMakers.clear();
        }
        return currentPopulation.peek().getMoveSeq().remove(0);
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

    private ArrayList<MOVE> mateSequence(ArrayList<MOVE> dad, ArrayList<MOVE> mom) {
        ArrayList<MOVE> child = new ArrayList<>();
        for (int i = 0; i < dad.size(); ++i) {
            if (i < dad.size() / 2) {
                child.add(dad.get(i));
            } else {
                child.add(mom.get(i));
            }
        }
        return child;
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
