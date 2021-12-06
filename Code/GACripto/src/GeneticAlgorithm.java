import java.util.Random;

public class GeneticAlgorithm {
    private int populationSize;
    private double mutationProbGene;
    private double mutationProbIndividual;
    private double crossoverRate;
    private int elitismCount;
    private int numberGenerationsToFinish;

    public GeneticAlgorithm() {
    }

    public GeneticAlgorithm(int populationSize, double mutationProbGene, double mutationProbIndividual, double crossoverRate, int elitismCount, int numberGenerationsToFinish) {
        this.populationSize = populationSize;
        this.mutationProbGene = mutationProbGene;
        this.mutationProbIndividual = mutationProbGene;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.numberGenerationsToFinish = numberGenerationsToFinish;
    }

    public Population initPopulation(int chromossomeLength, double[][] covarMatrix){
        Population population = new Population(this.populationSize,chromossomeLength, covarMatrix);
        return population;
    }

    public static double calcFitness(Individual individual, double[][] covarMatrix){
        float fitness = 0;

        double sumI = 0;

        for (int i = 0; i < individual.getChromossomeLength(); i++) {

            double sumJ = 0;

            for (int j = 0; j < individual.getChromossomeLength(); j++) {
                sumJ += individual.getGene(i)*individual.getGene(j)*covarMatrix[i][j];
            }

            sumI += sumJ;
        }

        fitness = (float) sumI;
        individual.setFitness(fitness);
        return sumI;

    }

    public void evalPopulation(Population population){
        double populationFitness = 0;
        int i = 0;

        for (Individual individual: population.getPopulation()) {
            populationFitness += calcFitness(individual, population.getCovarMatrix());
            i++;
        }

        population.setPopulationFitness(populationFitness);
    }

    public boolean isTerminationConditionMet(Population population){
        if (population.getGeneration() >= this.numberGenerationsToFinish) return true;
        return false;
    }

    public Individual selection(Population population){
        Individual individuals[] = population.getPopulation();

        double populationFitness = population.getPopulationFitness();
        double rouletteWheelPosition = Math.random() * populationFitness;

        double spinWheel = 0;

        for (Individual ind: individuals) {
            spinWheel += ind.getFitness();
            if (spinWheel >= rouletteWheelPosition){
                return ind;
            }
        }

        return individuals[population.size() - 1];
    }

    public Population crossover(Population population){
        Population newPopulation = new Population(population.size(), population.getGeneration()+1, population.getCovarMatrix());

        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual parent1 = population.getFittest(populationIndex);

            if (populationIndex > this.elitismCount && Math.random() < this.crossoverRate){

                Individual parent2;

                do{
                    parent2 = selection(population);
                }while (parent1.equals(parent2));

                Individual offpring = new Individual(parent1.getChromossomeLength());

                for (int geneIndex = 0; geneIndex < parent1.getChromossomeLength(); geneIndex++) {
                    if(Math.random() < 0.5){
                        offpring.setGene(geneIndex, parent1.getGene(geneIndex));
                    }else{
                        offpring.setGene(geneIndex, parent2.getGene(geneIndex));
                    }
                }

                offpring.normalize();

                newPopulation.setIndividual(populationIndex, offpring);
            }else{
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }

        return newPopulation;
    }

    public Population mutation(Population population){
        Population newPopulation = new Population(population.size(), population.getGeneration(), population.getCovarMatrix());

        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);
            Random r = new Random();

            if (populationIndex > this.elitismCount &&  Math.random() < this.mutationProbIndividual){
                for (int geneIndex = 0; geneIndex < individual.getChromossomeLength(); geneIndex++) {
                    if (Math.random() < this.mutationProbGene){
                        individual.setGene(geneIndex, r.nextFloat());
                    }
                }

                individual.normalize();
            }

            newPopulation.setIndividual(populationIndex, individual);
        }

        return newPopulation;
    }
}
