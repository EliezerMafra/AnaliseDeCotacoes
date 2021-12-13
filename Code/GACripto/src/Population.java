import java.util.Arrays;
import java.util.Comparator;

public class Population {
    private Individual population[];
    private double populationFitness;
    private long generation;
    private double covarMatrix[][];

    public Population(int populationSize, long generation, double[][] covarMatrix){
        this.population = new Individual[populationSize];
        this.generation = generation;
        this.covarMatrix = covarMatrix;
    }

    public Population(int populationSize, int chromossomeSize, double[][] covarMatrix) {
        this.population = population = new Individual[populationSize];
        this.generation = 1;
        this.covarMatrix = covarMatrix;

        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual(chromossomeSize);

            this.population[i] = individual;
        }
    }

    public long getGeneration() {
        return generation;
    }

    public void setGeneration(long generation) {
        this.generation = generation;
    }

    public Individual[] getPopulation() {
        return population;
    }

    public double getPopulationFitness() {
        return populationFitness;
    }

    public void setPopulationFitness(double populationFitness) {
        this.populationFitness = populationFitness;
    }

    /**
     * Find an individual in the population by its fitness
     *
     * This method lets you select an individual in order of its fitness. This
     * can be used to find the single strongest individual (eg, if you're
     * testing for a solution), but it can also be used to find weak individuals
     * (if you're looking to cull the population) or some of the strongest
     * individuals (if you're using "elitism").
     *
     * @param offset
     *            The offset of the individual you want, sorted by fitness. 0 is
     *            the strongest, population.length - 1 is the weakest.
     * @return individual Individual at offset
     */
    public Individual getFittest(int offset){
        /*Arrays.sort(this.population, (o1, o2) -> {
            if (o1.getFitness() < o2.getFitness()) {
                return -1;
            } else if (o1.getFitness() > o2.getFitness()) {
                return 1;
            }
            return 0;
        });*/

        Arrays.sort(this.population, (o1, o2) -> {
            if (Math.abs(o1.getFitness()) < Math.abs(o2.getFitness())) {
                return -1;
            } else if (Math.abs(o1.getFitness()) > Math.abs(o2.getFitness())) {
                return 1;
            }
            return 0;
        });



        return this.population[offset];
    }

    public int size(){
        return this.population.length;
    }

    public Individual setIndividual(int offset, Individual individual){
        return population[offset] = individual;
    }

    public Individual getIndividual(int offset){
        return population[offset];
    }

    public double[][] getCovarMatrix() {
        return covarMatrix;
    }
}
