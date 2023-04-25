import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class EightQueensGA {
    /* This implementation uses a population of 100 solutions, evolves the population
    for 1000 generations, and uses tournament selection to select parents for crossover.
    Each solution is represented by an array of integers representing the column index
    of each queen in its row. The fitness of a solution is the number of pairs of queens
    that are attacking each other. Crossover is performed by randomly selecting a row
    from each parent, and the child is mutated by randomly changing one of its positions
    with a probability of 0.01. The best solution found is printed after each generation,
    and the best solution overall is printed at the end.
    */
    private static final int POPULATION_SIZE = 100;
    private static final int NUM_GENERATIONS = 1000;
    private static final double MUTATION_RATE = 0.01;
    private static final int BOARD_SIZE = 8;
    private static Random random = new Random();

    public EightQueensGA() {
        // Initialize the population
        Queen[] population = new Queen[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = new Queen();
        }
        // Evolve the population for a fixed number of generations
        for (int i = 0; i < NUM_GENERATIONS; i++) {
            // Sort the population by fitness
            Arrays.sort(population);
            // Print the best solution of this generation
            System.out.println("Generation " + i + ":\n" + population[0]);
            // Check if we have found a solution
            if (population[0].getFitness() == 0) {
                break;
            }
            // Create the next generation
            Queen[] newPopulation = new Queen[POPULATION_SIZE];
            for (int j = 0; j < POPULATION_SIZE; j++) {
                // Select two parents from the population
                Queen parent1 = selectParent(population);
                Queen parent2 = selectParent(population);
                // Crossover the parents to create a new child
                Queen child = crossover(parent1, parent2);
                // Mutate the child
                mutate(child);
                // Add the child to the new population
                newPopulation[j] = child;
            }
            // Replace the old population with the new population
            population = newPopulation;
        }
        // Print the best solution we found
        Arrays.sort(population);
        System.out.println("Best solution found:\n" + population[0]);
    }

    public static void main(String[] args) {
        new EightQueensGA();
    }

    // Select a parent from the population using tournament selection
    private Queen selectParent(Queen[] population) {
        ArrayList<Queen> tournament = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tournament.add(population[random.nextInt(population.length)]);
        }
        Collections.sort(tournament);
        return tournament.get(0);
    }

    // Crossover two parents to create a new child
    private Queen crossover(Queen parent1, Queen parent2) {
        Queen child = new Queen();
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (random.nextDouble() < 0.5) {
                child.setRow(i, parent1.getRows()[i]);
            } else {
                child.setRow(i, parent2.getRows()[i]);
            }
        }
        return child;
    }

    // Mutate a queen by randomly changing one of its positions
    private void mutate(Queen queen) {
        if (random.nextDouble() < MUTATION_RATE) {
            int row = random.nextInt(BOARD_SIZE);
            int col = random.nextInt(BOARD_SIZE);
            queen.setRow(row, col);
        }
    }

    // The Queen class represents a solution to the 8-Queens problem
    private class Queen implements Comparable<Queen> {
        private int[] rows;
        private int fitness;

        public Queen() {
            this.rows = new int[BOARD_SIZE];
            for (int i = 0; i < BOARD_SIZE; i++) {
                setRow(i, random.nextInt(BOARD_SIZE));
            }
            calculateFitness();
        }

        public int[] getRows() {
            return rows;
        }

        public int getFitness() {
            return fitness;
        }

        public void setRow(int row, int col) {
            rows[row] = col;
            calculateFitness();
        }

        private void calculateFitness() {
            fitness = 0;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = i + 1; j < BOARD_SIZE; j++) {
                    // Check if the queens are in the same row or diagonal
                    if (rows[i] != rows[j] && Math.abs(rows[i] - rows[j]) != j - i) {
                        fitness++;
                    }
                }
            }
        }

        @Override
        public int compareTo(Queen other) {
            return Integer.compare(other.getFitness(), getFitness());
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (rows[i] == j) {
                        sb.append("Q ");
                    } else {
                        sb.append(". ");
                    }
                }
                sb.append("\n");
            }
            sb.append("Fitness: ").append(fitness);
            return sb.toString();
        }
    }
}