public class Parameter {
    public int populationSize;
    public double mutationProbGene;
    public double mutationProbIndividual;
    public double crossoverRate;
    public double elitismCountperCent; //taxa
    public int numberOfGenerations;

    public Parameter() {
    }

    public Parameter(int populationSize, double mutationProbGene, double mutationProbIndividual, double crossoverRate, double elitismCountperCent, int numberOfGenerations) {
        this.populationSize = populationSize;
        this.mutationProbGene = mutationProbGene;
        this.mutationProbIndividual = mutationProbIndividual;
        this.crossoverRate = crossoverRate;
        this.elitismCountperCent = elitismCountperCent;
        this.numberOfGenerations = numberOfGenerations;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public double getMutationProbGene() {
        return mutationProbGene;
    }

    public void setMutationProbGene(double mutationProbGene) {
        this.mutationProbGene = mutationProbGene;
    }

    public double getMutationProbIndividual() {
        return mutationProbIndividual;
    }

    public void setMutationProbIndividual(double mutationProbIndividual) {
        this.mutationProbIndividual = mutationProbIndividual;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public double getElitismCountperCent() {
        return elitismCountperCent;
    }

    public void setElitismCountperCent(double elitismCountperCent) {
        this.elitismCountperCent = elitismCountperCent;
    }

    public int getNumberOfGenerations() {
        return numberOfGenerations;
    }

    public void setNumberOfGenerations(int numberOfGenerations) {
        this.numberOfGenerations = numberOfGenerations;
    }
}
