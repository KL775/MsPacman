package pacman.entries.pacman.EvolutionaryStrategies;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.PriorityQueue;


/**
 * Created by Kevin on 10/23/2016.
 */
public class EvoStrategy extends Controller<MOVE>{
    MOVE[] possibleMoves = MOVE.values();
    int populationSizeDefault = 16;
    int generations = 80;
    public MOVE getMove(Game game, long timeDue){
        PriorityQueue<individual> currentPopulation = new PriorityQueue<>();
        ArrayList<individual> mutateMe = new ArrayList<>();
        //create random individuals
        for (int i = 0; i < populationSizeDefault; ++i){
            individual newBorn = new individual(game.copy(), 0, generateActions(10));
            currentPopulation.add(newBorn);
        }
        //System.out.println(currentPopulation.size());
        for (int i = 0; i < generations; ++i){
            //advance generations
            for (individual Individuals : currentPopulation) {
                Game currentGame = Individuals.getGame();
                ArrayList<MOVE> indivMoves = Individuals.getMoveSeq();
                for (MOVE move : indivMoves){
                    //advance game by intended sequence
                    currentGame.advanceGame(move, new StarterGhosts().getMove(currentGame.copy(),-1));
                }
                Individuals.setFitness(currentGame.getScore());
            }
            PriorityQueue<individual> dupePopulation = currentPopulation;
            for (int k = 0; k < populationSizeDefault/2; ++k){
                //removes top minimal value 8x, reducing heap by half
                dupePopulation.remove();
            }
            mutateMe.addAll(dupePopulation); //move fit population into queue
            for (individual Individuals : mutateMe){
                ArrayList<MOVE> oldMoves = Individuals.getMoveSeq();
                ArrayList<MOVE> mutatedMoves = mutateSequence(oldMoves);
                individual mutatednewBorn = new individual(game.copy(), 0, mutatedMoves);
                dupePopulation.add(mutatednewBorn);
            }
            mutateMe.clear(); //free up space to use mutateMe again
            currentPopulation = dupePopulation;
        }
        for (int i = 0; i < currentPopulation.size()-1; ++i){
            //pop everything but the greatest value
            currentPopulation.remove();
        }
        //return greatest score move sequence
        return currentPopulation.peek().getMoveSeq().remove(0);
    }

    public ArrayList<MOVE> generateActions(int seqLen){
        ArrayList<MOVE> actionsToTake = new ArrayList<>();
        for (int i = 0; i < seqLen; ++i){
            int randomIndex = (int)Math.floor(Math.random()*5);
            actionsToTake.add(possibleMoves[randomIndex]);
            //create an array with random moves
        }
        return actionsToTake;
    }

    public ArrayList<MOVE> mutateSequence(ArrayList<MOVE> prevActions){
        ArrayList<MOVE> newActions = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < prevActions.size(); ++i){
            int toMutate = (int)Math.floor(Math.random()*2);
            if (toMutate == 1){
                int randomIndex = (int)Math.floor(Math.random()*5);
                prevActions.set(i, possibleMoves[randomIndex]);
                ++counter;
            }
            if(counter == 4){
                break;
            }
        }
        newActions = prevActions;
        return newActions;
    }
}
