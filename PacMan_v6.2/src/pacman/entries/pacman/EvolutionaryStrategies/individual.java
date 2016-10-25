package pacman.entries.pacman.EvolutionaryStrategies;

import pacman.game.Constants.*;
import pacman.game.Game;

import java.util.ArrayList;

/**
 * Created by Kevin on 10/23/2016.
 */
public class individual implements Comparable<individual> {
    private Game game;
    private double fitness;
    private ArrayList<MOVE> moveSeq;
    public individual(Game game, int fitness, ArrayList<MOVE> moveSeq){
        this.game = game;
        this.fitness = fitness;
        this.moveSeq = moveSeq;
    }
    public int compareTo(individual otherDude){
        return (-1*(Double.compare(this.fitness, otherDude.fitness)));
    }
    public void setFitness(double newFitness){
        fitness = newFitness;
    }
    public double getFitness(){
        return fitness;
    }
    public Game getGame(){
        return game;
    }
    public void setGame(Game newGame){
        game = newGame;
    }
    public void setMoveSeq(ArrayList<MOVE> newSeq){
        moveSeq = newSeq;
    }
    public ArrayList<MOVE> getMoveSeq(){
        return moveSeq;
    }
}
